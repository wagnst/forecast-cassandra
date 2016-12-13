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
    protected String sbu;
    protected String region;
    protected Period currentPeriod;
    ExchangeRateRequest exchangeRates;
    KeyPerformanceIndicators[] kpiArray;
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
     * Constructor
     *
     * @param connection    Cassandra connection that is supposed to be used
     * @param sbu           SBU to filter for
     * @param region        Region to filter for
     * @param planPeriod    Indicates the time span for which the KPIs are
     *                      supposed to be queried
     * @param currentPeriod The point of view in time from which the data is
     *                      supposed to be looked at
     * @param exchangeRates ExchangeRateRequest with the desired output
     *                      currency
     * @param fcType        The type of KPI (example: "sales" or "fixed costs")
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
    }

    /**
     * @param fcType
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
        Stream<OutputDataType> entryTypeNull = calculateActualForecastKpis();
        /* Calculate Budget KPIs */
        Stream<OutputDataType> entryTypeBudget = calculateBudgetKpis();

        /* Concat and return both streams */
        return Stream.concat(entryTypeNull, entryTypeBudget);
    }

    /**
     * Calculates KPIs excluding topdown values
     *
     * @return Stream of OutputDataTypes
     */
    private Stream<OutputDataType> calculateBudgetKpis() {
        /* Prepare result stream that will be returned later */
        Stream<OutputDataType> resultStream;

        Period tempPlanPeriod = new Period(planPeriod);

        /* Prepare maps that will store the monthly values */
        final Map<KeyPerformanceIndicators, LinkedList<Double>> monthlyKpiValues = new HashMap<>();
        fillMap(monthlyKpiValues);
        final Map<KeyPerformanceIndicators, LinkedList<Double>> bjValues = new HashMap<>();
        fillMap(bjValues);

        /*Prepare ValidatedResult object */
        ValidatedResult kpisForSpecificMonth;
        /* calculateKpisForSpecificMonth() is called multiple times. After each time we increment the plan period */
        for (int i = 0; i < OutputDataType.getNumberOfMonths(); i++) {
            kpisForSpecificMonth = validateQueryResult(getBudgetData(tempPlanPeriod), tempPlanPeriod);
            /* Add all KPI values to the monthly KPI value map */

            /* The values from the validated result are written into their corresponding location in the monthly map */
            for (KeyPerformanceIndicators kpi : kpiArray) {
                monthlyKpiValues.get(kpi).add(kpisForSpecificMonth.getKpiResult().get(kpi));
            }

            /* increment the plan period */
            tempPlanPeriod.increment();
        }

        ZeroMonthPeriod bjPeriod = new ZeroMonthPeriod(tempPlanPeriod);
        ValidatedResult bjValuesForSpecificMonth;
        for (int i = 0; i < OutputDataType.getNumberOfBj(); i++) {
            bjValuesForSpecificMonth = calculateBj(bjPeriod);
            for (KeyPerformanceIndicators kpi : kpiArray) {
                bjValues.get(kpi).add(bjValuesForSpecificMonth.getKpiResult().get(kpi));
            }
            /* Jump to the next zeroMonthPeriod */
            bjPeriod.increment();
        }

        /* All the values are put together in OutputDataType objects and are added to the result stream */
        resultStream = Arrays.stream(kpiArray)
                .map(kpi -> createOutputDataType(kpi, EntryType.BUDGET, monthlyKpiValues.get(kpi), bjValues.get(kpi)));

        return resultStream;
    }

    /**
     * @return
     */
    private Stream<OutputDataType> calculateActualForecastKpis() {
        /* Prepare result list that will be returned later */
        Stream<OutputDataType> resultStream;
        Stream<OutputDataType> resultStreamTopdown;

        Period tempPlanPeriod = new Period(this.planPeriod);

        final Map<KeyPerformanceIndicators, LinkedList<Double>> monthlyKpiValues = new HashMap<>();
        fillMap(monthlyKpiValues);
        final Map<KeyPerformanceIndicators, LinkedList<Double>> monthlyTopdownValues = new HashMap<>();
        fillMap(monthlyTopdownValues);
        final Map<KeyPerformanceIndicators, LinkedList<Double>> bjValues = new HashMap<>();
        fillMap(bjValues);
        final Map<KeyPerformanceIndicators, LinkedList<Double>> topdownBjValues = new HashMap<>();
        fillMap(topdownBjValues);

        Map<Integer, KpiEntity> actualData = getActualData(tempPlanPeriod, currentPeriod);
        Map<Integer, KpiEntity> forecastData;

        tempPlanPeriod.incrementMultipleTimes(OutputDataType.getNumberOfMonths());
        if (actualData.get(currentPeriod.getPeriod()) == null) {
            actualData.remove(currentPeriod.getPeriod());
            forecastData = getForecastData(currentPeriod, tempPlanPeriod);
        } else {
            forecastData = getForecastData(currentPeriod.immutableIncrement(), tempPlanPeriod);
        }

        ValidatedResultTopdown validatedResultTopdown;
        for (Map<Integer, KpiEntity> kpiEntityMap : Arrays.asList(actualData, forecastData)) {
            for (Integer period : kpiEntityMap.keySet()) {
                validatedResultTopdown = validateTopdownQueryResult(kpiEntityMap.get(period), new Period(period));
                for (KeyPerformanceIndicators kpi : kpiArray) {
                    monthlyKpiValues.get(kpi).add(validatedResultTopdown.getKpiResult().get(kpi));
                    monthlyTopdownValues.get(kpi).add(validatedResultTopdown.getTopdownResult().get(kpi));
                }
            }
        }


        ZeroMonthPeriod bjPeriod = new ZeroMonthPeriod(tempPlanPeriod);
        ValidatedResultTopdown bjValuesForSpecificMonth;
        for (int i = 0; i < OutputDataType.getNumberOfBj(); i++) {
            bjValuesForSpecificMonth = calculateBjTopdown(bjPeriod);

            for (KeyPerformanceIndicators kpi : kpiArray) {
                bjValues.get(kpi).add(bjValuesForSpecificMonth.getKpiResult().get(kpi));
                topdownBjValues.get(kpi).add(bjValuesForSpecificMonth.getTopdownResult().get(kpi));
            }
            /* Jump to the next zeroMonthPeriod */
            bjPeriod.increment();
        }

        /* All the values are put together in OutputDataType objects and are added to the result stream */
        resultStream = Arrays.stream(kpiArray)
                .map(kpi -> createOutputDataType(kpi, EntryType.ACTUAL_FORECAST, monthlyKpiValues.get(kpi), bjValues.get(kpi)));

        resultStreamTopdown = Arrays.stream(kpiArray)
                .map(kpi -> createOutputDataType(kpi, EntryType.TOPDOWN, monthlyTopdownValues.get(kpi), topdownBjValues.get(kpi)));

        return Stream.concat(resultStream, resultStreamTopdown);
    }

    /**
     * method to validate a query result including the topdown values
     *
     * @param result         The query result that will be validated
     * @param tempPlanPeriod planPeriod of that query result
     * @return ValidatedResult with all the values for the sales KPIs
     */
    protected abstract ValidatedResultTopdown validateTopdownQueryResult(KpiEntity result, Period tempPlanPeriod);

    /**
     * method to validate a query result excluding the topdown values
     *
     * @param result         The query result that will be validated
     * @param tempPlanPeriod planPeriod of that query result
     * @return ValidatedResult with all the values for the sales KPIs
     */
    protected abstract ValidatedResult validateQueryResult(KpiEntity result, Period tempPlanPeriod);

    /**
     * method to get the actual data
     *
     * @param tempPlanPeriodFrom planPeriod the actual data is supposed to be taken
     *                           from
     * @return the actual data within the desired period.
     */
    protected abstract Map<Integer, KpiEntity> getActualData(Period tempPlanPeriodFrom, Period tempPlanPeriodTo);

    /**
     * method to get the forecast data
     *
     * @param tempPlanPeriodFrom planPeriod the forecast data is supposed to be
     *                           taken from
     * @return the forecast data within the desired period
     */
    protected abstract Map<Integer, KpiEntity> getForecastData(Period tempPlanPeriodFrom, Period tempPlanPeriodTo);

    /**
     * method to get the budget data
     *
     * @param tempPlanPeriod planPeriod the budget data is supposed to be taken
     *                       from
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
     * @return Instance of OutputDataType
     */
    protected abstract OutputDataType createOutputDataType(KeyPerformanceIndicators kpi, EntryType entryType,
                                                           LinkedList<Double> monthlyValues, LinkedList<Double> bjValues);

}
