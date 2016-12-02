package fourschlag.services.data.requests.kpi.manipulation;

import fourschlag.entities.accessors.fixedcosts.ForecastFixedCostsAccessor;
import fourschlag.services.data.requests.Request;
import fourschlag.services.db.CassandraConnection;

public class FixedCostsManipulationRequest extends Request {

    private final ForecastFixedCostsAccessor forecastAccessor;

    public FixedCostsManipulationRequest(CassandraConnection connection) {
        super(connection);
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
                // update and existing record
                forecastAccessor.updateForecastFixedCost(sbu, subregion, fixPreManCost, shipCost, sellCost, diffActPreManCost, idleEquipCost, rdCost, adminCostBu, adminCostOd, adminCostCompany, otherOpCostBu,
                        otherOpCostOd, otherOpCostCompany, specItems, provisions, currencyGains, valAdjustInventories, otherFixCost, deprecation, capCost, equitiyIncome, topdownAdjustFixCosts, planPeriod,
                        planYear, planHalfYear, planQuarter, planMonth, status, usercomment, entryType, period, region, periodYear, periodMonth, currency, userId, entryTs);
            } else {
                forecastAccessor.setForecastFixedCost(sbu, subregion, fixPreManCost, shipCost, sellCost, diffActPreManCost, idleEquipCost, rdCost, adminCostBu, adminCostOd, adminCostCompany, otherOpCostBu,
                        otherOpCostOd, otherOpCostCompany, specItems, provisions, currencyGains, valAdjustInventories, otherFixCost, deprecation, capCost, equitiyIncome, topdownAdjustFixCosts, planPeriod,
                        planYear, planHalfYear, planQuarter, planMonth, status, usercomment, entryType, period, region, periodYear, periodMonth, currency, userId, entryTs);
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
