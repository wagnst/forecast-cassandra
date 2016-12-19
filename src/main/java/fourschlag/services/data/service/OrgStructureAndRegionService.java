package fourschlag.services.data.service;

import fourschlag.services.data.requests.OrgStructureAndRegionRequest;
import fourschlag.services.db.CassandraConnection;

import java.util.List;

/**
 * Extends Service. Provides functionality to access OrgStructure and Regions
 * data
 */
public class OrgStructureAndRegionService extends Service {
    public OrgStructureAndRegionService(CassandraConnection connection) {
        super(connection);
    }

    /**
     * Gets all Product Main Groups from the org_structure table
     *
     * @return List with all PMGs
     */
    public List<String> getProductMainGroups() {
        OrgStructureAndRegionRequest orgStructureAndRegionRequest = new OrgStructureAndRegionRequest(getConnection());
        return orgStructureAndRegionRequest.getProductMainGroups();
    }

    /**
     * Gets all SBUs from the org_structure table
     *
     * @return List with all SBUs
     */
    public List<String> getSbus() {
        OrgStructureAndRegionRequest orgStructureAndRegionRequest = new OrgStructureAndRegionRequest(getConnection());
        return orgStructureAndRegionRequest.getSbus();
    }

    /**
     * Gets all Regions from the regions table
     *
     * @return List with all Regions
     */
    public List<String> getRegions() {
        OrgStructureAndRegionRequest orgStructureAndRegionRequest = new OrgStructureAndRegionRequest(getConnection());
        return orgStructureAndRegionRequest.getRegions();
    }

    /**
     * Gets all subregions for a specific region
     *
     * @param region region to filter for
     *
     * @return List with subregions
     */
    public List<String> getSubregions(String region) {
        OrgStructureAndRegionRequest orgStructureAndRegionRequest = new OrgStructureAndRegionRequest(getConnection());
        return orgStructureAndRegionRequest.getSubregionsForRegion(region);
    }
}
