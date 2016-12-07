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

public class SalesRequest extends Request {

    private final ActualSalesAccessor actualAccessor;
    private final ForecastSalesAccessor forecastAccessor;
    private Map<String, Set<String>> productMap;

    public SalesRequest(CassandraConnection connection) {
        super(connection);

        actualAccessor = getManager().createAccessor(ActualSalesAccessor.class);
        forecastAccessor = getManager().createAccessor(ForecastSalesAccessor.class);
    }

    public boolean setForecastSales(double topdownAdjustSalesVolumes, double topdownAdjustNetSales, double topdownAdjustCm1, int planPeriod, int planYear, int planHalfYear,
                                    int planQuarter, int planMonth, String entryType, String status, String usercomment, String productMainGroup, String salesType,
                                    double salesVolumes, double netSales, double cm1, int period, String region,
                                    int periodYear, int periodMonth, String currency, String userId, String entryTs) {

        try {
            if (forecastAccessor.getForecastSales(productMainGroup, region, period, salesType, planPeriod, entryType) != null) {
                // update an existing record
                forecastAccessor.updateForecastSales(topdownAdjustSalesVolumes, topdownAdjustNetSales, topdownAdjustCm1, planPeriod, planYear, planHalfYear, planQuarter,
                        planMonth, entryType, status, usercomment, productMainGroup, salesType, salesVolumes, netSales, cm1, period, region, periodYear, periodMonth, currency, userId, entryTs);
            } else {
                forecastAccessor.setForecastSales(topdownAdjustSalesVolumes, topdownAdjustNetSales, topdownAdjustCm1, planPeriod, planYear, planHalfYear, planQuarter,
                        planMonth, entryType, status, usercomment, productMainGroup, salesType, salesVolumes, netSales, cm1, period, region, periodYear, periodMonth, currency, userId, entryTs);
            }
        } catch (Exception e) {
            //TODO: implement better exception to be catched
            return false;
        }

        return true;

    }

    //TODO: implement method for non-forecast related tables


    /**
     * Gets all ForecastSales with no filter applied
     *
     * @return all entities which are present inside forecast_sales
     */
    public List<ForecastSalesEntity> getForecastSales() {
        return forecastAccessor.getAllForecastSales().all();
    }

    /**
     * Gets a specific list of ForecastSalesEnteties with filter applied
     *
     * @return specific entities which are present inside forecast_sales
     */
    public List<ForecastSalesEntity> getForecastSales(String productMainGroup, String region, Period period, SalesType salesType, EntryType entryType, Period planPeriodFrom, Period planPeriodTo) {
        return forecastAccessor.getForecastSales(productMainGroup, region, period.getPeriod(), salesType.getType(), entryType.getType(), planPeriodFrom.getPeriod(), planPeriodTo.getPeriod()).all();
    }

    /**
     * Gets a specific ForecastSalesEntity filtered by joinedString primary keys
     *
     * @return single entity of ForecastSalesEntity
     */
    public ForecastSalesEntity getForecastSales(String productMainGroup, String region, Period period, SalesType salesType, Period planPeriod, EntryType entryType) {
        return forecastAccessor.getForecastSales(productMainGroup, region, period.getPeriod(), salesType.getType(), planPeriod.getPeriod(), entryType.getType()).one();
    }

    public Map<String, Set<String>> getPmgAndRegions() {
        if (productMap == null) {
            queryPmgAndRegions();
        }
        return productMap;
    }

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