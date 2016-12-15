package fourschlag.services.web.ws;

import fourschlag.entities.types.EntryType;
import fourschlag.entities.types.SalesType;
import fourschlag.services.db.KeyspaceNames;
import fourschlag.services.web.Params;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Path("misc")
public class MiscWS {
    @GET
    @Path("get/keyspaces")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getKeyspaces() {
        List<String> resultList = Arrays.stream(KeyspaceNames.values())
                .map(KeyspaceNames::getKeyspace)
                .collect(Collectors.toList());

        return Response.ok(resultList, Params.MEDIATYPE).build();
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
}
