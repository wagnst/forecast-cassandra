package fourschlag.services.web.ws;

import fourschlag.entities.types.EntryType;
import fourschlag.entities.types.Period;
import fourschlag.services.data.service.FixedCostsService;
import fourschlag.services.db.CassandraConnection;
import fourschlag.services.db.ClusterEndpoints;
import fourschlag.services.db.ConnectionPool;
import fourschlag.services.db.KeyspaceNames;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static fourschlag.services.web.ws.ParameterUtil.*;

/**
 * ForecastFixedCostsWS offers web service to get plain fixed costs data from a
 * database
 */
@Path("{keyspace}/fixedcosts")
public class ForecastFixedCostsWS {
    private FixedCostsService fixedCostsService;

    /**
     * Constructor to initialize the database connection and services
     */
    public ForecastFixedCostsWS(@PathParam("keyspace") String keyspace) {
        CassandraConnection connection = ConnectionPool.getConnection(ClusterEndpoints.NODE1, KeyspaceNames.valueOf(keyspace.toUpperCase()), true);
        fixedCostsService = new FixedCostsService(connection);
    }

    /**
     * Method returns all entries from table forecast_fixed_costs
     * as a valid WebService
     *
     * @return all entries from forecast_fixed_costs
     */
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getForecastFixedCosts() {
        return Response.ok(fixedCostsService.getAllForecastFixedCosts()).build();
    }

    /**
     * Method returns a specific entry from table forecast_fixed_costs
     * as a valid WebService
     *
     * @param sbu           parameter for sbu
     * @param subregion     parameter for subregion
     * @param period        parameter for period
     * @param entryType     parameter for entryType
     * @param planPeriodInt parameter for planPeriod
     *
     * @return a specific entry of forecast_fixed_costs
     */
    @GET
    @Path("sbu/{sbu}/subregion/{subregion}/period/{period}/entry_type/{entryType}/plan_period/{planPeriod}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOneForecastFixedCost(
            @PathParam("sbu") String sbu,
            @PathParam("subregion") String subregion,
            @PathParam("period") int period,
            @PathParam("entryType") String entryType,
            @PathParam("planPeriod") int planPeriodInt) {

        if (validatePeriod(period) && validateEntryType(entryType) && validatePeriod(planPeriodInt)) {
            Period currentPeriod = new Period(period);
            Period planPeriod = new Period(planPeriodInt);

            return Response.ok(fixedCostsService.getSpecificForecastFixedCosts(sbu, subregion, currentPeriod,
                    EntryType.getEntryTypeByString(entryType), planPeriod)).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request: Parameters are not valid").build();
        }
    }

    /**
     * Method returns multiple entries from table forecast_fixed_costs
     * as a valid WebService
     *
     * @param sbu       parameter for sbu
     * @param subregion parameter for subregion
     * @param period    parameter for period
     * @param entryType parameter for entryType
     * @param planYear  parameter for planPeriod from
     *
     * @return multiple entries of forecast_fixed_costs
     */
    @GET
    @Path("sbu/{sbu}/subregion/{subregion}/period/{period}/entry_type/{entryType}/plan_year/{planYear}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMultipleForecastFixedCosts(
            @PathParam("sbu") String sbu,
            @PathParam("subregion") String subregion,
            @PathParam("period") int period,
            @PathParam("entryType") String entryType,
            @PathParam("planYear") int planYear) {

        if (validatePeriod(period) && validateEntryType(entryType) && validatePlanYear(planYear)) {
            Period currentPeriod = new Period(period);
            Period planPeriodFrom = Period.getPeriodByYear(planYear);
            Period planPeriodTo = ParameterUtil.calculateToPeriod(planPeriodFrom);

            return Response.ok(fixedCostsService.getMultipleForecastFixedCosts(
                    subregion,
                    sbu,
                    currentPeriod,
                    EntryType.getEntryTypeByString(entryType),
                    planPeriodFrom,
                    planPeriodTo)).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request: Parameters are not valid").build();
        }
    }

