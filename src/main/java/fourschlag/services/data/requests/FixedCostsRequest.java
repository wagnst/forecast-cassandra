package fourschlag.services.data.requests;

import fourschlag.entities.accessors.ActualFixedCostsAccessor;
import fourschlag.entities.accessors.ForecastFixedCostsAccessor;
import fourschlag.entities.tables.Entity;
import fourschlag.entities.tables.FixedCostsEntity;
import fourschlag.entities.types.*;
import fourschlag.services.db.CassandraConnection;

import java.util.*;
import java.util.stream.Collectors;

import static fourschlag.entities.types.KeyPerformanceIndicators.*;

public class FixedCostsRequest extends KpiRequest {
    private final String subregion;
    private final ActualFixedCostsAccessor actualAccessor;
    private final ForecastFixedCostsAccessor forecastAccessor;

    private boolean actualFlag = false;
    private boolean forecastFlag = false;

    public FixedCostsRequest(CassandraConnection connection, String sbu, int planYear, Period currentPeriod,
                             String subregion, ExchangeRateRequest exchangeRates,
                             OrgStructureAndRegionRequest orgAndRegionRequest) {
        super(connection, sbu, planYear, currentPeriod, exchangeRates);
        this.subregion = subregion;

        /* TODO: get Region with orgAndRegionRequest */

        actualAccessor = getManager().createAccessor(ActualFixedCostsAccessor.class);
        forecastAccessor = getManager().createAccessor(ForecastFixedCostsAccessor.class);
    }

    /* TODO: Prepare filtered enum */

    @Override
    protected void fillMap(Map<KeyPerformanceIndicators, LinkedList<Double>> map) {
        Arrays.stream(KeyPerformanceIndicators.values())
                .filter(kpi -> kpi.getFcType().equals("fixed costs"))
                .forEach(kpi -> map.put(kpi, new LinkedList<>()));
    }

    @Override
    protected List<OutputDataType> prepareResultList(EntryType valueUsedInOutputDataType) {
        return Arrays.stream(KeyPerformanceIndicators.values())
                .filter(kpi -> kpi.getFcType().equals("fixed costs"))
                .map(kpi -> createOutputDataType(kpi, valueUsedInOutputDataType, monthlyKpiValues.get(kpi), bjValues.get(kpi)))
                .collect(Collectors.toList());
    }

