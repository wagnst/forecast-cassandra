package fourschlag.services.web.ws;

import fourschlag.services.data.SalesService;
import fourschlag.services.db.CassandraConnection;
import fourschlag.services.db.ClusterEndpoints;
import fourschlag.services.db.KeyspaceNames;
import fourschlag.services.web.Params;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * ForecastWS offers web services to get KPIs from a database
 */
@Path("/forecast")
public class ForecastWS {

    private CassandraConnection connection = new CassandraConnection(ClusterEndpoints.NODE1, KeyspaceNames.ORIGINAL_VERSION);
    private SalesService salesService = new SalesService(connection);

    /* TODO: Maybe close session each time, but not connection */
    /* TODO: Create Connection pool and remove the connection from this WS */

    /* No JavaDoc yet, because this method is subject to change */
    @GET
    @Path("/sales/{planyear}/{period}/{currency}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSalesKPIs(
            @PathParam("planyear") int planYear,
            @PathParam("period") int period,
            @PathParam("currency") String currency) {
        //TODO: period must be the present or the past, but must not be the future (I guess so --> to be confirmed by SP)
        return Response.ok(salesService.getSalesKPIs(planYear, period, currency), Params.MEDIATYPE).build();
    }
}