package fourschlag.services.data.requests.kpi;

import com.datastax.driver.mapping.Result;
import fourschlag.entities.accessors.fixedcosts.ActualFixedCostsAccessor;
import fourschlag.entities.accessors.fixedcosts.ForecastFixedCostsAccessor;
import fourschlag.entities.tables.kpi.KpiEntity;
import fourschlag.entities.tables.kpi.fixedcosts.ActualFixedCostsEntity;
import fourschlag.entities.tables.kpi.fixedcosts.FixedCostsEntity;
import fourschlag.entities.tables.kpi.fixedcosts.ForecastFixedCostsEntity;
import fourschlag.entities.types.*;
import fourschlag.entities.types.KeyPerformanceIndicators;
import fourschlag.services.data.requests.ExchangeRateRequest;
import fourschlag.services.data.requests.OrgStructureAndRegionRequest;
import fourschlag.services.db.CassandraConnection;

import java.util.LinkedList;
import java.util.Map;

import static fourschlag.entities.types.KeyPerformanceIndicators.*;

/**
 * Extends KpiRequest. Offers Functionality to request Fixed Costs KPIs for a
 * specific subregion, period and sbu
 */
public class FixedCostsKpiRequest extends KpiRequest {
    private static final String FC_TYPE = "fixed costs";
    private String subregion;
    private ActualFixedCostsAccessor actualAccessor;
    private ForecastFixedCostsAccessor forecastAccessor;

    /**
     * Constructor for FixedCostsKpiRequest with additional parameters
     *
     * @param connection          Cassandra connection that is supposed to be
     *                            used
     * @param sbu                 sbu to query for
     * @param planPeriod          planPeriod to begin with
     * @param currentPeriod       point of view when querying data
     * @param subregion           subregion to query for
     * @param exchangeRates       ExchangeRateRequest with the desired output
     *                            currency
     * @param orgAndRegionRequest OrgStructureAndRegionInstance
     */
    public FixedCostsKpiRequest(CassandraConnection connection, String sbu, Period planPeriod, Period currentPeriod,
                                String subregion, ExchangeRateRequest exchangeRates,
                                OrgStructureAndRegionRequest orgAndRegionRequest) {
        super(connection, sbu, orgAndRegionRequest.getRegionBySubregion(subregion), planPeriod, currentPeriod, exchangeRates, FC_TYPE);
        this.subregion = subregion;

        actualAccessor = getManager().createAccessor(ActualFixedCostsAccessor.class);
        forecastAccessor = getManager().createAccessor(ForecastFixedCostsAccessor.class);
    }

    @Override
    protected Map<Integer, KpiEntity> getActualData(Period tempPlanPeriodFrom, Period tempPlanPeriodTo) {
        /* Send query to the database */
        Result<ActualFixedCostsEntity> queryResult = actualAccessor.getMultipleFixedCostsKpis
                (sbu, subregion, tempPlanPeriodFrom.getPeriod(), tempPlanPeriodTo.getPeriod());

        /* Prepare Map */
        Map<Integer, KpiEntity> returnMap = new PeriodMap<>(tempPlanPeriodFrom, tempPlanPeriodTo);

        /* Fill the map with the query results */
        for (ActualFixedCostsEntity entity : queryResult) {
            returnMap.put(entity.getPeriod(), entity);
        }

        return returnMap;
    }

    @Override
    protected Map<Integer, KpiEntity> getForecastData(Period tempPlanPeriodFrom, Period tempPlanPeriodTo) {
       /* Send query to the database */
        Result<ForecastFixedCostsEntity> queryResult = forecastAccessor.getMultipleFixedCostsKpis
                (subregion, sbu, currentPeriod.getPeriod(), EntryType.FORECAST.getType(),
                        tempPlanPeriodFrom.getPeriod(), tempPlanPeriodTo.getPeriod());

        /* Prepare map */
        Map<Integer, KpiEntity> returnMap = new PeriodMap<>(tempPlanPeriodFrom, tempPlanPeriodTo);

        /* Fill map with query results */
        for (ForecastFixedCostsEntity entity : queryResult) {
            returnMap.put(entity.getPlanPeriod(), entity);
        }

        /* Check if the map has still null values. For each null value -> redo query with period - 1 */
        for (Integer period : returnMap.keySet()) {
            /* IF entity is null THEN retry query with currentPeriod - 1 */
            returnMap.putIfAbsent(period, forecastAccessor.getFixedCostsKpis(sbu, subregion, currentPeriod.getPreviousPeriod(),
                    tempPlanPeriodFrom.getPeriod(), EntryType.FORECAST.getType()));
        }

        return returnMap;
    }

