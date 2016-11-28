package fourschlag.services.data.requests;

import fourschlag.entities.accessors.ActualSalesAccessor;
import fourschlag.entities.accessors.ForecastSalesAccessor;
import fourschlag.entities.tables.Entity;
import fourschlag.entities.tables.ForecastSalesEntity;
import fourschlag.entities.tables.SalesEntity;
import fourschlag.entities.types.*;
import fourschlag.entities.types.KeyPerformanceIndicators;
import fourschlag.services.db.CassandraConnection;

import java.util.*;

import static fourschlag.entities.types.KeyPerformanceIndicators.*;

/**
 * Extends Request. Offers Functionality to request Sales KPIs for a specific
 * region, period and product main group
 */
public class SalesRequest extends KpiRequest {

    private final String productMainGroup;
    private final SalesType salesType;
    private final ActualSalesAccessor actualAccessor;
    private final ForecastSalesAccessor forecastAccessor;
    private static final String FC_TYPE = "sales";

    /**
     * Constructor for SalesRequest
     *
     * @param connection       Cassandra connection that is supposed to be used
     * @param productMainGroup Product Main Group to filter for
     * @param planYear         Indicates the time span for which the KPIs are
     *                         supposed to be queried
     * @param currentPeriod    The point of view in time from which the data is
     *                         supposed to be looked at
     * @param region           Region to filter for
     * @param salesType        Sales Type to filter for
     * @param exchangeRates    Desired output currency
     */
    public SalesRequest(CassandraConnection connection, String productMainGroup, int planYear, Period currentPeriod,
                        String region, SalesType salesType, ExchangeRateRequest exchangeRates,
                        OrgStructureAndRegionRequest orgAndRegionRequest) {
        super(connection, orgAndRegionRequest.getSbu(productMainGroup), region, planYear , currentPeriod, exchangeRates, FC_TYPE);
        this.productMainGroup = productMainGroup;
        this.salesType = salesType;

        /* Create needed accessors to be able to do queries */
        actualAccessor = getManager().createAccessor(ActualSalesAccessor.class);
        forecastAccessor = getManager().createAccessor(ForecastSalesAccessor.class);
    }

    /**
     * Queries KPIs from the actual sales table
     *
     * @return SalesEntity Object with query result
     */
    @Override
    protected SalesEntity getActualData(Period tempPlanPeriod) {
        /* Send query to the database with data source BW B */
        SalesEntity queryResult = actualAccessor.getSalesKPIs(productMainGroup, tempPlanPeriod.getPeriod(), region,
                salesType.getType(), DataSource.BW_B.toString());

        /* IF result is empty THEN query again with data source BW A */
        if (queryResult == null) {
            queryResult = actualAccessor.getSalesKPIs(productMainGroup, tempPlanPeriod.getPeriod(), region,
                    salesType.getType(), DataSource.BW_A.toString());
            /* IF result is NOT empty THEN get cm1 value from forecast data and put it in the query result */
            if (queryResult != null) {
                queryResult.setCm1(getForecastCm1(tempPlanPeriod));
            }
        }
        return queryResult;
    }

    /**
     * Queries KPIs from the forecast sales table
     *
     * @return SalesEntity Object with query result
     */
    @Override
    protected SalesEntity getForecastData(Period tempPlanPeriod, EntryType entryType) {
        SalesEntity queryResult = forecastAccessor.getSalesKpis(productMainGroup, currentPeriod.getPeriod(),
                tempPlanPeriod.getPeriod(), region, salesType.toString(), entryType.toString());
        if (queryResult == null) {
            queryResult = forecastAccessor.getSalesKpis(productMainGroup, currentPeriod.getPreviousPeriod(),
                    tempPlanPeriod.getPeriod(), region, salesType.toString(), entryType.toString());
        }
        return queryResult;
    }

    /**
     * Gets the cm1 value from the forecast sales table
     *
     * @return double value of cm1
     */
    private double getForecastCm1(Period tempPlanPeriod) {
        /* TODO: Currency conversion */
        SalesEntity cm1 = forecastAccessor.getCm1(productMainGroup, currentPeriod.getPeriod(),
                tempPlanPeriod.getPeriod(), region, salesType.toString());

        if (cm1 == null) {
            return 0;
        }
        return cm1.getCm1();
    }

    @Override
    protected SalesEntity getBudgetData(Period tempPlanPeriod) {
        return forecastAccessor.getSalesKpis(productMainGroup, tempPlanPeriod.getPeriod(), tempPlanPeriod.getPeriod(),
                region, salesType.toString(), EntryType.BUDGET.toString());
    }

    /**
     * Private method that calculates the BJ values for all KPIs but one specific period (--> zero month period)
     *
     * @param zeroMonthPeriod ZeroMonthPeriod of the desired budget year
     */
    @Override
    protected ValidatedResult calculateBj(Period zeroMonthPeriod) {
        SalesEntity queryResult = forecastAccessor.getSalesKpis(productMainGroup, currentPeriod.getPeriod(),
                zeroMonthPeriod.getPeriod(), region, salesType.toString(), EntryType.BUDGET.getType());

        return validateQueryResult(queryResult, new Period(zeroMonthPeriod));
    }

