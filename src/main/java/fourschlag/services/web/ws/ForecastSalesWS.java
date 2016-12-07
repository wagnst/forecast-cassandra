package fourschlag.services.web.ws;

import fourschlag.entities.types.EntryType;
import fourschlag.entities.types.OutputDataType;
import fourschlag.entities.types.Period;
import fourschlag.entities.types.SalesType;
import fourschlag.services.data.service.SalesService;
import fourschlag.services.db.CassandraConnection;
import fourschlag.services.db.ClusterEndpoints;
import fourschlag.services.db.ConnectionPool;
import fourschlag.services.db.KeyspaceNames;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static fourschlag.services.web.ws.ParameterValidator.*;

/**
 * ForecastSalesWS offers web service to get KPIs from a database
 */
@Path("/{keyspace}/sales")
public class ForecastSalesWS {

    private CassandraConnection connection;
    private SalesService salesService;

    /**
     * Constructor to initialize the database connection and services
     */
    public ForecastSalesWS(@PathParam("keyspace") String keyspace) {
        connection = ConnectionPool.getConnection(ClusterEndpoints.NODE1, KeyspaceNames.valueOf(keyspace.toUpperCase()), true);
        salesService = new SalesService(connection);
    }

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
     * Method returns a specific entry from table forecast_sales
     * as a valid WebService
     *
     * @return a specific entry of forecast_sales
     */
    @GET
    @Path("/product_main_group/{productMainGroup}/region/{region}/period/{period}/sales_type/{salesType}/plan_period/{planPeriod}/entry_type/{entryType}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOneForecastFixedCost(
            @PathParam("productMainGroup") String productMainGroup,
            @PathParam("region") String region,
            @PathParam("period") int period,
            @PathParam("salesType") String salesType,
            @PathParam("planPeriod") int planPeriodInt,
            @PathParam("entryType") String entryType) {

        if (validatePeriod(period) && validateSalesType(salesType) && validatPlanPeriod(planPeriodInt) && validateEntryType(entryType)) {
            Period currentPeriod = new Period(period);
            Period planPeriod = new Period(planPeriodInt);
            return Response.ok(salesService.getForecastSales(productMainGroup, region, currentPeriod,
                    SalesType.valueOf(salesType.toUpperCase()), planPeriod, EntryType.valueOf(entryType.toUpperCase()))).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request: Parameters are not valid").build();
        }
    }

    /**
     * Method returns specific entries from table forecast_sales
     * as a valid WebService
     *
     * @return a specific entry of forecast_sales
     */
    @GET
    @Path("/product_main_group/{productMainGroup}/region/{region}/period/{period}/sales_type/{salesType}/entry_type/{entryType}/plan_year/{planYear}/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getForecastFixedCost(
            @PathParam("productMainGroup") String productMainGroup,
            @PathParam("region") String region,
            @PathParam("period") int period,
            @PathParam("salesType") String salesType,
            @PathParam("entryType") String entryType,
            @PathParam("planYear") int planYear) {

        if (validatePeriod(period) && validateSalesType(salesType) && validateEntryType(entryType)) {
            Period planPeriodTo = new Period(planYear).incrementMultipleTimes(OutputDataType.getNumberOfMonths());
            return Response.ok(salesService.getForecastSales(productMainGroup, region, new Period(period),
                    SalesType.valueOf(salesType.toUpperCase()), EntryType.valueOf(entryType.toUpperCase()),
                    new Period(planYear), planPeriodTo)).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request: Parameters are not valid").build();
        }
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
            return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request: Parameters are not valid").build();
        }
    }
}