    @Override
    protected FixedCostsEntity getBudgetData(Period tempPlanPeriod) {
        return forecastAccessor.getFixedCostsKpis(sbu, subregion, tempPlanPeriod.getPeriod(), tempPlanPeriod.getPeriod(),
                EntryType.BUDGET.toString());
    }

    @Override
    protected ValidatedResultTopdown calculateBjTopdown(ZeroMonthPeriod zeroMonthPeriod) {
        FixedCostsEntity queryResult = forecastAccessor.getFixedCostsKpis(sbu, subregion, currentPeriod.getPeriod(),
                zeroMonthPeriod.getPeriod(), EntryType.BUDGET.toString());

        /* return the validated result */
        return validateTopdownQueryResult(queryResult, new Period(zeroMonthPeriod));
    }

    @Override
    protected ValidatedResult calculateBj(ZeroMonthPeriod zeroMonthPeriod) {
        FixedCostsEntity queryResult = forecastAccessor.getFixedCostsKpis(sbu, subregion, currentPeriod.getPeriod(),
                zeroMonthPeriod.getPeriod(), EntryType.BUDGET.toString());

        /* return the validated result */
        return validateQueryResult(queryResult, new Period(zeroMonthPeriod));
    }

    @Override
    protected ValidatedResultTopdown validateTopdownQueryResult(KpiEntity result, Period tempPlanPeriod) {
        /* Cast the Entity to be able to access all needed fields */
        FixedCostsEntity queryResult = (FixedCostsEntity) result;

        /* Prepare the validated result object with topdown . One of the parameters is the method
         * validateQueryResult that returns a validated result without topdown
         */
        ValidatedResultTopdown validatedResult = new ValidatedResultTopdown(validateQueryResult(queryResult, tempPlanPeriod).getKpiResult());

        /* Prepare map for topdown values by getting it from the validatedResult Object */
        Map<KeyPerformanceIndicators, Double> topdownMap = validatedResult.getTopdownResult();

        /* IF the result is NOT empty THEN get its data */
        if (queryResult != null) {
            /* IF result is an instance of ForecastFixedCostsEntity THEN set topdown values */
            if (queryResult.getClass().isInstance(ForecastFixedCostsEntity.class)) {
                ForecastFixedCostsEntity fcEntity = (ForecastFixedCostsEntity) queryResult;
                double topdownFixCosts = fcEntity.getTopdownAdjustFixCosts();
                for (KeyPerformanceIndicators kpi : topdownMap.keySet()) {
                    topdownMap.put(kpi, topdownFixCosts);
                }
            }

            /* IF the currency of the KPIs is not the desired one THEN get the exchange rate and convert them */
            if (queryResult.getCurrency().equals(exchangeRates.getToCurrency()) == false) {
                double exchangeRate = exchangeRates.getExchangeRate(tempPlanPeriod, Currency.getCurrencyByAbbreviation(queryResult.getCurrency()));

                for (KeyPerformanceIndicators kpi : topdownMap.keySet()) {
                    topdownMap.put(kpi, topdownMap.get(kpi) * exchangeRate);
                }
            }
        }

        return validatedResult;
    }

