package fourschlag.services.data.requests;

import com.datastax.driver.mapping.Result;
import fourschlag.entities.accessors.OrgStructureAccessor;
import fourschlag.entities.accessors.RegionAccessor;
import fourschlag.entities.accessors.fixedcosts.ActualFixedCostsAccessor;
import fourschlag.entities.accessors.fixedcosts.ForecastFixedCostsAccessor;
import fourschlag.entities.accessors.sales.ActualSalesAccessor;
import fourschlag.entities.accessors.sales.ForecastSalesAccessor;
import fourschlag.entities.tables.OrgStructureEntity;
import fourschlag.entities.tables.RegionEntity;
import fourschlag.entities.tables.kpi.fixedcosts.ActualFixedCostsEntity;
import fourschlag.entities.tables.kpi.fixedcosts.ForecastFixedCostsEntity;
import fourschlag.entities.tables.kpi.sales.ActualSalesEntity;
import fourschlag.entities.tables.kpi.sales.ForecastSalesEntity;
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
        Result<ActualSalesEntity> entitiesFromActual = actualSalesAccessor.getDistinctPmgAndRegions();
        Result<ForecastSalesEntity> entitiesFromForecast = forecastSalesAccessor.getDistinctPmgAndRegions();
        productMap = new HashMap<>();

        for (ActualSalesEntity entity : entitiesFromActual) {
            if (productMap.containsKey(entity.getProductMainGroup())) {
                productMap.get(entity.getProductMainGroup()).add(entity.getRegion());
            } else {
                productMap.put(entity.getProductMainGroup(), new HashSet<String>(){{
                    add(entity.getRegion());
                }});
            }
        }

        for (ForecastSalesEntity entity : entitiesFromForecast) {
            if (productMap.containsKey(entity.getProductMainGroup())) {
                productMap.get(entity.getProductMainGroup()).add(entity.getRegion());
            } else {
                productMap.put(entity.getProductMainGroup(), new HashSet<String>(){{
                    add(entity.getRegion());
                }});
            }
        }
    }

    public Map<String, Set<String>> getSubregionsAndSbuFromFixedCosts() {
        if (sbuMap == null) {
            querySubregionsAndSbuFromFixedCosts();
        }
        return sbuMap;
    }

    private void querySubregionsAndSbuFromFixedCosts() {
        Result<ActualFixedCostsEntity> entitiesFromActual = actualFixedCostsAccessor.getDistinctSbuAndSubregions();
        Result<ForecastFixedCostsEntity> entitiesFromForecast = forecastFixedCostsAccessor.getDistinctSbuAndSubregions();
        sbuMap = new HashMap<>();

        for (ActualFixedCostsEntity entity : entitiesFromActual) {
            if (sbuMap.containsKey(entity.getSbu())) {
                sbuMap.get(entity.getSbu()).add(entity.getSubregion());
            } else {
                sbuMap.put(entity.getSbu(), new HashSet<String>(){{
                    add(entity.getSubregion());
                }});
            }
        }

        for (ForecastFixedCostsEntity entity : entitiesFromForecast) {
            if (sbuMap.containsKey(entity.getSbu())) {
                sbuMap.get(entity.getSbu()).add(entity.getSubregion());
            } else {
                sbuMap.put(entity.getSbu(), new HashSet<String>(){{
                    add(entity.getSubregion());
                }});
            }
        }
    }

    public String getSbu(String productMainGroup) {
        if (sbu == null) {
            Result<OrgStructureEntity> queryResult = orgStructureAccessor.getProductsAndSbus();
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

    public String getRegion(String subregion) {
        if (region == null) {
            Result<RegionEntity> queryResult = regionAccessor.getSubregions();
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