    /**
     * Method returns multiple entries from table forecast_fixed_costs
     * as a valid WebService. Just taking care of budget values
     *
     * @param sbu       parameter for sbu
     * @param subregion parameter for subregion
     * @param planYear  parameter for planYear from
     *
     * @return multiple entries of forecast_fixed_costs
     */
    @GET
    @Path("sbu/{sbu}/subregion/{subregion}/entry_type/budget/plan_year/{planYear}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBudgetForecastFixedCosts(
            @PathParam("sbu") String sbu,
            @PathParam("subregion") String subregion,
            @PathParam("planYear") int planYear) {
        if (validatePlanYear(planYear)) {
            Period planPeriodFrom = Period.getPeriodByYear(planYear);
            Period planPeriodTo = ParameterUtil.calculateToPeriod(planPeriodFrom);

            return Response.ok(fixedCostsService.getBudgetForecastFixedCosts(
                    subregion,
                    sbu,
                    planPeriodFrom,
                    planPeriodTo)).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request: Parameters are not valid").build();
        }
    }

    /**
     * This method allows data modification or creation of ForecastFixedCosts
     * related table
     *
     * @return HTTP Response OK or BAD_REQUEST
     */
    @POST
    @Path("/")
    public Response createForecastFixedCosts(
            @FormParam("sbu") String sbu,
            @FormParam("subregion") String subregion,
            @FormParam("fix_pre_man_cost") double fixPreManCost,
            @FormParam("ship_cost") double shipCost,
            @FormParam("sell_cost") double sellCost,
            @FormParam("diff_act_pre_man_cost") double diffActPreManCost,
            @FormParam("idle_equip_cost") double idleEquipCost,
            @FormParam("rd_cost") double rdCost,
            @FormParam("admin_cost_bu") double adminCostBu,
            @FormParam("admin_cost_od") double adminCostOd,
            @FormParam("admin_cost_company") double adminCostCompany,
            @FormParam("other_op_cost_bu") double otherOpCostBu,
            @FormParam("other_op_cost_od") double otherOpCostOd,
            @FormParam("other_op_cost_company") double otherOpCostCompany,
            @FormParam("spec_items") double specItems,
            @FormParam("provisions") double provisions,
            @FormParam("currency_gains") double currencyGains,
            @FormParam("val_adjust_inventories") double valAdjustInventories,
            @FormParam("other_fix_cost") double otherFixCost,
            @FormParam("depreciation") double depreciation,
            @FormParam("cap_cost") double capCost,
            @FormParam("equitiy_income") double equitiyIncome,
            @FormParam("topdown_adjust_fix_costs") double topdownAdjustFixCosts,
            @FormParam("plan_period") int planPeriod,
            @FormParam("status") String status,
            @FormParam("usercomment") String usercomment,
            @FormParam("entry_type") String entryType,
            @FormParam("period") int period,
            @FormParam("region") String region,
            @FormParam("currency") String currency,
            @FormParam("user_id") String userId,
            @FormParam("entry_ts") String entryTs) {

        if (validateCurrency(currency) && validateEntryType(entryType) && validatePeriod(planPeriod) && validatePeriod(period)) {

            Period tempPlanPeriod = new Period(planPeriod);
            Period tempPeriod = new Period(period);

            if (fixedCostsService.setForecastFixedCosts(
                    sbu, subregion, fixPreManCost, shipCost, sellCost, diffActPreManCost, idleEquipCost, rdCost, adminCostBu, adminCostOd, adminCostCompany,
                    otherOpCostBu, otherOpCostOd, otherOpCostCompany, specItems, provisions, currencyGains, valAdjustInventories, otherFixCost, depreciation, capCost, equitiyIncome, topdownAdjustFixCosts,
                    tempPlanPeriod, status, usercomment, entryType, tempPeriod, region, currency, userId, entryTs)) {
                return Response.status(Response.Status.OK).entity("Request completed. The data has been successfully inserted or updated.").build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request: Parameters are not valid").build();
    }

    /**
     * This method allows data deletion of ForecastFixedCosts related table
     *
     * @return HTTP Response OK or BAD_REQUEST
     */
    @DELETE
    @Path("sbu/{sbu}/subregion/{subregion}/period/{period}/entry_type/{entryType}/plan_period/{planPeriod}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteForecastFixedCosts(
            @PathParam("sbu") String sbu,
            @PathParam("subregion") String subregion,
            @PathParam("period") int period,
            @PathParam("entryType") String entryType,
            @PathParam("planPeriod") int planPeriod
    ) {
        if (validatePeriod(period) && validatePeriod(planPeriod) && validateEntryType(entryType)) {

            Period tempPlanPeriod = new Period(planPeriod);
            Period tempPeriod = new Period(period);

            if (fixedCostsService.deleteForecastFixedCosts(sbu, subregion, tempPeriod, entryType, tempPlanPeriod)) {
                return Response.status(Response.Status.OK).entity("Request completed. The data has been successfully removed.").build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request: Parameters are not valid").build();
    }
}