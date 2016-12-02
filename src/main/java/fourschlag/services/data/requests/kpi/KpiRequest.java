package fourschlag.services.data.requests.kpi;

import fourschlag.entities.tables.kpi.KpiEntity;
import fourschlag.entities.types.*;
import fourschlag.services.data.requests.ExchangeRateRequest;
import fourschlag.services.data.requests.Request;
import fourschlag.services.db.CassandraConnection;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Extends Request. Contains HashMap that is used by all children of KpiRequest
 * to store the KPIs.
 */
public abstract class KpiRequest extends Request {

    private final Map<KeyPerformanceIndicators, LinkedList<Double>> monthlyKpiValues = new HashMap<>();
    private final Map<KeyPerformanceIndicators, LinkedList<Double>> bjValues = new HashMap<>();
    protected String sbu;
    protected String region;
    protected Period currentPeriod;
    protected ExchangeRateRequest exchangeRates;
    protected KeyPerformanceIndicators[] kpiArray;
    private Period planPeriod;

    /**
     * Default constructor to only open a database connection
     *
     * @param connection Cassandra connection that is supposed to be used
     */
    public KpiRequest(CassandraConnection connection) {
        super(connection);
    }

    /**
     * Constructor for KpiRequest
     *
     * @param connection Cassandra connection that is supposed to be used
     */
    public KpiRequest(CassandraConnection connection, String sbu, String region, Period planPeriod, Period currentPeriod,
                      ExchangeRateRequest exchangeRates, String fcType) {
        super(connection);
        this.sbu = sbu;
        this.region = region;
        this.planPeriod = planPeriod;
        this.currentPeriod = currentPeriod;
        this.exchangeRates = exchangeRates;

        this.kpiArray = filterKpiArray(fcType);

        fillMap(monthlyKpiValues);
        fillMap(bjValues);
    }

    /**
     * @param fcType
     *
     * @return
     */
    private KeyPerformanceIndicators[] filterKpiArray(String fcType) {
        return Arrays.stream(KeyPerformanceIndicators.values())
                .filter(kpi -> kpi.getFcType().equals(fcType))
                .toArray(KeyPerformanceIndicators[]::new);
    }

    /**
     * @param map
     */
    private void fillMap(Map<KeyPerformanceIndicators, LinkedList<Double>> map) {
        Arrays.stream(kpiArray)
                .forEach(kpi -> map.put(kpi, new LinkedList<>()));
    }

    /**
     * Initiates the calculation for all KPIs with the attributes of this
     * instance
     *
     * @return Stream of OutputDataTypes
     */
    public Stream<OutputDataType> calculateKpis() {
        /* Calculate Actual/Forecast KPIs */
        Stream<OutputDataType> entryTypeNull = calculateKpisWithTopdown(EntryType.ACTUAL_FORECAST);
        /* Calculate Budget KPIs */
        Stream<OutputDataType> entryTypeBudget = calculateKpisWithoutTopdown(EntryType.BUDGET);

        /* Concat and return both streams */
        return Stream.concat(entryTypeNull, entryTypeBudget);
    }

