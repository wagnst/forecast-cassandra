package fourschlag.services.web.ws;

import fourschlag.services.data.service.OrgStructureAndRegionService;
import fourschlag.services.db.CassandraConnection;
import fourschlag.services.db.ClusterEndpoints;
import fourschlag.services.db.ConnectionPool;
import fourschlag.services.db.KeyspaceNames;
import fourschlag.services.web.Params;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * OrgStructureAndRegionWS offers functionality to get data directly from the org_structure and regions tables
 */
@Path("{keyspace}/org_region")
public class OrgStructureAndRegionWS {
    private OrgStructureAndRegionService orgStructureAndRegionService;

    public OrgStructureAndRegionWS(@PathParam("keyspace") String keyspace) {
        CassandraConnection connection = ConnectionPool.getConnection(
                ClusterEndpoints.NODE1, KeyspaceNames.valueOf(keyspace.toUpperCase()), true);

        orgStructureAndRegionService = new OrgStructureAndRegionService(connection);
    }

    /**
     * Gets all product main groups from org_structure
     * @return WS Response with JSON containing all PMGs
     */
    @GET
    @Path("get/product_main_groups")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductMainGroups() {
        return Response.ok(orgStructureAndRegionService.getProductMainGroups(), Params.MEDIATYPE).build();
    }

    /**
     * Gets all SBUs from the org_structure table
     * @return WS Response with JSON containing all SBUs
     */
    @GET
    @Path("get/sbus")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSbus() {
        return Response.ok(orgStructureAndRegionService.getSbus(), Params.MEDIATYPE).build();
    }

    /**
     * Gets all regions from the regions table
     * @return WS Response with JSON containing all regions
     */
    @GET
    @Path("get/regions")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRegions() {
        return Response.ok(orgStructureAndRegionService.getRegions(), Params.MEDIATYPE).build();
    }

    /**
     * Gets all subregions for a specific region from the regions table
     * @param region region to filter for
     * @return WS Response with JSON containing subregions
     */
    @GET
    @Path("get/subregions/region/{region}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSubregions(@PathParam("region") String region) {
        return Response.ok(orgStructureAndRegionService.getSubregions(region), Params.MEDIATYPE).build();
    }
}
