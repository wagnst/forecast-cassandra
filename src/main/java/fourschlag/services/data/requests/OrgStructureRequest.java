package fourschlag.services.data.requests;

import com.datastax.driver.mapping.Result;
import fourschlag.entities.accessors.ActualSalesAccessor;
import fourschlag.entities.accessors.ForecastSalesAccessor;
import fourschlag.entities.accessors.OrgStructureAccessor;
import fourschlag.entities.tables.ActualSalesEntity;
import fourschlag.entities.tables.ForecastSalesEntity;
import fourschlag.entities.tables.OrgStructureEntity;
import fourschlag.services.db.CassandraConnection;

import java.util.HashSet;
import java.util.Set;

/**
 * Extends Request. Offers functionality to request product main groups and
 * sbus.
 */
public class OrgStructureRequest extends Request {

    private OrgStructureAccessor orgStructureAccessor;
    private ActualSalesAccessor actualSalesAccessor;
    private ForecastSalesAccessor forecastSalesAccessor;

    /**
     * Constructor for OrgStructureRequest.
     *
     * @param connection Cassandra connection that is supposed to be used
     */
    public OrgStructureRequest(CassandraConnection connection) {
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
        return orgStructureAccessor.getProducts();
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
        Result<ActualSalesEntity> productsFromActualSales = getProductMainGroupsFromActualSales();
        Result<ForecastSalesEntity> productsFromForecastSales = getProductMainGroupsFromForecastSales();
        Set<String> productSet = new HashSet<>();
        productsFromActualSales.forEach(product -> productSet.add(product.getProductMainGroup()));
        productsFromForecastSales.forEach(product -> productSet.add(product.getProductMainGroup()));

        return productSet;
    }

    public String getSbu(String productMainGroup) {
        OrgStructureEntity sbu = orgStructureAccessor.getSbu(productMainGroup);
        if (sbu == null) {
            return productMainGroup;
        } else {
            return sbu.getSbu();
        }
    }
}
