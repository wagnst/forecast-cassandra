package fourschlag.services.data.requests;

import com.datastax.driver.mapping.Result;
import fourschlag.entities.accessors.sales.ActualSalesAccessor;
import fourschlag.entities.accessors.sales.ForecastSalesAccessor;
import fourschlag.entities.tables.kpi.sales.ActualSalesEntity;
import fourschlag.entities.tables.kpi.sales.ForecastSalesEntity;
import fourschlag.entities.tables.kpi.sales.SalesEntity;
import fourschlag.entities.types.EntryType;
import fourschlag.entities.types.Period;
import fourschlag.entities.types.SalesType;
import fourschlag.services.db.CassandraConnection;

import java.util.*;

/**
 * Extends Request. Offers functionality to query the Sales tables.
 */
public class SalesRequest extends Request {

    private final ActualSalesAccessor actualAccessor;
    private final ForecastSalesAccessor forecastAccessor;
    private Map<String, Set<String>> productMap;

    public SalesRequest(CassandraConnection connection) {
        super(connection);

        actualAccessor = getManager().createAccessor(ActualSalesAccessor.class);
        forecastAccessor = getManager().createAccessor(ForecastSalesAccessor.class);
    }

    /**
     * Inserts (if combination of primary keys does not exist) or updates a row in the forecast sales table
     *
     * @return True IF process was successful; False IF it something went wrong
     */
    public boolean setForecastSales(double topdownAdjustSalesVolumes, double topdownAdjustNetSales, double topdownAdjustCm1, Period planPeriod,
                                    String entryType, String status, String usercomment, String productMainGroup, String salesType,
                                    double salesVolumes, double netSales, double cm1, Period period, String region,
                                    String currency, String userId, String entryTs) {

        OrgStructureAndRegionRequest request = new OrgStructureAndRegionRequest(getConnection());

        /* Check if the given values for product main group and region are existent in the OrgStructure and Regions tables */
        if (!request.checkSalesParams(productMainGroup, region)) {
            /* IF invalid THEN return false */
            return false;
        }

        try {
            /* IF the forecast sales table already has data for that combination of primary key values */
            if (forecastAccessor.getSpecificForecastSales(productMainGroup, region, period.getPeriod(), salesType, planPeriod.getPeriod(), entryType) != null) {
                /* THEN update that existing record */
                forecastAccessor.updateForecastSales(topdownAdjustSalesVolumes, topdownAdjustNetSales, topdownAdjustCm1, planPeriod.getPeriod(), planPeriod.getYear(), planPeriod.getHalfYear(), planPeriod.getQuarter(),
                        planPeriod.getMonth(), entryType, status, usercomment, productMainGroup, salesType, salesVolumes, netSales, cm1, period.getPeriod(), region, period.getYear(), period.getMonth(), currency, userId, entryTs);
            } else {
                /* ELSE insert a new row */
                forecastAccessor.setForecastSales(topdownAdjustSalesVolumes, topdownAdjustNetSales, topdownAdjustCm1, planPeriod.getPeriod(), planPeriod.getYear(), planPeriod.getHalfYear(), planPeriod.getQuarter(),
                        planPeriod.getMonth(), entryType, status, usercomment, productMainGroup, salesType, salesVolumes, netSales, cm1, period.getPeriod(), region, period.getYear(), period.getMonth(), currency, userId, entryTs);
            }
        } catch (Exception e) {
            /* IF something goes wrong THEN return false */
            //TODO: implement better exception to be catched
            return false;
        }

        return true;

    }


    /**
     * Gets all ForecastSales with no filter applied
     *
     * @return all entities which are present inside forecast_sales
     */
    public List<ForecastSalesEntity> getAllForecastSales() {
        return forecastAccessor.getAllForecastSales().all();
    }

