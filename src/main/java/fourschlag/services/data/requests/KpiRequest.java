package fourschlag.services.data.requests;

import fourschlag.entities.tables.Entity;
import fourschlag.entities.types.*;
import fourschlag.services.data.Service;
import fourschlag.services.db.CassandraConnection;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Extends Request. Contains HashMap that is used by all children of KpiRequest
 * to store the KPIs.
 */
public abstract class KpiRequest extends Request {

    protected final String sbu;
    protected final Period planPeriod;
    protected final Period currentPeriod;
    protected final ExchangeRateRequest exchangeRates;
    protected boolean actualFlag = false;
    protected boolean forecastFlag = false;

    protected Map<KeyPerformanceIndicators, LinkedList<Double>> monthlyKpiValues = new HashMap<>();
    protected Map<KeyPerformanceIndicators, LinkedList<Double>> bjValues = new HashMap<>();

    /**
     * Constructor for KpiRequest
     *
     * @param connection Cassandra connection that is supposed to be used
     */
    public KpiRequest(CassandraConnection connection, String sbu, int planYear, Period currentPeriod, ExchangeRateRequest exchangeRates) {
        super(connection);
        this.sbu = sbu;
        this.planPeriod = Period.getPeriodByYear(planYear);
        this.currentPeriod = currentPeriod;
        this.exchangeRates = exchangeRates;

        fillMap(monthlyKpiValues);
        fillMap(bjValues);
    }

    protected abstract void fillMap(Map<KeyPerformanceIndicators, LinkedList<Double>> map);

    public List<OutputDataType> calculateKpis() {
        List<OutputDataType> resultList = calculateKpis(null);
        resultList.addAll(calculateKpis(EntryType.BUDGET));
        return resultList;
    }

    /**
     * Calculates sales KPIs for the attributes saved in this request
     *
     * @return List of OutputDataTypes that contain all KPIs for given
     * parameters
     */
    protected List<OutputDataType> calculateKpis(EntryType entryType) {
        /* Prepare result list that will be returned later */
        List<OutputDataType> resultList;
        Period tempPlanPeriod = new Period(planPeriod);

        /* calculateSalesKpisForSpecificMonth() is called multiple times. After each time we increment the plan period */
        for (int i = 0; i < Service.getNumberOfMonths(); i++) {
            calculateKpisForSpecificMonths(tempPlanPeriod, entryType);
            tempPlanPeriod.increment();
        }

        Period bjPeriod = new ZeroMonthPeriod(tempPlanPeriod);
        for (int i = 0; i < Service.getNumberOfBj(); i++) {
            calculateBj(bjPeriod);
            /* Jump to the next zeroMonthPeriod */
            bjPeriod.increment();
        }

        final EntryType valueUsedInOutputDataType;
        if (entryType != EntryType.BUDGET) {
            valueUsedInOutputDataType = setEntryTypeWithFlags();
        } else {
            valueUsedInOutputDataType = EntryType.BUDGET;
        }

        /* All the values are put together in OutputDataType objects and are added to the result list */

        resultList = prepareResultList(valueUsedInOutputDataType);

        /* Reset the flags */
        actualFlag = false;
        forecastFlag = false;
        return resultList;
    }

    protected abstract List<OutputDataType> prepareResultList(EntryType valueUsedInOutputDataType);

    protected void calculateKpisForSpecificMonths(Period tempPlanPeriod, EntryType entryType) {
        Entity queryResult;

        if (entryType == EntryType.BUDGET) {
            queryResult = getBudgetData(tempPlanPeriod);
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

        putValuesIntoMonthlyMap(validateQueryResult(queryResult, tempPlanPeriod));
    }

    protected abstract void putValuesIntoMonthlyMap(Map<KeyPerformanceIndicators, Double> map);

    protected abstract Map<KeyPerformanceIndicators, Double> validateQueryResult(Entity queryResult, Period tempPlanPeriod);

    protected abstract Entity getActualData(Period tempPlanPeriod);

    protected abstract Entity getForecastData(Period tempPlanPeriod, EntryType entryType);

    protected abstract Entity getBudgetData(Period tempPlanPeriod);

    protected abstract void calculateBj(Period zeroMonthPeriod);

    protected abstract OutputDataType createOutputDataType(KeyPerformanceIndicators kpi, EntryType entryType,
                                                  LinkedList<Double> monthlyValues, LinkedList<Double> bjValues);

    /**
     * Sets the entry type of this request
     */
    protected EntryType setEntryTypeWithFlags() {
        /* IF both flags are set true THEN set entry type to actual/forecast
         * ELSE IF only the forecast flag is set true THEN set entry type to forecast
         * ELSE set entry type to actual*/
        if (actualFlag && forecastFlag) {
            return EntryType.ACTUAL_FORECAST;
        } else if (forecastFlag) {
            return EntryType.FORECAST;
        } else {
            return EntryType.ACTUAL;
        }
    }
}
