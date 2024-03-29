package fourschlag.services.data.requests.kpi;

import com.datastax.driver.mapping.Result;
import fourschlag.entities.accessors.sales.ActualSalesAccessor;
import fourschlag.entities.accessors.sales.ForecastSalesAccessor;
import fourschlag.entities.tables.kpi.KpiEntity;
import fourschlag.entities.tables.kpi.sales.ActualSalesEntity;
import fourschlag.entities.tables.kpi.sales.ForecastSalesEntity;
import fourschlag.entities.tables.kpi.sales.SalesEntity;
import fourschlag.entities.types.*;
import fourschlag.entities.types.KeyPerformanceIndicators;
import fourschlag.services.data.requests.ExchangeRateRequest;
import fourschlag.services.data.requests.OrgStructureAndRegionRequest;
import fourschlag.services.db.CassandraConnection;

import java.util.LinkedList;
import java.util.Map;

import static fourschlag.entities.types.KeyPerformanceIndicators.*;

/**
 * Extends Request. Offers Functionality to request Sales KPIs for a specific
 * region, period and product main group
 */
public class SalesKpiRequest extends KpiRequest {

    private static final String FC_TYPE = "sales";
    private String productMainGroup;
    private SalesType salesType;
    private ActualSalesAccessor actualAccessor;
    private ForecastSalesAccessor forecastAccessor;

    /**
     * Constructor for SalesKpiRequest
     *
     * @param connection          Cassandra connection that is supposed to be
     *                            used
     * @param productMainGroup    Product Main Group to filter for
     * @param planPeriod          Indicates the time span for which the KPIs are
     *                            supposed to be queried
     * @param currentPeriod       The point of view in time from which the data
     *                            is supposed to be looked at
     * @param region              Region to filter for
     * @param salesType           Sales Type to filter for
     * @param exchangeRates       ExchangeRateRequest with the desired output
     *                            currency
     * @param orgAndRegionRequest OrgStructureAndRegionRequest instance
     */
    public SalesKpiRequest(CassandraConnection connection, String productMainGroup, Period planPeriod, Period currentPeriod,
                           String region, SalesType salesType, ExchangeRateRequest exchangeRates,
                           OrgStructureAndRegionRequest orgAndRegionRequest) {
        super(connection, orgAndRegionRequest.getSbuByPmg(productMainGroup), region, planPeriod, currentPeriod, exchangeRates, FC_TYPE);
        this.productMainGroup = productMainGroup;
        this.salesType = salesType;

        /* Create needed accessors to be able to do queries */
        actualAccessor = getManager().createAccessor(ActualSalesAccessor.class);
        forecastAccessor = getManager().createAccessor(ForecastSalesAccessor.class);
    }

    @Override
    protected Map<Integer, KpiEntity> getActualData(Period tempPlanPeriodFrom, Period tempPlanPeriodTo) {
        /* Send query to the database with data source BW B */
        Result<ActualSalesEntity> queryResult = actualAccessor.getMultipleSalesKpis(productMainGroup, region,
                salesType.getType(), DataSource.BW_B.toString(), tempPlanPeriodFrom.getPeriod(), tempPlanPeriodTo.getPeriod());

        Map<Integer, KpiEntity> returnMap = new PeriodMap<>(tempPlanPeriodFrom, tempPlanPeriodTo);

        for (ActualSalesEntity entity : queryResult) {
            returnMap.put(entity.getPeriod(), entity);
        }

        /* For each entry in the map */
        for (Integer period : returnMap.keySet()) {
            /* IF entry is null THEN redo query with BW A instead of BW B */
            if (returnMap.get(period) == null) {
                returnMap.put(period, actualAccessor.getSalesKPIs(productMainGroup, period, region,
                        salesType.getType(), DataSource.BW_A.toString()));

                /* IF result is NOT empty THEN get cm1 value from forecast data and put it in the query result because BW A
                 * has no cm1 values
                 */
                if (returnMap.get(period) != null) {
                    /* Cast the object so we can access the CM1 Setter */
                    ActualSalesEntity cm1Entity = (ActualSalesEntity) returnMap.get(period);
                    /* The CM1 value is directly written in the query result */
                    cm1Entity.setCm1(getForecastCm1(new Period(period), returnMap.get(period).getCurrency()));
                }
            }
        }
        return returnMap;
    }

