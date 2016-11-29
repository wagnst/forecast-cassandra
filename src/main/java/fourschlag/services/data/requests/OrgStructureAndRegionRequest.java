package fourschlag.services.data.requests;

import com.datastax.driver.mapping.Result;
import fourschlag.entities.accessors.*;
import fourschlag.entities.accessors.fixedcosts.ActualFixedCostsAccessor;
import fourschlag.entities.accessors.fixedcosts.ForecastFixedCostsAccessor;
import fourschlag.entities.accessors.sales.ActualSalesAccessor;
import fourschlag.entities.accessors.sales.ForecastSalesAccessor;
import fourschlag.entities.tables.*;
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
    private Set<String> productSet;
    private Set<String> regionSet;
    private Set<String> sbuSet;
    private Set<String> subregionSet;
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

    /**
     * Queries the database for all Product Main Groups
     *
     * @return Result Iterable with multiple OrgStructure entities
     */
    private Result<OrgStructureEntity> getProductMainGroupsFromOrgStructure() {
        return orgStructureAccessor.getProductsAndSbus();
    }

    public Set<OrgStructureEntity> getProductMainGroupsAsSetFromOrgStructure() {
        Result<OrgStructureEntity> productsFromOrgStructure = getProductMainGroupsFromOrgStructure();

        Set<OrgStructureEntity> productSet = new HashSet<>();
        productsFromOrgStructure.forEach(product -> productSet.add(product));

        return productSet;
    }

    public Set<String> getProductMainGroupsAsSetFromSales() {
        if (productSet == null) {
            getPmgAndRegionAsSetFromSales();
        }
        return productSet;
    }

    public Set<String> getRegionsAsSetFromSales() {
        if (regionSet == null) {
            getPmgAndRegionAsSetFromSales();
        }
        return regionSet;
    }

    private void getPmgAndRegionAsSetFromSales() {
        Result<ActualSalesEntity> entitiesFromActualSales = actualSalesAccessor.getProductMainGroups();
        Result<ForecastSalesEntity> entitiesFromForecastSales = forecastSalesAccessor.getProductMainGroups();
        productSet = new HashSet<>();
        regionSet = new HashSet<>();
        for (ActualSalesEntity entity : entitiesFromActualSales) {
            productSet.add(entity.getProductMainGroup());
            regionSet.add(entity.getRegion());
        }
        for (ForecastSalesEntity entity : entitiesFromForecastSales) {
            productSet.add(entity.getProductMainGroup());
            regionSet.add(entity.getRegion());
        }
    }

    public Set<String> getSbuAsSetFromFixedCost() {
        if (sbuSet == null) {
            getSbuAndSubregionsAsSetFromFixedCosts();
        }
        return sbuSet;
    }

    public Set<String> getSubregionsAsSetFromFixedCosts() {
        if (subregionSet == null) {
            getSbuAndSubregionsAsSetFromFixedCosts();
        }
        return subregionSet;
    }

    private void getSbuAndSubregionsAsSetFromFixedCosts() {
        Result<ActualFixedCostsEntity> entitiesFromActualFixedCosts = actualFixedCostsAccessor.getSbuAndSubregions();
        Result<ForecastFixedCostsEntity> entitiesFromForecastFixedCosts = forecastFixedCostsAccessor.getSbuAndSubregions();

        sbuSet = new HashSet<>();
        subregionSet = new HashSet<>();
        for (ActualFixedCostsEntity entity : entitiesFromActualFixedCosts) {
            sbuSet.add(entity.getSbu());
            subregionSet.add(entity.getSubregion());
        }
        for (ForecastFixedCostsEntity entity : entitiesFromForecastFixedCosts) {
            sbuSet.add(entity.getSbu());
            subregionSet.add(entity.getSubregion());
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
