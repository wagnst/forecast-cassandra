package fourschlag.services.web.ws;

import fourschlag.entities.types.EntryType;
import fourschlag.entities.types.SalesType;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Path("{keyspace}/misc")
public class MiscWS {
    private OrgStructureAndRegionService orgStructureAndRegionService;

    public MiscWS(@PathParam("keyspace") String keyspace) {
        CassandraConnection connection = ConnectionPool.getConnection(
                ClusterEndpoints.NODE1, KeyspaceNames.valueOf(keyspace.toUpperCase()), true);

        orgStructureAndRegionService = new OrgStructureAndRegionService(connection);
    }

    @GET
    @Path("get/sales_types")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSalesTypes() {
        List<String> resultList = Arrays.stream(SalesType.values())
                .map(SalesType::getType)
                .collect(Collectors.toList());

        return Response.ok(resultList, Params.MEDIATYPE).build();
    }

    @GET
    @Path("get/entry_types")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEntryTypes() {
        List<String> resultList = Arrays.stream(EntryType.values())
                .filter(EntryType::isInTable)
                .map(EntryType::getType)
                .collect(Collectors.toList());

        return Response.ok(resultList, Params.MEDIATYPE).build();
    }

    @GET
    @Path("get/product_main_groups")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductMainGroups() {
        return Response.ok(orgStructureAndRegionService.getProductMainGroups(), Params.MEDIATYPE).build();
    }

    @GET
    @Path("get/sbus")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSbus() {
        return Response.ok(orgStructureAndRegionService.getSbus(), Params.MEDIATYPE).build();
    }

    @GET
    @Path("get/regions")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRegions() {
        return Response.ok(orgStructureAndRegionService.getRegions(), Params.MEDIATYPE).build();
    }

    @GET
    @Path("get/subregions/region/{region}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSubregions(@PathParam("region") String region) {
        return Response.ok(orgStructureAndRegionService.getSubregions(region), Params.MEDIATYPE).build();
    }
}
