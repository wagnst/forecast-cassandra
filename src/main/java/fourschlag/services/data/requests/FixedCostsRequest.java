package fourschlag.services.data.requests;

import com.datastax.driver.mapping.Result;
import fourschlag.entities.accessors.fixedcosts.ActualFixedCostsAccessor;
import fourschlag.entities.accessors.fixedcosts.ForecastFixedCostsAccessor;
import fourschlag.entities.tables.kpi.fixedcosts.ActualFixedCostsEntity;
import fourschlag.entities.tables.kpi.fixedcosts.FixedCostsEntity;
import fourschlag.entities.tables.kpi.fixedcosts.ForecastFixedCostsEntity;
import fourschlag.entities.types.EntryType;
import fourschlag.entities.types.Period;
import fourschlag.services.db.CassandraConnection;

import java.util.*;

/**
 * Extends Request. Offers functionality to query the Fixed Costs tables.
 */
public class FixedCostsRequest extends Request {

    private final ActualFixedCostsAccessor actualAccessor;
    private final ForecastFixedCostsAccessor forecastAccessor;
    private Map<String, Set<String>> sbuMap;

    public FixedCostsRequest(CassandraConnection connection) {
        super(connection);
        actualAccessor = getManager().createAccessor(ActualFixedCostsAccessor.class);
        forecastAccessor = getManager().createAccessor(ForecastFixedCostsAccessor.class);
    }

    /**
     * Inserts (if combination of primary keys does not exist) or updates a row
     * in the forecast fixed costs table
     *
     * @return True IF process was successful; False IF it something went wrong
     */
    public boolean setForecastFixedCosts(String sbu, String subregion, double fixPreManCost, double shipCost, double sellCost, double diffActPreManCost,
                                         double idleEquipCost, double rdCost, double adminCostBu, double adminCostOd, double adminCostCompany, double otherOpCostBu, double otherOpCostOd,
                                         double otherOpCostCompany, double specItems, double provisions, double currencyGains, double valAdjustInventories, double otherFixCost,
                                         double depreciation, double capCost, double equitiyIncome, double topdownAdjustFixCosts, Period planPeriod,
                                         String status, String usercomment, String entryType, Period period, String region, String currency,
                                         String userId, String entryTs) {

        OrgStructureAndRegionRequest request = new OrgStructureAndRegionRequest(getConnection());

        /* Check if the given values for sbu, subregion and region are existent in the OrgStructure and Regions tables */
        if (!request.checkFixedCostsParams(sbu, subregion, region)) {
            /* IF invalid THEN return false */
            return false;
        }

        try {
            /* IF the forecast fixed costs table already has data for that combination of primary key values */
            if (forecastAccessor.getSpecificForecastFixedCosts(sbu, subregion, period.getPeriod(), planPeriod.getPeriod(), entryType) != null) {
                /* THEN update that existing record */
                forecastAccessor.updateForecastFixedCosts(sbu, subregion, fixPreManCost, shipCost, sellCost, diffActPreManCost, idleEquipCost, rdCost, adminCostBu, adminCostOd, adminCostCompany, otherOpCostBu,
                        otherOpCostOd, otherOpCostCompany, specItems, provisions, currencyGains, valAdjustInventories, otherFixCost, depreciation, capCost, equitiyIncome, topdownAdjustFixCosts, planPeriod.getPeriod(),
                        planPeriod.getYear(), planPeriod.getHalfYear(), planPeriod.getQuarter(), planPeriod.getMonth(), status, usercomment, entryType, period.getPeriod(), region, period.getYear(), period.getMonth(), currency, userId, entryTs);
            } else {
                /* ELSE insert a new row */
                forecastAccessor.setForecastFixedCost(sbu, subregion, fixPreManCost, shipCost, sellCost, diffActPreManCost, idleEquipCost, rdCost, adminCostBu, adminCostOd, adminCostCompany, otherOpCostBu,
                        otherOpCostOd, otherOpCostCompany, specItems, provisions, currencyGains, valAdjustInventories, otherFixCost, depreciation, capCost, equitiyIncome, topdownAdjustFixCosts, planPeriod.getPeriod(),
                        planPeriod.getYear(), planPeriod.getHalfYear(), planPeriod.getQuarter(), planPeriod.getMonth(), status, usercomment, entryType, period.getPeriod(), region, period.getYear(), period.getMonth(), currency, userId, entryTs);
            }
        } catch (Exception e) {
            /* IF something goes wrong THEN return false */
            //TODO: implement better exception to be catched
            return false;
        }

        return true;
    }

