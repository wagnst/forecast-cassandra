package fourschlag.services.data.service;

import fourschlag.services.data.requests.OrgStructureAndRegionRequest;
import fourschlag.services.db.CassandraConnection;

import java.util.List;

public class OrgStructureAndRegionService extends Service {
    public OrgStructureAndRegionService(CassandraConnection connection) {
        super(connection);
    }

    public List<String> getProductMainGroups() {
        OrgStructureAndRegionRequest orgStructureAndRegionRequest = new OrgStructureAndRegionRequest(getConnection());
        return orgStructureAndRegionRequest.getProductMainGroups();
    }

    public List<String> getSbus() {
        OrgStructureAndRegionRequest orgStructureAndRegionRequest = new OrgStructureAndRegionRequest(getConnection());
        return orgStructureAndRegionRequest.getSbus();
    }

    public List<String> getRegions() {
        OrgStructureAndRegionRequest orgStructureAndRegionRequest = new OrgStructureAndRegionRequest(getConnection());
        return orgStructureAndRegionRequest.getRegions();
    }

    public List<String> getSubregions(String region) {
        OrgStructureAndRegionRequest orgStructureAndRegionRequest = new OrgStructureAndRegionRequest(getConnection());
        return orgStructureAndRegionRequest.getSubregionsForRegion(region);
    }
}
