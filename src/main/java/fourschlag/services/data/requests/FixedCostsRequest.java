package fourschlag.services.data.requests;

import com.datastax.driver.mapping.Result;
import fourschlag.entities.accessors.fixedcosts.ActualFixedCostsAccessor;
import fourschlag.entities.accessors.fixedcosts.ForecastFixedCostsAccessor;
import fourschlag.entities.tables.kpi.fixedcosts.ActualFixedCostsEntity;
import fourschlag.entities.tables.kpi.fixedcosts.FixedCostsEntity;
import fourschlag.entities.tables.kpi.fixedcosts.ForecastFixedCostsEntity;
import fourschlag.services.db.CassandraConnection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FixedCostsRequest extends Request {

    private final ActualFixedCostsAccessor actualAccessor;
    private final ForecastFixedCostsAccessor forecastAccessor;
    private Map<String, Set<String>> sbuMap;

    public FixedCostsRequest(CassandraConnection connection) {
        super(connection);
        actualAccessor = getManager().createAccessor(ActualFixedCostsAccessor.class);
        forecastAccessor = getManager().createAccessor(ForecastFixedCostsAccessor.class);
    }

    public boolean setForecastFixedCosts(String sbu, String subregion, double fixPreManCost, double shipCost, double sellCost, double diffActPreManCost,
                                         double idleEquipCost, double rdCost, double adminCostBu, double adminCostOd, double adminCostCompany, double otherOpCostBu, double otherOpCostOd,
                                         double otherOpCostCompany, double specItems, double provisions, double currencyGains, double valAdjustInventories, double otherFixCost,
                                         double deprecation, double capCost, double equitiyIncome, double topdownAdjustFixCosts, int planPeriod, int planYear, int planHalfYear, int planQuarter,
                                         int planMonth, String status, String usercomment, String entryType, int period, String region, int periodYear, int periodMonth, String currency,
                                         String userId, String entryTs) {
        try {
            if (forecastAccessor.getForecastFixedCost(sbu, subregion, period, planPeriod, entryType) != null) {
                // update an existing record
                forecastAccessor.updateForecastFixedCost(sbu, subregion, fixPreManCost, shipCost, sellCost, diffActPreManCost, idleEquipCost, rdCost, adminCostBu, adminCostOd, adminCostCompany, otherOpCostBu,
                        otherOpCostOd, otherOpCostCompany, specItems, provisions, currencyGains, valAdjustInventories, otherFixCost, deprecation, capCost, equitiyIncome, topdownAdjustFixCosts, planPeriod,
                        planYear, planHalfYear, planQuarter, planMonth, status, usercomment, entryType, period, region, periodYear, periodMonth, currency, userId, entryTs);
            } else {
                forecastAccessor.setForecastFixedCost(sbu, subregion, fixPreManCost, shipCost, sellCost, diffActPreManCost, idleEquipCost, rdCost, adminCostBu, adminCostOd, adminCostCompany, otherOpCostBu,
                        otherOpCostOd, otherOpCostCompany, specItems, provisions, currencyGains, valAdjustInventories, otherFixCost, deprecation, capCost, equitiyIncome, topdownAdjustFixCosts, planPeriod,
                        planYear, planHalfYear, planQuarter, planMonth, status, usercomment, entryType, period, region, periodYear, periodMonth, currency, userId, entryTs);
            }
        } catch (Exception e) {
            //TODO: implement better exception to be catched
            return false;
        }

        return true;
    }

    //TODO: implement method for non-forecast related tables

    public Map<String, Set<String>> getSubregionsAndSbu() {
        if (sbuMap == null) {
            querySubregionsAndSbu();
        }
        return sbuMap;
    }

    private void querySubregionsAndSbu() {
        Result<ActualFixedCostsEntity> entitiesFromActual = actualAccessor.getDistinctSbuAndSubregions();
        Result<ForecastFixedCostsEntity> entitiesFromForecast = forecastAccessor.getDistinctSbuAndSubregions();
        sbuMap = new HashMap<>();

        for (ActualFixedCostsEntity entity : entitiesFromActual) {
            addToSbuMap(entity);
        }

        for (ForecastFixedCostsEntity entity : entitiesFromForecast) {
            addToSbuMap(entity);
        }
    }

    private void addToSbuMap(FixedCostsEntity entity) {
        if (sbuMap.containsKey(entity.getSbu())) {
            sbuMap.get(entity.getSbu()).add(entity.getSubregion());
        } else {
            sbuMap.put(entity.getSbu(), new HashSet<String>() {{
                add(entity.getSubregion());
            }});
        }
    }
}