    /**
     * Calculates KPIs excluding topdown values
     *
     * @return Stream of OutputDataTypes
     */
    private Stream<OutputDataType> calculateKpisWithoutTopdown(final EntryType entryType) {
        /* Prepare result stream that will be returned later */
        Stream<OutputDataType> resultStream;

        Period tempPlanPeriod = new Period(planPeriod);

        /* Prepare maps that will store the monthly values */
        final Map<KeyPerformanceIndicators, LinkedList<Double>> tempMonthlyKpiValues = new HashMap<>(monthlyKpiValues);
        final Map<KeyPerformanceIndicators, LinkedList<Double>> tempBjValues = new HashMap<>(bjValues);

        /*Prepare ValidatedResult object */
        ValidatedResult kpisForSpecificMonth;
        /* calculateKpisForSpecificMonth() is called multiple times. After each time we increment the plan period */
        for (int i = 0; i < OutputDataType.getNumberOfMonths(); i++) {
            kpisForSpecificMonth = calculateKpisForSpecificMonths(tempPlanPeriod, entryType);
            /* Add all KPI values to the monthly KPI value map */

            /* The values from the validated result are written into their corresponding location in the monthly map */
            for (KeyPerformanceIndicators kpi : kpiArray) {
                tempMonthlyKpiValues.get(kpi).add(kpisForSpecificMonth.getKpiResult().get(kpi));
            }

            /* increment the plan period */
            tempPlanPeriod.increment();
        }

        ZeroMonthPeriod bjPeriod = new ZeroMonthPeriod(tempPlanPeriod);
        ValidatedResult bjValuesForSpecificMonth;
        for (int i = 0; i < OutputDataType.getNumberOfBj(); i++) {
            bjValuesForSpecificMonth = calculateBj(bjPeriod);
            for (KeyPerformanceIndicators kpi : kpiArray) {
                tempBjValues.get(kpi).add(bjValuesForSpecificMonth.getKpiResult().get(kpi));
            }
            /* Jump to the next zeroMonthPeriod */
            bjPeriod.increment();
        }

        /* All the values are put together in OutputDataType objects and are added to the result list */
        resultStream = Arrays.stream(kpiArray)
                .map(kpi -> createOutputDataType(kpi, entryType, tempMonthlyKpiValues.get(kpi), tempBjValues.get(kpi)));

        return resultStream;
    }

    /**
     * @param entryType
     *
     * @return
     */
    private Stream<OutputDataType> calculateKpisWithTopdown(final EntryType entryType) {
        /* Prepare result list that will be returned later */
        Stream<OutputDataType> resultStream;
        Stream<OutputDataType> resultStream2;

        Period tempPlanPeriod = new Period(planPeriod);

        final Map<KeyPerformanceIndicators, LinkedList<Double>> tempMonthlyKpiValues = new HashMap<>(monthlyKpiValues);
        final Map<KeyPerformanceIndicators, LinkedList<Double>> tempMonthlyTopdownValues = new HashMap<>(monthlyKpiValues);
        final Map<KeyPerformanceIndicators, LinkedList<Double>> tempBjValues = new HashMap<>(bjValues);
        final Map<KeyPerformanceIndicators, LinkedList<Double>> tempTopdownBjValues = new HashMap<>(bjValues);

        ValidatedResultTopdown kpisForSpecificMonth;
        /* calculateSalesKpisForSpecificMonth() is called multiple times. After each time we increment the plan period */
        for (int i = 0; i < OutputDataType.getNumberOfMonths(); i++) {
            kpisForSpecificMonth = (ValidatedResultTopdown) calculateKpisForSpecificMonths(tempPlanPeriod, entryType);

            /* Add all KPI values to the monthly KPI value map */
            for (KeyPerformanceIndicators kpi : kpiArray) {
                tempMonthlyKpiValues.get(kpi).add(kpisForSpecificMonth.getKpiResult().get(kpi));
                tempMonthlyTopdownValues.get(kpi).add(kpisForSpecificMonth.getTopdownResult().get(kpi));
            }

            tempPlanPeriod.increment();
        }

        ZeroMonthPeriod bjPeriod = new ZeroMonthPeriod(tempPlanPeriod);
        ValidatedResultTopdown bjValuesForSpecificMonth;
        for (int i = 0; i < OutputDataType.getNumberOfBj(); i++) {
            bjValuesForSpecificMonth = calculateBjTopdown(bjPeriod);

            for (KeyPerformanceIndicators kpi : kpiArray) {
                tempBjValues.get(kpi).add(bjValuesForSpecificMonth.getKpiResult().get(kpi));
                tempTopdownBjValues.get(kpi).add(bjValuesForSpecificMonth.getTopdownResult().get(kpi));
            }
            /* Jump to the next zeroMonthPeriod */
            bjPeriod.increment();
        }

        /* All the values are put together in OutputDataType objects and are added to the result list */
        resultStream = Arrays.stream(kpiArray)
                .map(kpi -> createOutputDataType(kpi, entryType, tempMonthlyKpiValues.get(kpi), tempBjValues.get(kpi)));

        resultStream2 = Arrays.stream(kpiArray)
                .map(kpi -> createOutputDataType(kpi, EntryType.TOPDOWN, tempMonthlyTopdownValues.get(kpi), tempTopdownBjValues.get(kpi)));

        return Stream.concat(resultStream, resultStream2);
    }