    @Override
    protected Map<Integer, KpiEntity> getForecastData(Period tempPlanPeriodFrom, Period tempPlanPeriodTo) {
        /* Request data from forecast sales */
        Result<ForecastSalesEntity> queryResult = forecastAccessor.getMultipleSalesKpis(productMainGroup, region,
                currentPeriod.getPeriod(), salesType.toString(), EntryType.FORECAST.getType(), tempPlanPeriodFrom.getPeriod(),
                tempPlanPeriodTo.getPeriod());

        Map<Integer, KpiEntity> returnMap = new PeriodMap<>(tempPlanPeriodFrom, tempPlanPeriodTo);

        for (ForecastSalesEntity entity : queryResult) {
            returnMap.put(entity.getPlanPeriod(), entity);
        }

        for (Integer period : returnMap.keySet()) {
            /* IF entity is null THEN retry query with currentPeriod - 1 */
            returnMap.putIfAbsent(period, forecastAccessor.getSalesKpis(productMainGroup, currentPeriod.getPreviousPeriod(),
                    period, region, salesType.toString(), EntryType.FORECAST.getType()));

        }

        return returnMap;
    }

    /**
     * Gets the cm1 value from the forecast sales table
     *
     * @param tempPlanPeriod period of the desired cm1 value
     * @param toCurrency     desired return currency
     *
     * @return cm1 value as double
     */
    private double getForecastCm1(Period tempPlanPeriod, String toCurrency) {
        /* Request data from forecast sales */
        SalesEntity cm1 = forecastAccessor.getCm1(productMainGroup, currentPeriod.getPeriod(),
                tempPlanPeriod.getPeriod(), region, salesType.toString());

        /* IF result is null THEN return 0
         * ELSE Use CM1 value from result
         */
        if (cm1 == null) {
            return 0;
        } else {
            /* IF currency from this query is equal to the one calling this method THEN return the value as it is
             * ELSE create a new exchange rate request and get the exchange rate for the desired period and currency
             */
            if (cm1.getCurrency().equals(toCurrency)) {
                return cm1.getCm1();
            } else {
                double exchangeRate = new ExchangeRateRequest(getConnection(), Currency.getCurrencyByAbbreviation(toCurrency))
                        .getExchangeRate(tempPlanPeriod, Currency.getCurrencyByAbbreviation(cm1.getCurrency()));
                return cm1.getCm1() * exchangeRate;
            }
        }
    }

    @Override
    protected SalesEntity getBudgetData(Period tempPlanPeriod) {
        return forecastAccessor.getSalesKpis(productMainGroup, tempPlanPeriod.getPeriod(), tempPlanPeriod.getPeriod(),
                region, salesType.toString(), EntryType.BUDGET.toString());
    }

    @Override
    protected ValidatedResult calculateBj(ZeroMonthPeriod zeroMonthPeriod) {
        SalesEntity queryResult = forecastAccessor.getSalesKpis(productMainGroup, currentPeriod.getPeriod(),
                zeroMonthPeriod.getPeriod(), region, salesType.toString(), EntryType.BUDGET.getType());

        return validateQueryResult(queryResult, new Period(zeroMonthPeriod));
    }

    @Override
    protected ValidatedResultTopdown calculateBjTopdown(ZeroMonthPeriod zeroMonthPeriod) {
        SalesEntity queryResult = forecastAccessor.getSalesKpis(productMainGroup, currentPeriod.getPeriod(),
                zeroMonthPeriod.getPeriod(), region, salesType.toString(), EntryType.BUDGET.getType());

        return validateTopdownQueryResult(queryResult, new Period(zeroMonthPeriod));
    }

