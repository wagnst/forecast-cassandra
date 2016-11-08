package fourschlag.services.web.ws;

import fourschlag.services.data.SalesService;
import fourschlag.services.web.Params;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/forecast")
public class ForecastWS {
    private SalesService salesService = new SalesService();

    @GET
    @Path("/sales/{year}/{period}/{currency}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSalesKPIs(@PathParam("year") int year, @PathParam("period") int period, @PathParam("currency") String currency) {
        return Response.ok(salesService.getSalesKPIs(year, period, currency), Params.MEDIATYPE).build();
    }
}
