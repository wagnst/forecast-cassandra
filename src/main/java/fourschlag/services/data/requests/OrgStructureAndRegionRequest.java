package fourschlag.services.data.requests;

import fourschlag.entities.jpaAccessors.OrgStructureAccessor;
import fourschlag.entities.jpaAccessors.RegionAccessor;
import fourschlag.entities.jpaTables.OrgStructureEntity;
import fourschlag.entities.jpaTables.RegionEntity;
import fourschlag.services.db.JpaConnection;

import java.util.HashMap;
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
     */
    public OrgStructureAndRegionRequest(JpaConnection connection) {
        super(connection);
        orgStructureAccessor = new OrgStructureAccessor(connection);
        regionAccessor = new RegionAccessor(connection);
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
                    put(entity.getPrimaryKey().getProductMainGroup(), entity.getPrimaryKey().getSbu());
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
                    put(entity.getPrimaryKey().getSubregion(), entity.getPrimaryKey().getRegion());
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
