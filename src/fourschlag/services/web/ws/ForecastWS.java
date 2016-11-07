package fourschlag.services.web.ws;

import fourschlag.services.data.ActualSalesService;
import fourschlag.services.web.Params;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
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
}
