package fourschlag.services.web.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("forecast")
public class ForecastWS {
    private static final String STANDARD_KEYSPACE = "HUNDRED_THOUSAND";
    private ForecastWSWithKeyspace webservice;

    public ForecastWS() {
        webservice = new ForecastWSWithKeyspace(STANDARD_KEYSPACE);
    }

    /**
     * This method collects all to be calculated forecast KPI's in
     * the different service classes
     *
     * @param currency desired currency parameter
     * @param planYear desired plan-year parameter
     * @param period   desired period parameter
     *
     * @return WS Response as JSON containing all calculated KPI's
     */
    @GET
    @Path("period/{period}/planyear/{planyear}/currency/{currency}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getKPIs(
            @PathParam("currency") String currency,
            @PathParam("planyear") int planYear,
            @PathParam("period") int period) {

        return webservice.getKPIs(currency, planYear, period);
    }

    /**
     * This method collects only sales dependent forecast KPI's in
     * the different service classes
     *
     * @param currency desired currency parameter
     * @param planYear desired plan-year parameter
     * @param period   desired period parameter
     *
     * @return WS Response as JSON containing all calculated KPI's
     */
    @GET
    @Path("period/{period}/planyear/{planyear}/currency/{currency}/sales")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSalesKPIs(
            @PathParam("currency") String currency,
            @PathParam("planyear") int planYear,
            @PathParam("period") int period) {

        return webservice.getSalesKPIs(currency, planYear, period);
    }

    /**
     * This method collects fixed costs related forecast KPI's in
     * the different service classes
     *
     * @param currency desired currency parameter
     * @param planYear desired plan-year parameter
     * @param period   desired period parameter
     *
     * @return WS Response as JSON containing all calculated KPI's
     */
    @GET
    @Path("period/{period}/planyear/{planyear}/currency/{currency}/fixedcosts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFixedCostsKPIs(
            @PathParam("currency") String currency,
            @PathParam("planyear") int planYear,
            @PathParam("period") int period) {

        return webservice.getFixedCostsKPIs(currency, planYear, period);
    }
}
