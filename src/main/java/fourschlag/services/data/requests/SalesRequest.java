package fourschlag.services.data.requests;

import fourschlag.entities.accessors.ActualSalesAccessor;
import fourschlag.entities.accessors.ForecastSalesAccessor;
import fourschlag.entities.tables.ForecastSalesEntity;
import fourschlag.entities.tables.SalesEntity;
import fourschlag.entities.types.*;
import fourschlag.services.data.Service;
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
    private final String sbu;
    private final Period planPeriod;
    private final Period currentPeriod;
    private final String region;
    private final SalesType salesType;
    private final ExchangeRateRequest exchangeRates;
    private final ActualSalesAccessor actualAccessor;
    private final ForecastSalesAccessor forecastAccessor;
    private boolean actualFlag = false;
    private boolean forecastFlag = false;
    private EntryType entryType;

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
    public SalesRequest(CassandraConnection connection, String productMainGroup, int planYear,
                        Period currentPeriod, String region, SalesType salesType, ExchangeRateRequest exchangeRates) {
        super(connection);
        this.productMainGroup = productMainGroup;
        this.planPeriod = Period.getPeriodByYear(planYear);
        this.currentPeriod = currentPeriod;
        this.region = region;
        this.salesType = salesType;
        this.exchangeRates = exchangeRates;

        /* TODO: Avoid creating new OrgStructureRequest every time */
        this.sbu = new OrgStructureRequest(connection).getSbu(productMainGroup);
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

    /**
     * Calculates sales KPIs for the attributes saved in this request
     *
     * @return List of OutputDataTypes that contain all KPIs for given
     * parameters
     */
    public List<OutputDataType> calculateSalesKpis() {
        /* Prepare result list that will be returned later */
        List<OutputDataType> resultList;
        Period tempPlanPeriod = new Period(planPeriod);

        /* calculateSalesKpisForSpecificMonth() is called multiple times. After each time we increment the plan period */
        for (int i = 0; i < Service.getNumberOfMonths(); i++) {
            calculateSalesKpisForSpecificMonth(tempPlanPeriod);
            tempPlanPeriod.increment();
        }

        int bjPeriod = tempPlanPeriod.getZeroMonthPeriod();
        for (int i = 0; i < 3; i++) {
            calculateBj(bjPeriod);
            /* Jump to the next zeroMonthPeriod */
            bjPeriod += 100;
        }

        setEntryType();

        /* All the values are put together in OutputDataType objects and are added to the result list */

        resultList = Arrays.stream(KeyPerformanceIndicators.values())
                .filter(kpi -> kpi.getFcType().equals("sales"))
                .map(kpi -> createOutputDataType(kpi, monthlyKpiValues.get(kpi), bjValues.get(kpi)))
                .collect(Collectors.toList());

        /* Reset the flags */
        actualFlag = false;
        forecastFlag = false;
        return resultList;
    }

    /**
     * Private method to get KPIs for exactly one period (current value of
     * planPeriod)
     */
    private void calculateSalesKpisForSpecificMonth(Period tempPlanPeriod) {
        SalesEntity queryResult;

        /* IF plan period is in the past compared to current period THEN get data from the actual sales table
         * ELSE get data from the forecast table
         */
        if (tempPlanPeriod.getPeriod() < currentPeriod.getPreviousPeriod()) {
            queryResult = getActualData(tempPlanPeriod);
        } else if (tempPlanPeriod.getPeriod() == currentPeriod.getPreviousPeriod()) {
            queryResult = getActualData(tempPlanPeriod);
            if (queryResult == null) {
                queryResult = getForecastData(tempPlanPeriod);
            }
        } else {
            queryResult = getForecastData(tempPlanPeriod);
        }

        Map<KeyPerformanceIndicators, Double> map = validateQueryResult(queryResult, tempPlanPeriod);

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
    private SalesEntity getActualData(Period tempPlanPeriod) {
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
    private SalesEntity getForecastData(Period tempPlanPeriod) {
        /* Set this flag to true, so the entry type can be set correctly later */
        forecastFlag = true;
        return forecastAccessor.getSalesKpis(productMainGroup, currentPeriod.getPeriod(),
                tempPlanPeriod.getPeriod(), region, salesType.toString());
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
        SalesEntity cm1 =  forecastAccessor.getCm1(productMainGroup, currentPeriod.getPeriod(),
                tempPlanPeriod.getPeriod(), region, salesType.toString());

        if(cm1 == null) {
            return 0;
        }
        return cm1.getCm1();
    }

    private void calculateBj(int zeroMonthPeriod) {
        ForecastSalesEntity queryResult = forecastAccessor.getBudgetYear(productMainGroup, currentPeriod.getPeriod(),
                zeroMonthPeriod, region, salesType.toString());

        Map<KeyPerformanceIndicators, Double> map = validateQueryResult(queryResult, new Period(zeroMonthPeriod));

        Arrays.stream(KeyPerformanceIndicators.values())
                .filter(kpi -> kpi.getFcType().equals("sales"))
                .forEach(kpi -> bjValues.get(kpi).add(map.get(kpi)));
    }

    private Map<KeyPerformanceIndicators, Double> validateQueryResult(SalesEntity queryResult, Period tempPlanPeriod) {
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
     *
     * @return OutputDataType object
     */
    private OutputDataType createOutputDataType(KeyPerformanceIndicators kpi, LinkedList<Double> monthlyValues,
                                                LinkedList<Double> bjValues) {
        return new OutputDataType(kpi, sbu, productMainGroup,
                region, region, salesType.toString(), entryType.toString(), exchangeRates.getToCurrency(), monthlyValues,
                bjValues);
    }

    /**
     * Sets the entry type of this request
     */
    private void setEntryType() {
        /* IF both flags are set true THEN set entry type to actual/forecast
         * ELSE IF only the forecast flag is set true THEN set entry type to forecast
         * ELSE set entry type to actual*/
        if (actualFlag && forecastFlag) {
            entryType = EntryType.ACTUAL_FORECAST;
        } else if (forecastFlag) {
            entryType = EntryType.FORECAST;
        } else {
            entryType = EntryType.ACTUAL;
        }
    }

}
