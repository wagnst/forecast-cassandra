package fourschlag.services.data.requests;

import com.datastax.driver.mapping.Result;
import fourschlag.entities.accessors.OrgStructureAccessor;
import fourschlag.entities.accessors.RegionAccessor;
import fourschlag.entities.tables.OrgStructureEntity;
import fourschlag.entities.tables.RegionEntity;
import fourschlag.services.db.CassandraConnection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Extends Request. Offers functionality to request product main groups and
 * sbus.
 */
public class OrgStructureAndRegionRequest extends Request {

    private OrgStructureAccessor orgStructureAccessor;
    private RegionAccessor regionAccessor;
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
        regionAccessor = getManager().createAccessor(RegionAccessor.class);
    }

    /**
     * method that applies the sbu belonging to a specific PMG
     *
     * @param productMainGroup product main group for which the sbu is supposed
     *                         to be found
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

    public boolean checkSalesParams(String productMainGroup, String region) {
        List<OrgStructureEntity> orgEntities = orgStructureAccessor.getEntitiesByPmg(productMainGroup).all();
        if (orgEntities.isEmpty()) {
            return false;
        }
        List<RegionEntity> regionEntities = regionAccessor.getEntitiesByRegion(region).all();

        return (!regionEntities.isEmpty());
    }

    public boolean checkFixedCostsParams(String sbu, String subregion) {
        List<OrgStructureEntity> orgEntities = orgStructureAccessor.getEntitiesBySbu(sbu).all();
        if (orgEntities.isEmpty()) {
            return false;
        }
        List<RegionEntity> regionEntities = regionAccessor.getEntitiesBySubregion(subregion).all();

        return (!regionEntities.isEmpty());
    }
}
