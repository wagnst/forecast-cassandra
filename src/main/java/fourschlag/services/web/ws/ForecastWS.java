package fourschlag.services.web.ws;

import fourschlag.entities.types.OutputDataType;
import fourschlag.services.data.SalesService;
import fourschlag.services.data.FixedCostsService;
import fourschlag.services.db.CassandraConnection;
import fourschlag.services.web.Params;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ForecastWS offers web services to get KPIs from a database
 */
@Path("/forecast")
public class ForecastWS {

    private CassandraConnection connection = CassandraConnection.getInstance();
    private SalesService salesService = new SalesService(connection);
    private FixedCostsService fixedCostsService = new FixedCostsService(connection);

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

        List<OutputDataType> resultList = salesService.getSalesKPIs(planYear, period, currency)
                .flatMap(outputDataType -> fixedCostsService.getFixedCostsKpis(planYear, period, currency))
                .collect(Collectors.toList());

        return Response.ok(resultList, Params.MEDIATYPE).build();
    }
}