    /**
     * Gets all ForecastFixedCostsEntities with no filter applied
     *
     * @return all entities which are present inside forecast_fixed_costs
     */
    public List<ForecastFixedCostsEntity> getForecastFixedCosts() {
        return forecastAccessor.getAllForecastFixedCosts().all();
    }

    /**
     * Gets a specific ForecastFixedCostsEntity filtered by joined primary keys
     *
     * @return single entity of ForeCastFixedCostsEntity
     */
    public ForecastFixedCostsEntity getSpecificForecastFixedCosts(String sbu, String subregion, Period period, EntryType entryType, Period planPeriod) {
        return forecastAccessor.getSpecificForecastFixedCosts(sbu, subregion, period.getPeriod(), planPeriod.getPeriod(), entryType.getType());
    }

    /**
     * Gets specific ForecastFixedCostsEntites filtered by senseful drill down
     * parameters
     *
     * @return a list of entities which are present inside forecast_fixed_costs
     */
    public List<ForecastFixedCostsEntity> getMultipleForecastFixedCosts(String subregion, String sbu, Period period,
                                                                        EntryType entryType, Period planPeriodFrom,
                                                                        Period planPeriodTo) {

        return forecastAccessor.getMultipleForecastFixedCosts(subregion, sbu, period.getPeriod(),
                entryType.getType(), planPeriodFrom.getPeriod(), planPeriodTo.getPeriod()).all();
    }

    /**
     * Method can delete an entity from forecast_fixed_costs with its primary
     * key
     *
     * @return boolean if successfull or not
     */
    public boolean deleteForecastFixedCosts(String sbu, String subregion, Period period,
                                            String entryType, Period planPeriod) {
        try {
            /* send delete query, is idempotent */
            forecastAccessor.deleteForecastFixedCosts(sbu, subregion, period.getPeriod(), entryType, planPeriod.getPeriod());
        } catch (Exception e) {
            //TODO: implement better exception to be catched
            /* IF something goes wrong THEN return false */
            return false;
        }
        return true;
    }

    /**
     * Gets Budget Data from the forecast fixed costs table
     *
     * @param subregion      subregion in where clause
     * @param sbu            sbu in where clause
     * @param planPeriodFrom plan period to begin with in where clause
     * @param planPeriodTo   plan period to end with in where clause
     *
     * @return List with mapped Entities
     */
    public List<ForecastFixedCostsEntity> getBudgetForecastFixedCosts(String subregion, String sbu, Period planPeriodFrom,
                                                                      Period planPeriodTo) {
        List<ForecastFixedCostsEntity> resultList = new ArrayList<>();
        while (planPeriodFrom.getPeriod() < planPeriodTo.getPeriod()) {
            resultList.add(forecastAccessor.getSpecificForecastFixedCosts(sbu, subregion, planPeriodFrom.getPeriod(),
                    planPeriodFrom.getPeriod(), EntryType.BUDGET.getType()));
            /* increment period to fetch all months */
            planPeriodFrom.increment();
        }
        return resultList;
    }

    /**
     * Gets all distinct combinations of subregions and sbus in the fixed costs
     * tables
     *
     * @return Map with SBU as Key and Sets of subregions as values
     */
    public Map<String, Set<String>> getSubregionsAndSbu() {
        if (sbuMap == null) {
            querySubregionsAndSbu();
        }
        return sbuMap;
    }

    /**
     * Queries the database for all combinations of SBU and subregion and puts
     * them in the sbuMap
     */
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

    /**
     * Takes values from a fixed costs entity and puts them into the sbuMap
     *
     * @param entity fixed costs entity with values
     */
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