package fourschlag.services.web.ws;

import fourschlag.services.data.service.SalesService;
import fourschlag.services.db.CassandraConnection;
import fourschlag.services.db.ClusterEndpoints;
import fourschlag.services.db.ConnectionPool;
import fourschlag.services.db.KeyspaceNames;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * ForecastWS offers web service to get KPIs from a database
 */
@Path("/sales")
public class ForecastSalesWS {
    private CassandraConnection connection = ConnectionPool.getConnection(ClusterEndpoints.NODE1, KeyspaceNames.ORIGINAL_VERSION, true);
    private SalesService salesService = new SalesService(connection);

    /**
     * Method returns all entries from table forecast_sales
     * as a valid WebService
     *
     * @return all entries from forecast_sales
     */
    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getForecastSales() {
        return Response.ok(salesService.getForecastSales()).build();
    }

    /**
     * Method returns a specifiy entry from table forecast_fixed_costs
     * as a valid WebService
     *
     * @return a specific entry of forecast_fixed_costs
     */
    @GET
    @Path("/product_main_group/{productMainGroup}/region/{region}/period/{period}/sales_type/{salesType}/plan_period/{planPeriod}/entry_type/{entryType}")
    @Produces(MediaType.APPLICATION_JSON)

    public Response getOneForecastFixedCost(
            @PathParam("productMainGroup") String productMainGroup,
            @PathParam("region") String region,
            @PathParam("period") int period,
            @PathParam("salesType") String salesType,
            @PathParam("planPeriod") int planPeriod,
            @PathParam("entryType") String entryType) {
        return Response.ok(salesService.getForecastSales(productMainGroup, region, period, salesType, planPeriod, entryType)).build();
    }

    /**
     * This method allows data modification or creation of ForecastSales
     * related table
     *
     * @return HTTP Response OK or BAD_REQUEST
     */
    @POST
    @Path("")
    public Response createForecastSales(
            @FormParam("topdown_adjust_sales_volumes") double topdownAdjustSalesVolumes,
            @FormParam("topdown_adjust_net_sales") double topdownAdjustNetSales,
            @FormParam("topdown_adjust_cm1") double topdownAdjustCm1,
            @FormParam("plan_period") int planPeriod,
            @FormParam("plan_year") int planYear,
            @FormParam("plan_half_year") int planHalfYear,
            @FormParam("plan_quarter") int planQuarter,
            @FormParam("plan_month") int planMonth,
            @FormParam("entry_type") String entryType,
            @FormParam("status") String status,
            @FormParam("usercomment") String usercomment,
            @FormParam("product_main_group") String productMainGroup,
            @FormParam("sales_type") String salesType,
            @FormParam("sales_volumes") double salesVolumes,
            @FormParam("net_sales") double netSales,
            @FormParam("cm1") double cm1,
            @FormParam("period") int period,
            @FormParam("region") String region,
            @FormParam("period_year") int periodYear,
            @FormParam("period_month") int periodMonth,
            @FormParam("currency") String currency,
            @FormParam("userid") String userId,
            @FormParam("entry_ts") String entryTs) {
        if (salesService.setForecastSales(
                topdownAdjustSalesVolumes, topdownAdjustNetSales, topdownAdjustCm1, planPeriod, planYear, planHalfYear, planQuarter, planMonth, entryType,
                status, usercomment, productMainGroup, salesType, salesVolumes, netSales, cm1, period, region, periodYear, periodMonth, currency, userId, entryTs)) {
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}