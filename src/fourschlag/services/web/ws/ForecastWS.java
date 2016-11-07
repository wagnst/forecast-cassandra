package fourschlag.services.web.ws;

import fourschlag.services.data.ActualSalesService;
import fourschlag.services.web.Params;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by thor on 07.11.2016.
 */
@Path("/forecast")
public class ForecastWS {
    private ActualSalesService actualSalesService = new ActualSalesService();

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSomethingFromActualSales() {
        return Response.ok(actualSalesService.getSomething(), Params.MEDIATYPE).build();
    }

    @GET
    @Path("/kpi/{product_main_group}/{period}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getKPIs(@PathParam("product_main_group") String product_main_group, @PathParam("period") int period) {
        return Response.ok(actualSalesService.getKPIs(product_main_group, period), Params.MEDIATYPE).build();
    }
}
