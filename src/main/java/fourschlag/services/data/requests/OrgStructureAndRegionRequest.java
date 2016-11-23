package fourschlag.services.data.requests;

import com.datastax.driver.mapping.Result;
import fourschlag.entities.accessors.ActualSalesAccessor;
import fourschlag.entities.accessors.ForecastSalesAccessor;
import fourschlag.entities.accessors.OrgStructureAccessor;
import fourschlag.entities.tables.ActualSalesEntity;
import fourschlag.entities.tables.ForecastSalesEntity;
import fourschlag.entities.tables.OrgStructureEntity;
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
    private Set<String> productSet;
    private Set<String> regionSet;
    private Map<String, String> sbu;

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
    }

    /**
     * Queries the database for all Product Main Groups
     *
     * @return Result Iterable with multiple OrgStructure entities
     */
    private Result<OrgStructureEntity> getProductMainGroupsFromOrgStructure() {
        return orgStructureAccessor.getProductsAndSbus();
    }

    private Result<ActualSalesEntity> getProductMainGroupsFromActualSales() {
        return actualSalesAccessor.getProductMainGroups();
    }

    private Result<ForecastSalesEntity> getProductMainGroupsFromForecastSales() {
        return forecastSalesAccessor.getProductMainGroups();
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
        Result<ActualSalesEntity> entitiesFromActualSales = getProductMainGroupsFromActualSales();
        Result<ForecastSalesEntity> entitiesFromForecastSales = getProductMainGroupsFromForecastSales();
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
}
