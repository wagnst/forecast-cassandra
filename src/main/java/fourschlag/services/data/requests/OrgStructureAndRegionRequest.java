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
 * Extends Request. Offers functionality to request product main groups and sbus.
 *
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
     * Queries the database for all Product Main Groups from OrgStructure
     *
     * @return Result Iterable with multiple OrgStructure entities
     */
    private Result<OrgStructureEntity> getProductMainGroupsFromOrgStructure() {
        return orgStructureAccessor.getProductsAndSbus();
    }

    /**
     * Adds all products from a Product Main Group to a Set
     *
     * @return Result Set with multiple products
     */
    public Set<OrgStructureEntity> getProductMainGroupsAsSetFromOrgStructure() {
        Result<OrgStructureEntity> productsFromOrgStructure = getProductMainGroupsFromOrgStructure();

        Set<OrgStructureEntity> productSet = new HashSet<>();
        productsFromOrgStructure.forEach(product -> productSet.add(product));

        return productSet;
    }

    /**
     * Queries the database for all Product Main Groups from Sales
     *
     * @return Result Set with multiple products
     */
    public Set<String> getProductMainGroupsAsSetFromSales() {

        if (productSet == null) {
            getPmgAndRegionAsSetFromSales();
        }
        return productSet;
    }

    /**
     * Queries the database for all Regions from Sales
     *
     * @return Result Set with all distinct Regions
     */
    public Set<String> getRegionsAsSetFromSales() {
        if (regionSet == null) {
            getPmgAndRegionAsSetFromSales();
        }
        return regionSet;
    }

    /**
     * Queries the database for all Product Main Groups and Regions from Sales and adds the result to a set
     *
     */
    private void getPmgAndRegionAsSetFromSales() {
        /* Gets the Product Main Groups from the Actual SalesEntity */
        Result<ActualSalesEntity> entitiesFromActualSales = actualSalesAccessor.getProductMainGroups();
        /* Gets the Product Main Groups from the Forecast SalesEntity */
        Result<ForecastSalesEntity> entitiesFromForecastSales = forecastSalesAccessor.getProductMainGroups();
        productSet = new HashSet<>();
        regionSet = new HashSet<>();
        /* iterates over all Product Main Groups from Actual Sales */
        for (ActualSalesEntity entity : entitiesFromActualSales) {
           /* adds the Product Main Group to a result set*/
            productSet.add(entity.getProductMainGroup());
           /* adds the regions  to a result set */
            regionSet.add(entity.getRegion());
        }
        /* iterates over all Product Main Groups from Forecast Sales */
        for (ForecastSalesEntity entity : entitiesFromForecastSales) {
            /* adds the Product Main Group to a result set*/
            productSet.add(entity.getProductMainGroup());
            /* adds the regions to a result set */
            regionSet.add(entity.getRegion());
        }
    }

    /**
     * Queries the database for all sbus from fixed costs
     *
     * @return Result Set with all distinct sbus
     */
    public Set<String> getSbuAsSetFromFixedCost() {
        if (sbuSet == null) {
            getSbuAndSubregionsAsSetFromFixedCosts();
        }
        return sbuSet;
    }

    /**
     * Queries the database for all subregions from fixed costs
     *
     * @return Result Set with all distinct subregions
     */
    public Set<String> getSubregionsAsSetFromFixedCosts() {
        if (subregionSet == null) {
            getSbuAndSubregionsAsSetFromFixedCosts();
        }
        return subregionSet;
    }

    /**
     * Queries the database for all sbus and subregions from fixed costs and adds the result to a set
     */
    private void getSbuAndSubregionsAsSetFromFixedCosts() {
       /* Gets the sbus und subregions from the ActualFixedCostsEntity */
        Result<ActualFixedCostsEntity> entitiesFromActualFixedCosts = actualFixedCostsAccessor.getSbuAndSubregions();
       /* Gets the sbus und subregions from the ForecastFixedCostsEntity */
        Result<ForecastFixedCostsEntity> entitiesFromForecastFixedCosts = forecastFixedCostsAccessor.getSbuAndSubregions();

        sbuSet = new HashSet<>();
        subregionSet = new HashSet<>();
        /* iterates over all sbus and subregions from ActualFixedCosts */
        for (ActualFixedCostsEntity entity : entitiesFromActualFixedCosts) {
            /* adds the sbus to a result set */
            sbuSet.add(entity.getSbu());
            /* adds the subregions to a result set */
            subregionSet.add(entity.getSubregion());
        }
         /* iterates over all sbus and subregions from ForecastFixedCosts */
        for (ForecastFixedCostsEntity entity : entitiesFromForecastFixedCosts) {
            /* adds the sbus to a result set */
            sbuSet.add(entity.getSbu());
            /* adds the subregions to a result set */
            subregionSet.add(entity.getSubregion());
        }
    }

    /**
     * method that applies the sbu belonging to a specific PMG
     *
     * @param productMainGroup product main group for which the sbu is supposed to be found
     *
     * @return
     */
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

    /**
     * Getter for the region
     *
     * @param subregion
     *
     * @return
     */
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
