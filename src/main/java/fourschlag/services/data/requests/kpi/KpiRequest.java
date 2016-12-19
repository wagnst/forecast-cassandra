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
    Period currentPeriod;
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
     * Filters the Enum KeyPerformanceIndicators.
     *
     * @param fcType filter param fc_type (example: sales)
     *
     * @return Array of KeyPerformanceIndicators
     */
    private KeyPerformanceIndicators[] filterKpiArray(String fcType) {
        return Arrays.stream(KeyPerformanceIndicators.values())
                .filter(kpi -> kpi.getFcType().equals(fcType))
                .toArray(KeyPerformanceIndicators[]::new);
    }

    /**
     * Fills a map with all KPIs needed and a LinkedList each
     *
     * @param map map to be filled
     */
    private void fillMap(Map<KeyPerformanceIndicators, LinkedList<Double>> map) {
        Arrays.stream(kpiArray)
                .forEach(kpi -> map.put(kpi, new LinkedList<>()));
    }

    /**
     * Initiates the calculation for all KPIs with the attributes of this
     * instance. Calculates actual/forecast and budget Kpis.
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
     * Calculates Budget KPIs without topdown
     *
     * @return Stream of OutputDataTypes
     */
    private Stream<OutputDataType> calculateBudgetKpis() {
        /* Prepare result stream that will be returned later */
        Stream<OutputDataType> resultStream;

        /* local copy of the plan_period */
        Period tempPlanPeriod = new Period(planPeriod);

        /* Prepare maps that will store the monthly values */
        final Map<KeyPerformanceIndicators, LinkedList<Double>> monthlyKpiValues = new HashMap<>(kpiArray.length);
        fillMap(monthlyKpiValues);
        final Map<KeyPerformanceIndicators, LinkedList<Double>> bjValues = new HashMap<>(kpiArray.length);
        fillMap(bjValues);

        /*Prepare ValidatedResult object */
        ValidatedResult kpisForSpecificMonth;
        /* calculateKpisForSpecificMonth() is called multiple times. After each time we increment the plan period */
        for (int i = 0; i < OutputDataType.getNumberOfMonths(); i++) {
            kpisForSpecificMonth = validateQueryResult(getBudgetData(tempPlanPeriod), tempPlanPeriod);

            /* The values from the validated result are written into their corresponding location in the monthly map */
            for (KeyPerformanceIndicators kpi : kpiArray) {
                monthlyKpiValues.get(kpi).add(kpisForSpecificMonth.getKpiResult().get(kpi));
            }

            /* increment the plan period */
            tempPlanPeriod.increment();
        }

        /* Create the zero month period --> 201606 is converted to 201600 */
        ZeroMonthPeriod bjPeriod = new ZeroMonthPeriod(tempPlanPeriod);
        ValidatedResult bjValuesForSpecificMonth;

        /* calculate the bj values for each bj period */
        for (int i = 0; i < OutputDataType.getNumberOfBj(); i++) {
            bjValuesForSpecificMonth = calculateBj(bjPeriod);

            /* put the values into the map */
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
     * Calculates actual/forecast KPIs with topdown
     *
     * @return Stream of OutputDataTypes
     */
    private Stream<OutputDataType> calculateActualForecastKpis() {
        /* Prepare result streams that will be returned later */
        Stream<OutputDataType> resultStream;
        Stream<OutputDataType> resultStreamTopdown;

        /* local copy of plan_period */
        Period tempPlanPeriod = new Period(this.planPeriod);

        /* Prepare maps to store all the kpi values */
        final Map<KeyPerformanceIndicators, LinkedList<Double>> monthlyKpiValues = new HashMap<>(kpiArray.length);
        fillMap(monthlyKpiValues);
        final Map<KeyPerformanceIndicators, LinkedList<Double>> monthlyTopdownValues = new HashMap<>(kpiArray.length);
        fillMap(monthlyTopdownValues);
        final Map<KeyPerformanceIndicators, LinkedList<Double>> bjValues = new HashMap<>(kpiArray.length);
        fillMap(bjValues);
        final Map<KeyPerformanceIndicators, LinkedList<Double>> topdownBjValues = new HashMap<>(kpiArray.length);
        fillMap(topdownBjValues);

        /* Get kpis from the actual table from planPeriod to currentPeriod */
        Map<Integer, KpiEntity> actualData = getActualData(tempPlanPeriod, currentPeriod);
        /* Prepare map for forecast data */
        Map<Integer, KpiEntity> forecastData;

        tempPlanPeriod.incrementMultipleTimes(OutputDataType.getNumberOfMonths());
        /* IF the actual table had NO data for the current period THEN query that period again with the forecast table */
        if (actualData.get(currentPeriod.getPeriod()) == null) {
            /* Remove that entry from the actual data map */
            actualData.remove(currentPeriod.getPeriod());
            forecastData = getForecastData(currentPeriod, tempPlanPeriod);
        } else {
            /* ELSE start with the period one after the current period */
            forecastData = getForecastData(currentPeriod.immutableIncrement(), tempPlanPeriod);
        }

        ValidatedResultTopdown validatedResultTopdown;
        /* For each one of the two result maps... */
        for (Map<Integer, KpiEntity> kpiEntityMap : Arrays.asList(actualData, forecastData)) {
            /* For each entry of the map... */
            for (Integer period : kpiEntityMap.keySet()) {
                /* Validate the query result */
                validatedResultTopdown = validateTopdownQueryResult(kpiEntityMap.get(period), new Period(period));
                /* For each kpi... */
                for (KeyPerformanceIndicators kpi : kpiArray) {
                    /* Add the values to the monthly lists */
                    monthlyKpiValues.get(kpi).add(validatedResultTopdown.getKpiResult().get(kpi));
                    monthlyTopdownValues.get(kpi).add(validatedResultTopdown.getTopdownResult().get(kpi));
                }
            }
        }

        /* Create the zero month period --> 201606 is converted to 201600 */
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
     *
     * @return ValidatedResult with all the kpi values
     */
    protected abstract ValidatedResultTopdown validateTopdownQueryResult(KpiEntity result, Period tempPlanPeriod);

    /**
     * method to validate a query result excluding the topdown values
     *
     * @param result         The query result that will be validated
     * @param tempPlanPeriod planPeriod of that query result
     *
     * @return ValidatedResult with all kpi values
     */
    protected abstract ValidatedResult validateQueryResult(KpiEntity result, Period tempPlanPeriod);

    /**
     * Queries entities from the actual table that are within a specific time
     * window.
     *
     * @param tempPlanPeriodFrom plan_period to begin with
     * @param tempPlanPeriodTo   plan_period to end with
     *
     * @return Map with mapped entities from the actual table
     */
    protected abstract Map<Integer, KpiEntity> getActualData(Period tempPlanPeriodFrom, Period tempPlanPeriodTo);

    /**
     * Queries entities from the forecast table table that are within a specific
     * time window.
     *
     * @param tempPlanPeriodFrom plan_period to begin with
     * @param tempPlanPeriodTo   plan_period to end with
     *
     * @return Map with mapped entities from the forecast table
     */
    protected abstract Map<Integer, KpiEntity> getForecastData(Period tempPlanPeriodFrom, Period tempPlanPeriodTo);

    /**
     * Queries the database for budget data -> period = plan_period and
     * entry_type = budget
     *
     * @param tempPlanPeriod plan_period of data
     *
     * @return Mapped entity with budget query results
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