    /**
     * Gets a specific ForecastSalesEntity filtered by joinedString primary keys
     *
     * @return single entity of ForecastSalesEntity
     */
    public ForecastSalesEntity getSpecificForecastSales(String productMainGroup, String region, Period period, SalesType salesType, Period planPeriod, EntryType entryType) {
        return forecastAccessor.getSpecificForecastSales(productMainGroup, region, period.getPeriod(), salesType.getType(), planPeriod.getPeriod(), entryType.getType());
    }

    /**
     * Gets a specific list of ForecastSalesEnteties with filter applied
     *
     * @return specific entities which are present inside forecast_sales
     */
    public List<ForecastSalesEntity> getMultipleForecastSales(String productMainGroup, String region, Period period, SalesType salesType, EntryType entryType, Period planPeriodFrom, Period planPeriodTo) {
        return forecastAccessor.getMultipleForecastSales(productMainGroup, region, period.getPeriod(), salesType.getType(), entryType.getType(), planPeriodFrom.getPeriod(), planPeriodTo.getPeriod()).all();
    }

    /**
     * Method can delete an entity from forecast_sales with its primary
     * key
     *
     * @return boolean if successfull or not
     */
    public boolean deleteForecastSales(String productMainGroup, String region, Period period, String salesType, Period planPeriod, String entryType) {
        try {
            /* send delete query, is idempotent */
            forecastAccessor.deleteForecastSales(productMainGroup, region, period.getPeriod(), salesType, planPeriod.getPeriod(), entryType);
        } catch (Exception e) {
            //TODO: implement better expception to be catched
            /* IF something goes wrong THEN return false */
            return false;
        }
        return true;
    }

    /**
     * Gets Budget Data from the forecast sales table
     *
     * @param productMainGroup productMainGroup in where clause
     * @param region region in where clause
     * @param salesType salesType in where clause
     * @param planPeriodFrom plan period to begin with in where clause
     * @param planPeriodTo plan period to end with in where clause
     * @return List with mapped entities
     */
    public List<ForecastSalesEntity> getBudgetForecastSales(String productMainGroup, String region, SalesType salesType, Period planPeriodFrom, Period planPeriodTo) {
        List<ForecastSalesEntity> resultList = new ArrayList<>();
        while (planPeriodFrom.getPeriod() < planPeriodTo.getPeriod()) {
            resultList.add(forecastAccessor.getSpecificForecastSales(productMainGroup, region, planPeriodFrom.getPeriod(),
                    salesType.getType(), planPeriodFrom.getPeriod(), EntryType.BUDGET.getType()));
            /*increment period to fetch all months */
            planPeriodFrom.increment();
        }
        return resultList;
    }

    /**
     * Gets all distinct combinations of product main groups and regions in the fixed costs tables
     *
     * @return Map with product main group as Key and Sets of regions as values
     */
    public Map<String, Set<String>> getPmgAndRegions() {
        if (productMap == null) {
            queryPmgAndRegions();
        }
        return productMap;
    }

    /**
     * Queries the database for all combinations of product main groups and regions and puts them in the productMap
     */
    private void queryPmgAndRegions() {
        Result<ActualSalesEntity> entitiesFromActual = actualAccessor.getDistinctPmgAndRegions();
        Result<ForecastSalesEntity> entitiesFromForecast = forecastAccessor.getDistinctPmgAndRegions();
        productMap = new HashMap<>();

        for (ActualSalesEntity entity : entitiesFromActual) {
            addToProductMap(entity);
        }

        for (ForecastSalesEntity entity : entitiesFromForecast) {
            addToProductMap(entity);
        }
    }

    /**
     * Takes values from a sales entity and puts them into the productMap
     *
     * @param entity sales entity with values
     */
    private void addToProductMap(SalesEntity entity) {
        if (productMap.containsKey(entity.getProductMainGroup())) {
            productMap.get(entity.getProductMainGroup()).add(entity.getRegion());
        } else {
            productMap.put(entity.getProductMainGroup(), new HashSet<String>() {{
                add(entity.getRegion());
            }});
        }
    }
}