    @Override
    protected ValidatedResult validateQueryResult(KpiEntity result, Period tempPlanPeriod) {
        /* Cast the Entity to be able to access all needed fields */
        FixedCostsEntity queryResult = (FixedCostsEntity) result;

        /* Prepare the validated result object */
        ValidatedResult validatedResult = new ValidatedResult(kpiArray);

        /* Prepare map for kpi result by getting it from the validated result object */
        Map<KeyPerformanceIndicators, Double> kpiMap = validatedResult.getKpiResult();

        /* IF the result of the query is NOT empty THEN get the values from the query result */
        if (queryResult != null) {
            kpiMap.put(FIX_PRE_MAN_COST, queryResult.getFixPreManCost());
            kpiMap.put(SHIP_COST, queryResult.getShipCost());
            kpiMap.put(SELL_COST, queryResult.getSellCost());
            kpiMap.put(DIFF_ACT_PRE_MAN_COST, queryResult.getDiffActPreManCost());
            kpiMap.put(IDLE_EQUIP_COST, queryResult.getIdleEquipCost());

            double fixCostBetweenCm1Cm2 = kpiMap.get(FIX_PRE_MAN_COST) + kpiMap.get(SHIP_COST) +
                    kpiMap.get(SELL_COST) + kpiMap.get(DIFF_ACT_PRE_MAN_COST) + kpiMap.get(IDLE_EQUIP_COST);

            kpiMap.put(FIX_COST_BETWEEN_CM1_CM2, fixCostBetweenCm1Cm2);
            kpiMap.put(RD_COST, queryResult.getRdCost());
            kpiMap.put(ADMIN_COST_BU, queryResult.getAdminCostBu());
            kpiMap.put(ADMIN_COST_OD, queryResult.getAdminCostOd());
            kpiMap.put(ADMIN_COST_COMPANY, queryResult.getAdminCostCompany());
            kpiMap.put(OTHER_OP_COST_BU, queryResult.getOtherOpCostBu());
            kpiMap.put(OTHER_OP_COST_OD, queryResult.getOtherOpCostOd());
            kpiMap.put(OTHER_OP_COST_COMPANY, queryResult.getOtherOpCostCompany());
            kpiMap.put(SPEC_ITEMS, queryResult.getSpecItems());
            kpiMap.put(PROVISIONS, queryResult.getProvisions());
            kpiMap.put(CURRENCY_GAINS, queryResult.getCurrencyGains());
            kpiMap.put(VAL_ADJUST_INVENTORIES, queryResult.getValAdjustInventories());
            kpiMap.put(OTHER_FIX_COST, queryResult.getOtherFixCost());

            double fixCostBelowCm2 = kpiMap.get(RD_COST) + kpiMap.get(ADMIN_COST_BU) +
                    kpiMap.get(ADMIN_COST_OD) + kpiMap.get(ADMIN_COST_COMPANY) + kpiMap.get(OTHER_OP_COST_BU) +
                    kpiMap.get(OTHER_OP_COST_OD) + kpiMap.get(OTHER_OP_COST_COMPANY) + kpiMap.get(SPEC_ITEMS) +
                    kpiMap.get(PROVISIONS) + kpiMap.get(CURRENCY_GAINS) + kpiMap.get(VAL_ADJUST_INVENTORIES) +
                    kpiMap.get(OTHER_FIX_COST);

            kpiMap.put(FIX_COST_BELOW_CM2, fixCostBelowCm2);
            kpiMap.put(TOTAL_FIX_COST, fixCostBetweenCm1Cm2 + fixCostBelowCm2);
            kpiMap.put(DEPRECIATION, queryResult.getDepreciation());
            kpiMap.put(CAP_COST, queryResult.getCapCost());
            kpiMap.put(EQUITY_INCOME, queryResult.getEquityIncome());

            /* IF the currency of the KPIs is not the desired one THEN get the exchange rate and convert them */
            if (queryResult.getCurrency().equals(exchangeRates.getToCurrency()) == false) {
                double exchangeRate = exchangeRates.getExchangeRate(tempPlanPeriod, Currency.getCurrencyByAbbreviation(queryResult.getCurrency()));

                for (KeyPerformanceIndicators kpi : kpiMap.keySet()) {
                    kpiMap.put(kpi, kpiMap.get(kpi) * exchangeRate);
                }
            }
        }

        return validatedResult;
    }

    @Override
    protected OutputDataType createOutputDataType(KeyPerformanceIndicators kpi, EntryType entryType,
                                                  LinkedList<Double> monthlyValues, LinkedList<Double> bjValues) {
        return new OutputDataType(kpi, sbu, sbu, region, subregion, SalesType.THIRD_PARTY.toString(),
                entryType.toString(), exchangeRates.getToCurrency(), monthlyValues, bjValues);
    }
}