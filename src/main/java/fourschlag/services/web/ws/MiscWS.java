package fourschlag.services.web.ws;

import fourschlag.entities.types.Currency;
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

/**
 * MiscWS offers web services that don't need accecss to the database and don't
 * fit into any other category
 */
@Path("misc")
public class MiscWS {
    /**
     * Gets all keyspaces from the KeyspaceNames Enum
     *
     * @return WS Response with JSON containing all keyspace names
     */
    @GET
    @Path("get/keyspaces")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getKeyspaces() {
        return Response.ok(KeyspaceNames.values(), Params.MEDIATYPE).build();
    }

    /**
     * Gets all sales types from the SalesTypes Enum
     *
     * @return WS Response with JSON containing all sales types
     */
    @GET
    @Path("get/sales_types")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSalesTypes() {
        List<String> resultList = Arrays.stream(SalesType.values())
                .map(SalesType::getType)
                .collect(Collectors.toList());

        return Response.ok(resultList, Params.MEDIATYPE).build();
    }

    /**
     * Gets those entry types form the EntryTypes Enum that can occur in any of
     * the database tables
     *
     * @return WS Response with JSON containing entry types
     */
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

    /**
     * Gets all Currencies by their abbreviations
     *
     * @return WS Response with JSON containing all currencies
     */
    @GET
    @Path("get/currencies")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCurrencies() {
        List<String> resultList = Arrays.stream(Currency.values())
                .map(Currency::getAbbreviation)
                .collect(Collectors.toList());

        return Response.ok(resultList, Params.MEDIATYPE).build();
    }
}
