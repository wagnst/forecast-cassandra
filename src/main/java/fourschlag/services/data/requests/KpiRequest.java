package fourschlag.services.data.requests;

import fourschlag.entities.tables.Entity;
import fourschlag.entities.types.*;
import fourschlag.services.data.Service;
import fourschlag.services.db.CassandraConnection;

import java.util.*;
import java.util.stream.Stream;

/**
 * Extends Request. Contains HashMap that is used by all children of KpiRequest
 * to store the KPIs.
 */
public abstract class KpiRequest extends Request {

    protected final String sbu;
    protected final String region;
    private final Period planPeriod;
    protected final Period currentPeriod;
    protected final ExchangeRateRequest exchangeRates;

    protected final KeyPerformanceIndicators[] kpiArray;

    private final Map<KeyPerformanceIndicators, LinkedList<Double>> monthlyKpiValues = new HashMap<>();
    private final Map<KeyPerformanceIndicators, LinkedList<Double>> bjValues = new HashMap<>();

    /**
     * Constructor for KpiRequest
     *
     * @param connection Cassandra connection that is supposed to be used
     */
    public KpiRequest(CassandraConnection connection, String sbu, String region, int planYear, Period currentPeriod,
                      ExchangeRateRequest exchangeRates, String fcType) {
        super(connection);
        this.sbu = sbu;
        this.region = region;
        this.planPeriod = Period.getPeriodByYear(planYear);
        this.currentPeriod = currentPeriod;
        this.exchangeRates = exchangeRates;

        this.kpiArray = filterKpiArray(fcType);

        fillMap(monthlyKpiValues);
        fillMap(bjValues);
    }

    private KeyPerformanceIndicators[] filterKpiArray(String fcType) {
        return Arrays.stream(KeyPerformanceIndicators.values())
                .filter(kpi -> kpi.getFcType().equals(fcType))
                .toArray(KeyPerformanceIndicators[]::new);
    }

    private void fillMap(Map<KeyPerformanceIndicators, LinkedList<Double>> map) {
        Arrays.stream(kpiArray)
                .forEach(kpi -> map.put(kpi, new LinkedList<>()));
    }

    /* TODO: Refactor Entry-Types */
    public Stream<OutputDataType> calculateKpis() {
        Stream<OutputDataType> entryTypeNull = calculateKpisWithTopdown(EntryType.ACTUAL_FORECAST);
        Stream<OutputDataType> entryTypeBudget = calculateKpisWithoutTopdown(EntryType.BUDGET);
        return Stream.concat(entryTypeNull, entryTypeBudget);
    }

    /**
     * Calculates sales KPIs for the attributes saved in this request
     *
     * @return List of OutputDataTypes that contain all KPIs for given
     * parameters
     */
    private Stream<OutputDataType> calculateKpisWithoutTopdown(final EntryType entryType) {
        /* Prepare result list that will be returned later */
        Stream<OutputDataType> resultStream;

        Period tempPlanPeriod = new Period(planPeriod);

        final Map<KeyPerformanceIndicators, LinkedList<Double>> tempMonthlyKpiValues = new HashMap<>(monthlyKpiValues);
        final Map<KeyPerformanceIndicators, LinkedList<Double>> tempBjValues = new HashMap<>(bjValues);

        ValidatedResult kpisForSpecificMonth;
        /* calculateSalesKpisForSpecificMonth() is called multiple times. After each time we increment the plan period */
        for (int i = 0; i < Service.getNumberOfMonths(); i++) {
            kpisForSpecificMonth = calculateKpisForSpecificMonths(tempPlanPeriod, entryType);
            /* Add all KPI values to the monthly KPI value map */

            for (KeyPerformanceIndicators kpi : kpiArray) {
                tempMonthlyKpiValues.get(kpi).add(kpisForSpecificMonth.getKpiResult().get(kpi));
            }

            tempPlanPeriod.increment();
        }

        Period bjPeriod = new ZeroMonthPeriod(tempPlanPeriod);
        ValidatedResult bjValuesForSpecificMonth;
        for (int i = 0; i < Service.getNumberOfBj(); i++) {
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
        for (int i = 0; i < Service.getNumberOfMonths(); i++) {
            kpisForSpecificMonth = (ValidatedResultTopdown) calculateKpisForSpecificMonths(tempPlanPeriod, entryType);

            /* Add all KPI values to the monthly KPI value map */
            for (KeyPerformanceIndicators kpi : kpiArray) {
                tempMonthlyKpiValues.get(kpi).add(kpisForSpecificMonth.getKpiResult().get(kpi));
                tempMonthlyTopdownValues.get(kpi).add(kpisForSpecificMonth.getTopdownResult().get(kpi));
            }

            tempPlanPeriod.increment();
        }

        Period bjPeriod = new ZeroMonthPeriod(tempPlanPeriod);
        ValidatedResultTopdown bjValuesForSpecificMonth;
        for (int i = 0; i < Service.getNumberOfBj(); i++) {
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

    private ValidatedResult calculateKpisForSpecificMonths(Period tempPlanPeriod, EntryType entryType) {
        Entity queryResult;

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

    protected abstract ValidatedResultTopdown validateTopdownQueryResult(Entity queryResult, Period tempPlanPeriod);

    protected abstract ValidatedResult validateQueryResult(Entity result, Period tempPlanPeriod);

    protected abstract Entity getActualData(Period tempPlanPeriod);

    protected abstract Entity getForecastData(Period tempPlanPeriod, EntryType entryType);

    protected abstract Entity getBudgetData(Period tempPlanPeriod);

    protected abstract ValidatedResult calculateBj(Period zeroMonthPeriod);

    protected abstract ValidatedResultTopdown calculateBjTopdown(Period zeroMonthPeriod);

    protected abstract OutputDataType createOutputDataType(KeyPerformanceIndicators kpi, EntryType entryType,
                                                           LinkedList<Double> monthlyValues, LinkedList<Double> bjValues);

}
