package fourschlag.services.data.requests.kpi;

import fourschlag.entities.accessors.fixedcosts.ActualFixedCostsAccessor;
import fourschlag.entities.accessors.fixedcosts.ForecastFixedCostsAccessor;
import fourschlag.entities.tables.kpi.KpiEntity;
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
 * Extends KpiRequest. Offers Functionality to request Fixed Costs KPIs for a specific
 * region, period and product main group
 */
public class FixedCostsRequest extends KpiRequest {
    private final String subregion;
    private final ActualFixedCostsAccessor actualAccessor;
    private final ForecastFixedCostsAccessor forecastAccessor;

    private static final String FC_TYPE = "fixed costs";

    public FixedCostsRequest(CassandraConnection connection, String sbu, Period planPeriod, Period currentPeriod,
                             String subregion, ExchangeRateRequest exchangeRates,
                             OrgStructureAndRegionRequest orgAndRegionRequest) {
        super(connection, sbu, orgAndRegionRequest.getRegion(subregion), planPeriod, currentPeriod, exchangeRates, FC_TYPE);
        this.subregion = subregion;

        actualAccessor = getManager().createAccessor(ActualFixedCostsAccessor.class);
        forecastAccessor = getManager().createAccessor(ForecastFixedCostsAccessor.class);
    }

    /**
     * Queries KPIs from the actual fixed costs table
     *
     *
     * @return SalesEntity Object with query result
     */
    @Override
    protected FixedCostsEntity getActualData(Period tempPlanPeriod) {
        /* Send query to the database */
        return actualAccessor.getFixedCostsKpis(sbu, subregion, tempPlanPeriod.getPeriod());
    }

    /**
     * Queries KPIs from the forecast fixed costs table
     *
     * @param tempPlanPeriod    planPeriod the forecast data is supposed to be taken from
     *
     * @param entryType         the type of the data
     *
     * @return
     */
    @Override
    protected FixedCostsEntity getForecastData(Period tempPlanPeriod, EntryType entryType) {
        FixedCostsEntity queryResult = forecastAccessor.getFixedCostsKpis(sbu, subregion, currentPeriod.getPeriod(),
                tempPlanPeriod.getPeriod(), entryType.toString());
        if (queryResult == null) {
            queryResult = forecastAccessor.getFixedCostsKpis(sbu, subregion, currentPeriod.getPreviousPeriod(),
                    tempPlanPeriod.getPeriod(), entryType.toString());
        }
        return queryResult;
    }

    /**
     * Queries KPIs with the budgetdata
     *
     * @param tempPlanPeriod planPeriod ....
     *
     * @return
     */
    @Override
    protected FixedCostsEntity getBudgetData(Period tempPlanPeriod) {
        return forecastAccessor.getFixedCostsKpis(sbu, subregion, tempPlanPeriod.getPeriod(), tempPlanPeriod.getPeriod(),
                EntryType.BUDGET.toString());
    }

    /**
     * method that calculates the BJ values for all KPIs but one specific period (--> zero month period)
     *
     * @param zeroMonthPeriod ZeroMonthPeriod of the desired budget year
     */
    @Override
    protected ValidatedResultTopdown calculateBjTopdown(ZeroMonthPeriod zeroMonthPeriod) {
        FixedCostsEntity queryResult = forecastAccessor.getFixedCostsKpis(sbu, subregion, currentPeriod.getPeriod(),
                zeroMonthPeriod.getPeriod(), EntryType.BUDGET.toString());

        return validateTopdownQueryResult(queryResult, new Period(zeroMonthPeriod));
    }

    @Override
    protected ValidatedResult calculateBj(ZeroMonthPeriod zeroMonthPeriod) {
        FixedCostsEntity queryResult = forecastAccessor.getFixedCostsKpis(sbu, subregion, currentPeriod.getPeriod(),
                zeroMonthPeriod.getPeriod(), EntryType.BUDGET.toString());

        return validateQueryResult(queryResult, new Period(zeroMonthPeriod));
    }

    @Override
    protected ValidatedResultTopdown validateTopdownQueryResult(KpiEntity result, Period tempPlanPeriod) {
        FixedCostsEntity queryResult = (FixedCostsEntity) result;
        /* Prepare the kpi variables */
        ValidatedResultTopdown validatedResult = new ValidatedResultTopdown(validateQueryResult(queryResult, tempPlanPeriod).getKpiResult());

        Map<KeyPerformanceIndicators, Double> kpiMap = validatedResult.getKpiResult();
        Map<KeyPerformanceIndicators, Double> topdownMap = validatedResult.getTopdownResult();

        if(queryResult != null) {
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

                for (KeyPerformanceIndicators kpi : kpiMap.keySet()) {
                    topdownMap.put(kpi, topdownMap.get(kpi) * exchangeRate);
                }
            }
        }

        return validatedResult;
    }

    @Override
    protected ValidatedResult validateQueryResult(KpiEntity result, Period tempPlanPeriod) {
        FixedCostsEntity queryResult = (FixedCostsEntity) result;

        ValidatedResult validatedResult = new ValidatedResult(kpiArray);

        Map<KeyPerformanceIndicators, Double> kpiMap = validatedResult.getKpiResult();

        /* IF the result of the query is not empty THEN get the values from the query result */
        if (queryResult != null) {
            kpiMap.put(FIX_PRE_MAN_COST, queryResult.getFixPreManCost());
            kpiMap.put(SHIP_COST, queryResult.getShipCost());
            kpiMap.put(SELL_COST, queryResult.getSellCost());
            kpiMap.put(DIFF_ACT_PRE_MAN_COST, queryResult.getDiffActPreManCost());
            kpiMap.put(IDLE_EQUIP_COST, queryResult.getIdleEquipCost());

            /* TODO: Check alternative with saving each kpi to a double variable first, instead of getting them all from the map */
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
        /* TODO: why do we need the sales type in fixed costs */
        return new OutputDataType(kpi, sbu, sbu, region , subregion, SalesType.THIRD_PARTY.toString(),
                entryType.toString(), exchangeRates.getToCurrency(), monthlyValues, bjValues);
    }
}