    @Override
    protected ValidatedResultTopdown calculateBjTopdown(Period zeroMonthPeriod) {
        SalesEntity queryResult = forecastAccessor.getSalesKpis(productMainGroup, currentPeriod.getPeriod(),
                zeroMonthPeriod.getPeriod(), region, salesType.toString(), EntryType.BUDGET.getType());

        return validateTopdownQueryResult(queryResult, new Period(zeroMonthPeriod));
    }

    /**
     * Private method to validate a query result.
     *
     * @param result    The query result that will be validated
     * @param tempPlanPeriod planPeriod of that query result
     * @return Map with all the values for the sales KPIs
     */
    @Override
    protected ValidatedResultTopdown validateTopdownQueryResult(Entity result, Period tempPlanPeriod) {
        SalesEntity queryResult = (SalesEntity) result;
        /* Prepare the kpi variables */

        ValidatedResultTopdown validatedResult = new ValidatedResultTopdown(validateQueryResult(queryResult, tempPlanPeriod).getKpiResult());

        Map<KeyPerformanceIndicators, Double> topdownMap = validatedResult.getTopdownResult();

        /* IF the result of the query is empty THEN set these KPIs to 0
         * ELSE get the values from the query result
         */
        if (queryResult != null) {
            if(queryResult.getClass().isInstance(ForecastSalesEntity.class)) {
                ForecastSalesEntity fcEntity = (ForecastSalesEntity) queryResult;
                topdownMap.put(SALES_VOLUME, fcEntity.getTopdownAdjustSalesVolumes());
                topdownMap.put(NET_SALES, fcEntity.getTopdownAdjustNetSales());
                topdownMap.put(CM1, fcEntity.getTopdownAdjustCm1());
            }

            /* IF the currency of the KPIs is not the desired one THEN get the exchange rate and convert them */
            if (queryResult.getCurrency().equals(exchangeRates.getToCurrency()) == false) {
                double exchangeRate = exchangeRates.getExchangeRate(tempPlanPeriod, queryResult.getCurrency());

                for (KeyPerformanceIndicators kpi : kpiArray) {
                    topdownMap.put(kpi, topdownMap.get(kpi) * exchangeRate);
                }
            }
        }

        calculateRemainingSalesKpis(topdownMap);

        return validatedResult;
    }

    @Override
    protected ValidatedResult validateQueryResult(Entity result, Period tempPlanPeriod) {
        SalesEntity queryResult = (SalesEntity) result;

        ValidatedResult validatedResult = new ValidatedResult(kpiArray);

        Map<KeyPerformanceIndicators, Double> kpiMap = validatedResult.getKpiResult();
        /* IF the result of the query is empty THEN set these KPIs to 0
         * ELSE get the values from the query result
         */
        if (queryResult != null) {
            kpiMap.put(SALES_VOLUME, queryResult.getSalesVolumes());
            kpiMap.put(NET_SALES, queryResult.getNetSales());
            kpiMap.put(CM1, queryResult.getCm1());

            /* IF the currency of the KPIs is not the desired one THEN get the exchange rate and convert them */
            if (queryResult.getCurrency().equals(exchangeRates.getToCurrency()) == false) {
                double exchangeRate = exchangeRates.getExchangeRate(tempPlanPeriod, queryResult.getCurrency());

                for (KeyPerformanceIndicators kpi : kpiMap.keySet()) {
                    kpiMap.put(kpi, kpiMap.get(kpi) * exchangeRate);
                }
            }
        }

        calculateRemainingSalesKpis(kpiMap);

        return validatedResult;
    }

    private void calculateRemainingSalesKpis(Map<KeyPerformanceIndicators, Double> map) {
        /* IF sales volume is not 0 THEN calculate these other KPIs */
        if (map.get(SALES_VOLUME) != 0) {
            map.put(PRICE, map.get(NET_SALES) / map.get(SALES_VOLUME) * 1000);
            map.put(VAR_COSTS, (map.get(NET_SALES) - map.get(CM1)) * 1000 / map.get(SALES_VOLUME));
            map.put(CM1_SPECIFIC, map.get(CM1) / map.get(SALES_VOLUME) * 1000);
        }

        /* IF net sales is not 0 THEN calculate these other KPIs  */
        if (map.get(NET_SALES) != 0) {
            map.put(CM1_PERCENT, map.get(CM1) / map.get(NET_SALES));
        }
    }

    /**
     * Creates a OutputDataType Object with all given attributes
     *
     * @param kpi           KPI that is supposed to be set in the
     *                      OutputDataType
     * @param monthlyValues The monthly values for the KPI
     * @return OutputDataType object
     */
    @Override
    protected OutputDataType createOutputDataType(KeyPerformanceIndicators kpi, EntryType entryType,
                                                LinkedList<Double> monthlyValues, LinkedList<Double> bjValues) {
        return new OutputDataType(kpi, sbu, productMainGroup,
                region, region, salesType.toString(), entryType.toString(), exchangeRates.getToCurrency(), monthlyValues,
                bjValues);
    }

}
