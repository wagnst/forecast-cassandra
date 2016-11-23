package fourschlag.services.data.requests;

import fourschlag.entities.accessors.ActualSalesAccessor;
import fourschlag.entities.accessors.ForecastSalesAccessor;
import fourschlag.entities.tables.Entity;
import fourschlag.entities.tables.SalesEntity;
import fourschlag.entities.types.*;
import fourschlag.services.db.CassandraConnection;

import java.util.*;
import java.util.stream.Collectors;

import static fourschlag.entities.types.KeyPerformanceIndicators.*;

/**
 * Extends Request. Offers Functionality to request Sales KPIs for a specific
 * region, period and product main group
 */
public class SalesRequest extends KpiRequest {

    private final String productMainGroup;
    private final String region;
    private final SalesType salesType;
    private final ActualSalesAccessor actualAccessor;
    private final ForecastSalesAccessor forecastAccessor;

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
        super(connection, orgAndRegionRequest.getSbu(productMainGroup), planYear , currentPeriod, exchangeRates);
        this.productMainGroup = productMainGroup;
        this.region = region;
        this.salesType = salesType;

        /* Create needed accessors to be able to do queries */
        actualAccessor = getManager().createAccessor(ActualSalesAccessor.class);
        forecastAccessor = getManager().createAccessor(ForecastSalesAccessor.class);
    }

    @Override
    protected void fillMap(Map<KeyPerformanceIndicators, LinkedList<Double>> map) {
        Arrays.stream(KeyPerformanceIndicators.values())
                .filter(kpi -> kpi.getFcType().equals("sales"))
                .forEach(kpi -> map.put(kpi, new LinkedList<>()));
    }

    @Override
    protected List<OutputDataType> prepareResultList(EntryType valueUsedInOutputDataType) {
        return Arrays.stream(KeyPerformanceIndicators.values())
                .filter(kpi -> kpi.getFcType().equals("sales"))
                .map(kpi -> createOutputDataType(kpi, valueUsedInOutputDataType, monthlyKpiValues.get(kpi), bjValues.get(kpi)))
                .collect(Collectors.toList());
    }

    @Override
    protected void putValuesIntoMonthlyMap(Map<KeyPerformanceIndicators, Double> map) {
        /* Add all KPI values to the monthly KPI value map */
        Arrays.stream(KeyPerformanceIndicators.values())
                .filter(kpi -> kpi.getFcType().equals("sales"))
                .forEach(kpi -> monthlyKpiValues.get(kpi).add(map.get(kpi)));
    }

    /**
     * Queries KPIs from the actual sales table
     *
     * @return SalesEntity Object with query result
     */
    @Override
    protected SalesEntity getActualData(Period tempPlanPeriod) {
        /* Set this flag to true, so the entry type can be set correctly later */
        actualFlag = true;
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
        /* Set this flag to true, so the entry type can be set correctly later */
        forecastFlag = true;
        return forecastAccessor.getSalesKpis(productMainGroup, currentPeriod.getPeriod(),
                tempPlanPeriod.getPeriod(), region, salesType.toString(), entryType.toString());
    }

    /**
     * Gets the cm1 value from the forecast sales table
     *
     * @return double value of cm1
     */
    private double getForecastCm1(Period tempPlanPeriod) {
        /* Set this flag to true, so the entry type can be set correctly later */
        forecastFlag = true;
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
    protected void calculateBj(Period zeroMonthPeriod) {
        SalesEntity queryResult = forecastAccessor.getSalesKpis(productMainGroup, currentPeriod.getPeriod(),
                zeroMonthPeriod.getPeriod(), region, salesType.toString(), EntryType.BUDGET.getType());

        Map<KeyPerformanceIndicators, Double> map = validateQueryResult(queryResult, new Period(zeroMonthPeriod));

        Arrays.stream(KeyPerformanceIndicators.values())
                .filter(kpi -> kpi.getFcType().equals("sales"))
                .forEach(kpi -> bjValues.get(kpi).add(map.get(kpi)));
    }

    /**
     * Private method to validate a query result.
     *
     * @param result    The query result that will be validated
     * @param tempPlanPeriod planPeriod of that query result
     * @return Map with all the values for the sales KPIs
     */
    @Override
    protected Map<KeyPerformanceIndicators, Double> validateQueryResult(Entity result, Period tempPlanPeriod) {
        SalesEntity queryResult = (SalesEntity)result;
        /* Prepare the kpi variables */
        double salesVolume;
        double netSales;
        double cm1;
        double price;
        double varCost;
        double cm1Specific;
        double cm1Percent;

        /* IF the result of the query is empty THEN set these KPIs to 0
         * ELSE get the values from the query result
         */
        if (queryResult == null) {
            salesVolume = 0;
            netSales = 0;
            cm1 = 0;
        } else {
            salesVolume = queryResult.getSalesVolumes();
            netSales = queryResult.getNetSales();
            cm1 = queryResult.getCm1();

            /* IF the currency of the KPIs is not the desired one THEN get the exchange rate and convert them */
            if (queryResult.getCurrency().equals(exchangeRates.getToCurrency()) == false) {
                double exchangeRate = exchangeRates.getExchangeRate(tempPlanPeriod, queryResult.getCurrency());
                salesVolume *= exchangeRate;
                netSales *= exchangeRate;
                cm1 *= exchangeRate;
            }
        }

         /* IF sales volume is 0 THEN these other KPIs are 0 too*/
        if (salesVolume == 0) {
            price = 0;
            varCost = 0;
            cm1Specific = 0;
        } else {
            price = netSales / salesVolume * 1000;
            varCost = (netSales - cm1) * 1000 / salesVolume;
            cm1Specific = cm1 / salesVolume * 1000;
        }

        /* IF net sales is 0 THEN this other KPI is 0 too */
        if (netSales == 0) {
            cm1Percent = 0;
        } else {
            cm1Percent = cm1 / netSales;
        }

        Map<KeyPerformanceIndicators, Double> resultMap = new HashMap<>();
        resultMap.put(SALES_VOLUME, salesVolume);
        resultMap.put(NET_SALES, netSales);
        resultMap.put(CM1, cm1);
        resultMap.put(PRICE, price);
        resultMap.put(VAR_COSTS, varCost);
        resultMap.put(CM1_SPECIFIC, cm1Specific);
        resultMap.put(CM1_PERCENT, cm1Percent);

        return resultMap;
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