    @Override
    protected ValidatedResultTopdown validateTopdownQueryResult(KpiEntity result, Period tempPlanPeriod) {
        /* Parse the query result to a SalesEntity Instance */
        SalesEntity queryResult = (SalesEntity) result;

        /* Prepare the ValidatedRequest object. One of the parameters is the validated result without topdown values */
        ValidatedResultTopdown validatedResult = new ValidatedResultTopdown(validateQueryResult(queryResult, tempPlanPeriod).getKpiResult());

        /* This map will contain the topdown values */
        Map<KeyPerformanceIndicators, Double> topdownMap = validatedResult.getTopdownResult();

        /* IF the result of the query is NOT empty THEN get the topdown values from the query result */
        if (queryResult != null) {
            /* IF the result is an instance of ForecastSalesEntity THEN get the topdown values (actual doesnt have topdown) */
            if (queryResult.getClass().isInstance(ForecastSalesEntity.class)) {
                ForecastSalesEntity fcEntity = (ForecastSalesEntity) queryResult;
                topdownMap.put(SALES_VOLUME, fcEntity.getTopdownAdjustSalesVolumes());
                topdownMap.put(NET_SALES, fcEntity.getTopdownAdjustNetSales());
                topdownMap.put(CM1, fcEntity.getTopdownAdjustCm1());

                /* IF the currency of the KPIs is not the desired one THEN get the exchange rate and convert them */
                if (queryResult.getCurrency().equals(exchangeRates.getToCurrency()) == false) {
                    double exchangeRate = exchangeRates.getExchangeRate(tempPlanPeriod, Currency.getCurrencyByAbbreviation(queryResult.getCurrency()));

                    for (KeyPerformanceIndicators kpi : kpiArray) {
                        topdownMap.put(kpi, topdownMap.get(kpi) * exchangeRate);
                    }
                }
            }
        }

        /* calculate the other KPIs such as price, varCost, etc. */
        calculateRemainingSalesKpis(topdownMap);

        return validatedResult;
    }

    @Override
    protected ValidatedResult validateQueryResult(KpiEntity result, Period tempPlanPeriod) {
        /* Parse the query result to a SalesEntity Instance */
        SalesEntity queryResult = (SalesEntity) result;

        /* Prepare the ValidatedRequest object */
        ValidatedResult validatedResult = new ValidatedResult(kpiArray);

        /* Prepare the map that will store the kpi values */
        Map<KeyPerformanceIndicators, Double> kpiMap = validatedResult.getKpiResult();

        /* IF the result of the query is NOT empty THEN get the values from the query result */
        if (queryResult != null) {
            kpiMap.put(SALES_VOLUME, queryResult.getSalesVolumes());
            kpiMap.put(NET_SALES, queryResult.getNetSales());
            kpiMap.put(CM1, queryResult.getCm1());

            /* IF the currency of the KPIs is not the desired one THEN get the exchange rate and convert them */
            if (queryResult.getCurrency().equals(exchangeRates.getToCurrency()) == false) {
                double exchangeRate = exchangeRates.getExchangeRate(tempPlanPeriod, Currency.getCurrencyByAbbreviation(queryResult.getCurrency()));

                for (KeyPerformanceIndicators kpi : kpiMap.keySet()) {
                    kpiMap.put(kpi, kpiMap.get(kpi) * exchangeRate);
                }
            }
        }

        /* calculate the other KPIs such as price, varCost, etc. */
        calculateRemainingSalesKpis(kpiMap);

        return validatedResult;
    }

    /**
     * Calculates the remaining KPIs PRICE, VAR_COST, CM1_SPECIFIC, CM1_PERCENT
     *
     * @param map that contains all sales KPIs
     */
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

    @Override
    protected OutputDataType createOutputDataType(KeyPerformanceIndicators kpi, EntryType entryType,
                                                  LinkedList<Double> monthlyValues, LinkedList<Double> bjValues) {
        return new OutputDataType(kpi, sbu, productMainGroup,
                region, region, salesType.toString(), entryType.toString(), exchangeRates.getToCurrency(), monthlyValues,
                bjValues);
    }

}
