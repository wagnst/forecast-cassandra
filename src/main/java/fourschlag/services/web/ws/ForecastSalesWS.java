
package fourschlag.services.web.ws;

import fourschlag.entities.types.EntryType;
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

import static fourschlag.services.web.ws.ParameterUtil.*;

/**
 * ForecastSalesWS offers web service to get KPIs from a database
 */
@Path("{keyspace}/sales")
public class ForecastSalesWS {
    private SalesService salesService;

    /**
     * Constructor to initialize the database connection and services
     */
    public ForecastSalesWS(@PathParam("keyspace") String keyspace) {
        CassandraConnection connection = ConnectionPool.getConnection(ClusterEndpoints.NODE1, KeyspaceNames.valueOf(keyspace.toUpperCase()), true);
        salesService = new SalesService(connection);
    }

    /**
     * Method returns all entries from table forecast_sales
     * as a valid WebService
     *
     * @return all entries from forecast_sales
     */
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getForecastSales() {
        return Response.ok(salesService.getAllForecastSales()).build();
    }

    /**
     * Method returns a specific entry from table forecast_sales
     * as a valid WebService
     *
     * @return a specific entry of forecast_sales
     */
    @GET
    @Path("product_main_group/{productMainGroup}/region/{region}/period/{period}/sales_type/{salesType}/plan_period/{planPeriod}/entry_type/{entryType}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOneForecastSales(
            @PathParam("productMainGroup") String productMainGroup,
            @PathParam("region") String region,
            @PathParam("period") int period,
            @PathParam("salesType") String salesType,
            @PathParam("planPeriod") int planPeriodInt,
            @PathParam("entryType") String entryType) {

        if (validatePeriod(period) && validateSalesType(salesType) && validatePeriod(planPeriodInt) && validateEntryType(entryType)) {
            Period currentPeriod = new Period(period);
            Period planPeriod = new Period(planPeriodInt);
            return Response.ok(salesService.getSpecificForecastSales(productMainGroup, region, currentPeriod,
                    SalesType.getSalesTypeByString(salesType), planPeriod, EntryType.getEntryTypeByString(entryType))).build();
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
    @Path("product_main_group/{productMainGroup}/region/{region}/period/{period}/sales_type/{salesType}/entry_type/{entryType}/plan_year/{planYear}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getForecastSales(
            @PathParam("productMainGroup") String productMainGroup,
            @PathParam("region") String region,
            @PathParam("period") int period,
            @PathParam("salesType") String salesType,
            @PathParam("entryType") String entryType,
            @PathParam("planYear") int planYear) {

        if (validatePeriod(period) && validateSalesType(salesType) && validateEntryType(entryType) && validatePlanYear(planYear)) {

            Period currentPeriod = new Period(period);
            Period planPeriodFrom = Period.getPeriodByYear(planYear);
            Period planPeriodTo = ParameterUtil.calculateToPeriod(planPeriodFrom);

            return Response.ok(salesService.getMultipleForecastSales(productMainGroup, region, currentPeriod,
                    SalesType.getSalesTypeByString(salesType), EntryType.getEntryTypeByString(entryType),
                    planPeriodFrom, planPeriodTo)).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request: Parameters are not valid").build();
        }
    }

    /**
     * Method returns multiple entries from table forecast_sales
     * as a valid WebService. Just taking care of budget values
     *
     * @param productMainGroup parameter for sbu
     * @param region           parameter for subregion
     * @param salesType        parameter for Sales Type
     * @param planYear         parameter for planYear from
     *
     * @return multiple entries of forecast_fixed_costs
     */
    @GET
    @Path("product_main_group/{productMainGroup}/region/{region}/sales_type/{salesType}/entry_type/budget/plan_year/{planYear}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBudgetForecastSales(
            @PathParam("productMainGroup") String productMainGroup,
            @PathParam("region") String region,
            @PathParam("salesType") String salesType,
            @PathParam("planYear") int planYear) {
        if (validatePlanYear(planYear)) {
            Period planPeriodFrom = Period.getPeriodByYear(planYear);
            Period planPeriodTo = ParameterUtil.calculateToPeriod(planPeriodFrom);

            return Response.ok(salesService.getBudgetForecastSales(
                    productMainGroup,
                    region,
                    SalesType.getSalesTypeByString(salesType),
                    planPeriodFrom,
                    planPeriodTo)).build();
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
    @Path("/")
    public Response createForecastSales(
            @FormParam("topdown_adjust_sales_volumes") double topdownAdjustSalesVolumes,
            @FormParam("topdown_adjust_net_sales") double topdownAdjustNetSales,
            @FormParam("topdown_adjust_cm1") double topdownAdjustCm1,
            @FormParam("plan_period") int planPeriod,
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
            @FormParam("currency") String currency,
            @FormParam("userid") String userId,
            @FormParam("entry_ts") String entryTs) {

        if (validateCurrency(currency) && validateEntryType(entryType) && validatePeriod(planPeriod) && validatePeriod(period)) {

            Period tempPlanPeriod = new Period(planPeriod);
            Period tempPeriod = new Period(period);

            if (salesService.setForecastSales(
                    topdownAdjustSalesVolumes, topdownAdjustNetSales, topdownAdjustCm1, tempPlanPeriod, entryType,
                    status, usercomment, productMainGroup, salesType, salesVolumes, netSales, cm1, tempPeriod, region, currency, userId, entryTs)) {
                return Response.status(Response.Status.OK).entity("Request completed. The data has been successfully inserted or updated.").build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request: Parameters are not valid").build();
    }

    /**
     * This method allows data deletion of ForecastSales related table
     *
     * @return HTTP Response OK or BAD_REQUEST
     */
    @DELETE
    @Path("product_main_group/{productMainGroup}/region/{region}/period/{period}/sales_type/{salesType}/entry_type/{entryType}/plan_period/{planPeriod}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteForecastSales(
            @PathParam("productMainGroup") String productMainGroup,
            @PathParam("region") String region,
            @PathParam("period") int period,
            @PathParam("salesType") String salesType,
            @PathParam("planPeriod") int planPeriod,
            @PathParam("entryType") String entryType
    ) {
        if (validatePeriod(period) && validateSalesType(salesType) && validatePeriod(planPeriod) && validateEntryType(entryType)) {

            Period tempPlanPeriod = new Period(planPeriod);
            Period tempPeriod = new Period(period);

            if (salesService.deleteForecastSales(productMainGroup, region, tempPeriod, salesType, tempPlanPeriod, entryType)) {
                return Response.status(Response.Status.OK).entity("Request completed. The data has been successfully removed.").build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request: Parameters are not valid").build();
    }
}