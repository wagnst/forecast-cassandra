package fourschlag.services.web.ws;

import fourschlag.services.data.ForecastService;
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
    private ForecastService forecastService = new ForecastService();

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSomething() {
        return Response.ok(forecastService.getSomething(), Params.MEDIATYPE).build();
    }
}