    /**
     * method that calculates the KPIs for specific months
     *
     * @param tempPlanPeriod the desired Period
     * @param entryType      the EntryType of the
     *
     * @return
     */
    private ValidatedResult calculateKpisForSpecificMonths(Period tempPlanPeriod, EntryType entryType) {
        KpiEntity queryResult;

        if (entryType == EntryType.BUDGET) {
            queryResult = getBudgetData(tempPlanPeriod);
            return validateQueryResult(queryResult, tempPlanPeriod);
        } else {
            /* IF plan period is in the past compared to current period THEN get data from the actual sales table
             * ELSE get data from the forecast table
             */
            if (tempPlanPeriod.getPeriod() < currentPeriod.getPreviousPeriod()) {
                queryResult = getActualData(tempPlanPeriod);
            } else if (tempPlanPeriod.getPeriod() == currentPeriod.getPreviousPeriod()) {
                queryResult = getActualData(tempPlanPeriod);
                if (queryResult == null) {
                    queryResult = getForecastData(tempPlanPeriod, EntryType.FORECAST);
                }
            } else {
                queryResult = getForecastData(tempPlanPeriod, EntryType.FORECAST);
            }
        }

        return validateTopdownQueryResult(queryResult, tempPlanPeriod);
    }

    /**
     * method to validate a query result including the topdown values
     *
     * @param result         The query result that will be validated
     * @param tempPlanPeriod planPeriod of that query result
     *
     * @return ValidatedResult with all the values for the sales KPIs
     */
    protected abstract ValidatedResultTopdown validateTopdownQueryResult(KpiEntity result, Period tempPlanPeriod);

    /**
     * method to validate a query result excluding the topdown values
     *
     * @param result         The query result that will be validated
     * @param tempPlanPeriod planPeriod of that query result
     *
     * @return ValidatedResult with all the values for the sales KPIs
     */
    protected abstract ValidatedResult validateQueryResult(KpiEntity result, Period tempPlanPeriod);

    /**
     * method to get the actual data
     *
     * @param tempPlanPeriod planPeriod the actual data is supposed to be taken
     *                       from
     *
     * @return the actual data within the desired period.
     */
    protected abstract KpiEntity getActualData(Period tempPlanPeriod);

    /**
     * method to get the forecast data
     *
     * @param tempPlanPeriod planPeriod the forecast data is supposed to be
     *                       taken from
     * @param entryType      the type of the data
     *
     * @return the forecast data within the desired period
     */
    protected abstract KpiEntity getForecastData(Period tempPlanPeriod, EntryType entryType);

    /**
     * method to get the budget data
     *
     * @param tempPlanPeriod planPeriod the budget data is supposed to be taken
     *                       from
     *
     * @return the budget data from the desired period
     */
    protected abstract KpiEntity getBudgetData(Period tempPlanPeriod);

    /**
     * Method that calculates the BJ values for all KPIs but for one specific
     * period (--> zero month period)
     *
     * @param zeroMonthPeriod ZeroMonthPeriod of the desired budget year
     */
    protected abstract ValidatedResult calculateBj(ZeroMonthPeriod zeroMonthPeriod);

    /**
     * Method that calculates the BJ values for all KPIs but for one specific
     * period (--> zero month period) including the topdown values
     *
     * @param zeroMonthPeriod ZeroMonthPeriod of the desired budget year
     */
    protected abstract ValidatedResultTopdown calculateBjTopdown(ZeroMonthPeriod zeroMonthPeriod);

    /**
     * Creates a OutputDataType Object with all given attributes
     *
     * @param kpi           KPI that will be set in the OutputDataType
     * @param entryType     Entry Type of that KPI entry
     * @param monthlyValues All the monthly kpi values
     * @param bjValues      The budget year values
     *
     * @return Instance of OutputDataType
     */
    protected abstract OutputDataType createOutputDataType(KeyPerformanceIndicators kpi, EntryType entryType,
                                                           LinkedList<Double> monthlyValues, LinkedList<Double> bjValues);

}
