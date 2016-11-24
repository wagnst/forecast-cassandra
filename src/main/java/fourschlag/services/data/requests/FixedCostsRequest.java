package fourschlag.services.data.requests;

import fourschlag.entities.accessors.ActualFixedCostsAccessor;
import fourschlag.entities.accessors.ForecastFixedCostsAccessor;
import fourschlag.entities.tables.Entity;
import fourschlag.entities.tables.FixedCostsEntity;
import fourschlag.entities.types.*;
import fourschlag.services.db.CassandraConnection;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static fourschlag.entities.types.KeyPerformanceIndicators.*;

public class FixedCostsRequest extends KpiRequest {
    private final String subregion;
    private final ActualFixedCostsAccessor actualAccessor;
    private final ForecastFixedCostsAccessor forecastAccessor;

    private static final String FC_TYPE = "fixed costs";

    public FixedCostsRequest(CassandraConnection connection, String sbu, int planYear, Period currentPeriod,
                             String subregion, ExchangeRateRequest exchangeRates,
                             OrgStructureAndRegionRequest orgAndRegionRequest) {
        super(connection, sbu, orgAndRegionRequest.getRegion(subregion), planYear, currentPeriod, exchangeRates, FC_TYPE);
        this.subregion = subregion;

        /* TODO: get Region with orgAndRegionRequest */

        actualAccessor = getManager().createAccessor(ActualFixedCostsAccessor.class);
        forecastAccessor = getManager().createAccessor(ForecastFixedCostsAccessor.class);
    }

    /**
     * Queries KPIs from the actual sales table
     *
     * @return SalesEntity Object with query result
     */
    @Override
    protected FixedCostsEntity getActualData(Period tempPlanPeriod) {
        /* Set this flag to true, so the entry type can be set correctly later */
        actualFlag = true;
        /* Send query to the database */
        return actualAccessor.getFixedCostsKpis(sbu, subregion, tempPlanPeriod.getPeriod());
    }

    @Override
    protected FixedCostsEntity getForecastData(Period tempPlanPeriod, EntryType entryType) {
        /* Set this flag to true, so the entry type can be set correctly later */
        forecastFlag = true;
        return forecastAccessor.getFixedCostsKpis(sbu, subregion, currentPeriod.getPeriod(), tempPlanPeriod.getPeriod(),
                entryType.toString());
    }

    @Override
    protected FixedCostsEntity getBudgetData(Period tempPlanPeriod) {
        return forecastAccessor.getFixedCostsKpis(sbu, subregion, tempPlanPeriod.getPeriod(), tempPlanPeriod.getPeriod(),
                EntryType.BUDGET.toString());
    }

    /**
     * Private method that calculates the BJ values for all KPIs but one specific period (--> zero month period)
     *
     * @param zeroMonthPeriod ZeroMonthPeriod of the desired budget year
     */
    @Override
    protected Map<KeyPerformanceIndicators, Double> calculateBj(Period zeroMonthPeriod) {
        FixedCostsEntity queryResult = forecastAccessor.getFixedCostsKpis(sbu, subregion, currentPeriod.getPeriod(),
                zeroMonthPeriod.getPeriod(), EntryType.BUDGET.toString());

        return validateQueryResult(queryResult, new Period(zeroMonthPeriod));
    }