    @Override
    protected void putValuesIntoMonthlyMap(Map<KeyPerformanceIndicators, Double> map) {
        /* Add all KPI values to the monthly KPI value map */
        Arrays.stream(KeyPerformanceIndicators.values())
                .filter(kpi -> kpi.getFcType().equals("fixed costs"))
                .forEach(kpi -> monthlyKpiValues.get(kpi).add(map.get(kpi)));
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
    protected void calculateBj(Period zeroMonthPeriod) {
        FixedCostsEntity queryResult = forecastAccessor.getFixedCostsKpis(sbu, subregion, currentPeriod.getPeriod(),
                zeroMonthPeriod.getPeriod(), EntryType.BUDGET.toString());

        Map<KeyPerformanceIndicators, Double> map = validateQueryResult(queryResult, new Period(zeroMonthPeriod));

        Arrays.stream(KeyPerformanceIndicators.values())
                .filter(kpi -> kpi.getFcType().equals("fixed costs"))
                .forEach(kpi -> bjValues.get(kpi).add(map.get(kpi)));
    }

    @Override
    protected Map<KeyPerformanceIndicators, Double> validateQueryResult(Entity result, Period tempPlanPeriod) {
        FixedCostsEntity queryResult = (FixedCostsEntity) result;
        /* Prepare the kpi variables */
        double fixPreManCost;
        double shipCost;
        double sellCost;
        double diffActPreManCost;
        double idleEquipCost;
        double rdCost;
        double adminCostBu;
        double adminCostOd;
        double adminCostCompany;
        double otherOpCostBu;
        double otherOpCostOd;
        double otherOpCostCompany;
        double specItems;
        double provisions;
        double currencyGains;
        double valAdjustInventories;
        double otherFixCost;
        double depreciation;
        double capCost;
        double equityIncome;

        /* IF the result of the query is empty THEN set these KPIs to 0
         * ELSE get the values from the query result
         */
        if (queryResult == null) {
            fixPreManCost = 0;
            shipCost = 0;
            sellCost = 0;
            diffActPreManCost = 0;
            idleEquipCost = 0;
            rdCost = 0;
            adminCostBu = 0;
            adminCostOd = 0;
            adminCostCompany = 0;
            otherOpCostBu = 0;
            otherOpCostOd = 0;
            otherOpCostCompany = 0;
            specItems = 0;
            provisions = 0;
            currencyGains = 0;
            valAdjustInventories = 0;
            otherFixCost = 0;
            depreciation = 0;
            capCost = 0;
            equityIncome = 0;
        } else {
            fixPreManCost = queryResult.getFixPreManCost();
            shipCost = queryResult.getShipCost();
            sellCost = queryResult.getSellCost();
            diffActPreManCost = queryResult.getDiffActPreManCost();
            idleEquipCost = queryResult.getIdleEquipCost();
            rdCost = queryResult.getRdCost();
            adminCostBu = queryResult.getAdminCostBu();
            adminCostOd = queryResult.getAdminCostOd();
            adminCostCompany = queryResult.getAdminCostCompany();
            otherOpCostBu = queryResult.getOtherOpCostBu();
            otherOpCostOd = queryResult.getOtherOpCostOd();
            otherOpCostCompany = queryResult.getOtherOpCostCompany();
            specItems = queryResult.getSpecItems();
            provisions = queryResult.getProvisions();
            currencyGains = queryResult.getCurrencyGains();
            valAdjustInventories = queryResult.getValAdjustInventories();
            otherFixCost = queryResult.getOtherFixCost();
            depreciation = queryResult.getDepreciation();
            capCost = queryResult.getCapCost();
            equityIncome = queryResult.getEquityIncome();

            /* IF the currency of the KPIs is not the desired one THEN get the exchange rate and convert them */
            if (queryResult.getCurrency().equals(exchangeRates.getToCurrency()) == false) {
                double exchangeRate = exchangeRates.getExchangeRate(tempPlanPeriod, queryResult.getCurrency());
                /* TODO: Loop maybe? :D */
                fixPreManCost *= exchangeRate;
                shipCost *= exchangeRate;
                sellCost *= exchangeRate;
                diffActPreManCost *= exchangeRate;
                idleEquipCost *= exchangeRate;
                rdCost *= exchangeRate;
                adminCostBu *= exchangeRate;
                adminCostOd *= exchangeRate;
                adminCostCompany *= exchangeRate;
                otherOpCostBu *= exchangeRate;
                otherOpCostOd *= exchangeRate;
                otherOpCostCompany *= exchangeRate;
                specItems *= exchangeRate;
                provisions *= exchangeRate;
                currencyGains *= exchangeRate;
                valAdjustInventories *= exchangeRate;
                otherFixCost *= exchangeRate;
                depreciation *= exchangeRate;
                capCost *= exchangeRate;
                equityIncome *= exchangeRate;
            }
        }

        Map<KeyPerformanceIndicators, Double> resultMap = new HashMap<>();
        resultMap.put(FIX_PRE_MAN_COST, fixPreManCost);
        resultMap.put(SHIP_COST, shipCost);
        resultMap.put(SELL_COST, sellCost);
        resultMap.put(DIFF_ACT_PRE_MAN_COST, diffActPreManCost);
        resultMap.put(IDLE_EQUIP_COST, idleEquipCost);
        resultMap.put(RD_COST, rdCost);
        resultMap.put(ADMIN_COST_BU, adminCostBu);
        /*TODO: Finish... */

        return resultMap;
    }

    @Override
    protected OutputDataType createOutputDataType(KeyPerformanceIndicators kpi, EntryType entryType,
                                                  LinkedList<Double> monthlyValues, LinkedList<Double> bjValues) {
        /* TODO: use region instead of subregion.. */
        /* TODO: why do we need the sales type in fixed costs */
        return new OutputDataType(kpi, sbu, sbu, subregion, subregion, SalesType.THIRD_PARTY.toString(),
                entryType.toString(), exchangeRates.getToCurrency(), monthlyValues, bjValues);
    }
}
