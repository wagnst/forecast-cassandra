package fourschlag.services.data.requests;

import fourschlag.entities.accessors.OrgStructureAccessor;
import fourschlag.entities.accessors.RegionAccessor;
import fourschlag.entities.accessors.fixedcosts.ActualFixedCostsAccessor;
import fourschlag.entities.accessors.fixedcosts.ForecastFixedCostsAccessor;
import fourschlag.entities.accessors.sales.ActualSalesAccessor;
import fourschlag.entities.accessors.sales.ForecastSalesAccessor;
import fourschlag.entities.tables.OrgStructureEntity;
import fourschlag.entities.tables.RegionEntity;
import fourschlag.entities.tables.kpi.fixedcosts.ActualFixedCostsEntity;
import fourschlag.entities.tables.kpi.fixedcosts.FixedCostsEntity;
import fourschlag.entities.tables.kpi.fixedcosts.ForecastFixedCostsEntity;
import fourschlag.entities.tables.kpi.sales.ActualSalesEntity;
import fourschlag.entities.tables.kpi.sales.ForecastSalesEntity;
import fourschlag.entities.tables.kpi.sales.SalesEntity;
import fourschlag.services.db.CassandraConnection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Extends Request. Offers functionality to request product main groups and
 * sbus.
 */
public class OrgStructureAndRegionRequest extends Request {

    private OrgStructureAccessor orgStructureAccessor;
    private ActualSalesAccessor actualSalesAccessor;
    private ForecastSalesAccessor forecastSalesAccessor;
    private ActualFixedCostsAccessor actualFixedCostsAccessor;
    private ForecastFixedCostsAccessor forecastFixedCostsAccessor;
    private RegionAccessor regionAccessor;
    private Map<String, Set<String>> productMap;
    private Map<String, Set<String>> sbuMap;
    private Map<String, String> sbu;
    private Map<String, String> region;

    /**
     * Constructor for OrgStructureAndRegionRequest.
     *
     * @param connection Cassandra connection that is supposed to be used
     */
    public OrgStructureAndRegionRequest(CassandraConnection connection) {
        super(connection);
        orgStructureAccessor = getManager().createAccessor(OrgStructureAccessor.class);
        actualSalesAccessor = getManager().createAccessor(ActualSalesAccessor.class);
        forecastSalesAccessor = getManager().createAccessor(ForecastSalesAccessor.class);
        regionAccessor = getManager().createAccessor(RegionAccessor.class);
        actualFixedCostsAccessor = getManager().createAccessor(ActualFixedCostsAccessor.class);
        forecastFixedCostsAccessor = getManager().createAccessor(ForecastFixedCostsAccessor.class);
    }

    public Map<String, Set<String>> getPmgAndRegionsFromSales() {
        if (productMap == null) {
            queryPmgAndRegionsFromSales();
        }
        return productMap;
    }

    private void queryPmgAndRegionsFromSales() {
        Iterable<ActualSalesEntity> entitiesFromActual = actualSalesAccessor.getDistinctPmgAndRegions();
        Iterable<ForecastSalesEntity> entitiesFromForecast = forecastSalesAccessor.getDistinctPmgAndRegions();
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

    public Map<String, Set<String>> getSubregionsAndSbuFromFixedCosts() {
        if (sbuMap == null) {
            querySubregionsAndSbuFromFixedCosts();
        }
        return sbuMap;
    }

    private void querySubregionsAndSbuFromFixedCosts() {
        Iterable<ActualFixedCostsEntity> entitiesFromActual = actualFixedCostsAccessor.getDistinctSbuAndSubregions();
        Iterable<ForecastFixedCostsEntity> entitiesFromForecast = forecastFixedCostsAccessor.getDistinctSbuAndSubregions();
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

    /**
     * method that applies the sbu belonging to a specific PMG
     *
     * @param productMainGroup product main group for which the sbu is supposed to be found
     * @return
     */
    public String getSbu(String productMainGroup) {
        if (sbu == null) {
            Iterable<OrgStructureEntity> queryResult = orgStructureAccessor.getProductsAndSbus();
            sbu = new HashMap<String, String>() {{
                for (OrgStructureEntity entity : queryResult) {
                    put(entity.getProductMainGroup(), entity.getSbu());
                }
            }};
        }
        String returnValue = sbu.get(productMainGroup);
        if (returnValue == null) {
            return productMainGroup;
        }
        return returnValue;
    }

    /**
     * Getter for the region
     *
     * @param subregion
     * @return
     */
    public String getRegion(String subregion) {
        if (region == null) {
            Iterable<RegionEntity> queryResult = regionAccessor.getSubregions();
            region = new HashMap<String, String>() {{
                for (RegionEntity entity : queryResult) {
                    put(entity.getSubregion(), entity.getRegion());
                }
            }};
        }
        String returnValue = region.get(subregion);
        if (returnValue == null) {
            return subregion;
        }
        return returnValue;
    }
}