    @Override
    protected Map<KeyPerformanceIndicators, Double> validateQueryResult(Entity result, Period tempPlanPeriod) {
        FixedCostsEntity queryResult = (FixedCostsEntity) result;
        /* Prepare the kpi variables */
        Map<KeyPerformanceIndicators, Double> resultMap = new HashMap<KeyPerformanceIndicators, Double>(){{
            Arrays.stream(kpiArray)
                    .forEach(kpi -> put(kpi, 0.0));
        }};

        /* IF the result of the query is not empty THEN get the values from the query result */
        if (queryResult != null) {
            resultMap.put(FIX_PRE_MAN_COST, queryResult.getFixPreManCost());
            resultMap.put(SHIP_COST, queryResult.getShipCost());
            resultMap.put(SELL_COST, queryResult.getSellCost());
            resultMap.put(DIFF_ACT_PRE_MAN_COST, queryResult.getDiffActPreManCost());
            resultMap.put(IDLE_EQUIP_COST, queryResult.getIdleEquipCost());

            /* TODO: Check alternative with saving each kpi to a double variable first, instead of getting them all from the map */
            double fixCostBetweenCm1Cm2 = resultMap.get(FIX_PRE_MAN_COST) + resultMap.get(SHIP_COST) +
                    resultMap.get(SELL_COST) + resultMap.get(DIFF_ACT_PRE_MAN_COST) + resultMap.get(IDLE_EQUIP_COST);

            resultMap.put(FIX_COST_BETWEEN_CM1_CM2, fixCostBetweenCm1Cm2);
            resultMap.put(RD_COST, queryResult.getRdCost());
            resultMap.put(ADMIN_COST_BU, queryResult.getAdminCostBu());
            resultMap.put(ADMIN_COST_OD, queryResult.getAdminCostOd());
            resultMap.put(ADMIN_COST_COMPANY, queryResult.getAdminCostCompany());
            resultMap.put(OTHER_OP_COST_BU, queryResult.getOtherOpCostBu());
            resultMap.put(OTHER_OP_COST_OD, queryResult.getOtherOpCostOd());
            resultMap.put(OTHER_OP_COST_COMPANY, queryResult.getOtherOpCostCompany());
            resultMap.put(SPEC_ITEMS, queryResult.getSpecItems());
            resultMap.put(PROVISIONS, queryResult.getProvisions());
            resultMap.put(CURRENCY_GAINS, queryResult.getCurrencyGains());
            resultMap.put(VAL_ADJUST_INVENTORIES, queryResult.getValAdjustInventories());
            resultMap.put(OTHER_FIX_COST, queryResult.getOtherFixCost());

            double fixCostBelowCm2 = resultMap.get(RD_COST) + resultMap.get(ADMIN_COST_BU) +
                    resultMap.get(ADMIN_COST_OD) + resultMap.get(ADMIN_COST_COMPANY) + resultMap.get(OTHER_OP_COST_BU) +
                    resultMap.get(OTHER_OP_COST_OD) + resultMap.get(OTHER_OP_COST_COMPANY) + resultMap.get(SPEC_ITEMS) +
                    resultMap.get(PROVISIONS) + resultMap.get(CURRENCY_GAINS) + resultMap.get(VAL_ADJUST_INVENTORIES) +
                    resultMap.get(OTHER_FIX_COST);

            resultMap.put(FIX_COST_BELOW_CM2, fixCostBelowCm2);
            resultMap.put(TOTAL_FIX_COST, fixCostBetweenCm1Cm2 + fixCostBelowCm2);
            resultMap.put(DEPRECATION, queryResult.getDepreciation());
            resultMap.put(CAP_COST, queryResult.getCapCost());
            resultMap.put(EQUITY_INCOME, queryResult.getEquityIncome());

            /* IF the currency of the KPIs is not the desired one THEN get the exchange rate and convert them */
            if (queryResult.getCurrency().equals(exchangeRates.getToCurrency()) == false) {
                double exchangeRate = exchangeRates.getExchangeRate(tempPlanPeriod, queryResult.getCurrency());

                for (KeyPerformanceIndicators kpi : resultMap.keySet()) {
                    resultMap.put(kpi, resultMap.get(kpi) * exchangeRate);
                }
            }
        }

        /* TODO: add topdown_adjust_fix_costs */

        return resultMap;
    }

    @Override
    protected OutputDataType createOutputDataType(KeyPerformanceIndicators kpi, EntryType entryType,
                                                  LinkedList<Double> monthlyValues, LinkedList<Double> bjValues) {
        /* TODO: why do we need the sales type in fixed costs */
        return new OutputDataType(kpi, sbu, sbu, region , subregion, SalesType.THIRD_PARTY.toString(),
                entryType.toString(), exchangeRates.getToCurrency(), monthlyValues, bjValues);
    }
}
