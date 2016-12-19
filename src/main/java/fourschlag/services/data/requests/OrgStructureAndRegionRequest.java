package fourschlag.services.data.requests;

import com.datastax.driver.mapping.Result;
import fourschlag.entities.accessors.OrgStructureAccessor;
import fourschlag.entities.accessors.RegionAccessor;
import fourschlag.entities.tables.OrgStructureEntity;
import fourschlag.entities.tables.RegionEntity;
import fourschlag.services.db.CassandraConnection;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Extends Request. Offers functionality to query the OrgStructure and Regions
 * tables
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
     * Gets all Product Main Groups from the org_structure table
     *
     * @return List with all PMGs
     */
    public List<String> getProductMainGroups() {
        Result<OrgStructureEntity> queryResult = orgStructureAccessor.getDistinctPmg();

        List<String> resultList = new ArrayList<>();
        queryResult.forEach(e -> resultList.add(e.getProductMainGroup()));
        return resultList;
    }

    /**
     * Gets all SBUs from the org_structure table
     *
     * @return List with all SBUs
     */
    public List<String> getSbus() {
        Result<OrgStructureEntity> queryResult = orgStructureAccessor.getProductsAndSbus();

        /* Remove duplicates with a Set */
        Set<String> set = new HashSet<>();
        queryResult.forEach(e -> set.add(e.getSbu()));

        return set.stream().collect(Collectors.toList());
    }

    /**
     * Gets all Regions from the regions table
     *
     * @return List with all Regions
     */
    public List<String> getRegions() {
        Result<RegionEntity> queryResult = regionAccessor.getAll();

        Set<String> set = new HashSet<>();
        queryResult.forEach(e -> set.add(e.getRegion()));
        return set.stream().collect(Collectors.toList());
    }

    /**
     * Gets all subregions for a specific region
     *
     * @param region region to filter for
     *
     * @return List with subregions
     */
    public List<String> getSubregionsForRegion(String region) {
        Result<RegionEntity> queryResult = regionAccessor.getEntitiesByRegion(region);
        List<String> resultList = new ArrayList<>();
        queryResult.forEach(e -> resultList.add(e.getSubregion()));

        return resultList;
    }

    /**
     * Method that finds the correct SBU of a product main group
     *
     * @param productMainGroup product main group for which the sbu is needed
     *
     * @return SBU that corresponds with the given PMG
     */
    public String getSbuByPmg(String productMainGroup) {
        /* IF the map is null THEN fill it with data from the OrgStructure table */
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
     * Method that finds the correct region for a subregion
     *
     * @param subregion subregion for which the region is needed
     *
     * @return region that corresponds with the given subregion
     */
    public String getRegionBySubregion(String subregion) {
        /* IF the map is null THEN fill it with data from the Regions table */
        if (region == null) {
            Result<RegionEntity> queryResult = regionAccessor.getAll();
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

    /**
     * Checks if the given parameters are valid for a Sales Entity
     *
     * @param productMainGroup product main group to be checked
     * @param region           region to be checked
     *
     * @return True IF parameters are valid; False if not
     */
    boolean checkSalesParams(String productMainGroup, String region) {
        List<OrgStructureEntity> orgEntities = orgStructureAccessor.getEntitiesByPmg(productMainGroup).all();
        if (orgEntities.isEmpty()) {
            return false;
        }
        List<RegionEntity> regionEntities = regionAccessor.getEntitiesByRegion(region).all();

        return (!regionEntities.isEmpty());
    }

    /**
     * Checks if the given parameters are valid for a Fixed Costs Entity
     *
     * @param sbu       sbu to be checked
     * @param subregion subregion to be checked
     * @param region    region to be checked
     *
     * @return True IF parameters are valid; False if not
     */
    boolean checkFixedCostsParams(String sbu, String subregion, String region) {
        List<OrgStructureEntity> orgEntities = orgStructureAccessor.getEntitiesBySbu(sbu).all();
        if (orgEntities.isEmpty()) {
            return false;
        }
        RegionEntity regionEntity = regionAccessor.getSpecificEntity(subregion, region);

        return (regionEntity != null);
    }
}
