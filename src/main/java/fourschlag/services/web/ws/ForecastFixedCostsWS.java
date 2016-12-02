package fourschlag.services.web.ws;

import fourschlag.services.data.service.FixedCostsService;
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
@Path("/fixedcosts")
public class ForecastFixedCostsWS {
    private CassandraConnection connection = ConnectionPool.getConnection(ClusterEndpoints.NODE1, KeyspaceNames.ORIGINAL_VERSION, true);
    private FixedCostsService fixedCostsService = new FixedCostsService(connection);

    /**
     * Method returns all entries from table forecast_fixed_costs
     * as a valid WebService
     *
     * @return all entries from forecast_fixed_costs
     */
    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getForecastFixedCosts() {
        return Response.ok(fixedCostsService.getForecastFixedCosts()).build();
    }

    /**
     * Method returns a specifiy entry from table forecast_fixed_costs
     * as a valid WebService
     *
     * @param sbu        parameter for sbu
     * @param subregion  parameter for subregion
     * @param period     parameter for period
     * @param entryType  parameter for entryType
     * @param planPeriod parameter for planPeriod
     *
     * @return a specific entry of forecast_fixed_costs
     */
    @GET
    @Path("/{sbu}/{subregion}/{period}/{entryType}/{planPeriod}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getForecastFixedCost(
            @PathParam("sbu") String sbu,
            @PathParam("subregion") String subregion,
            @PathParam("period") int period,
            @PathParam("entryType") String entryType,
            @PathParam("planPeriod") int planPeriod) {
        return Response.ok(fixedCostsService.getForecastFixedCosts(sbu, subregion, period, entryType, planPeriod)).build();
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
            @FormParam("deprecation") double deprecation,
            @FormParam("cap_cost") double capCost,
            @FormParam("equitiy_income") double equitiyIncome,
            @FormParam("topdown_adjust_fix_costs") double topdownAdjustFixCosts,
            @FormParam("plan_period") int planPeriod,
            @FormParam("plan_year") int planYear,
            @FormParam("plan_half_year") int planHalfYear,
            @FormParam("plan_quarter") int planQuarter,
            @FormParam("plan_month") int planMonth,
            @FormParam("status") String status,
            @FormParam("usercomment") String usercomment,
            @FormParam("entry_type") String entryType,
            @FormParam("period") int period,
            @FormParam("region") String region,
            @FormParam("period_year") int periodYear,
            @FormParam("period_month") int periodMonth,
            @FormParam("currency") String currency,
            @FormParam("user_id") String userId,
            @FormParam("entry_ts") String entryTs) {
        if (fixedCostsService.setForecastFixedCosts(
                sbu, subregion, fixPreManCost, shipCost, sellCost, diffActPreManCost, idleEquipCost, rdCost, adminCostBu, adminCostOd, adminCostCompany,
                otherOpCostBu, otherOpCostOd, otherOpCostCompany, specItems, provisions, currencyGains, valAdjustInventories, otherFixCost, deprecation, capCost, equitiyIncome, topdownAdjustFixCosts,
                planPeriod, planYear, planHalfYear, planQuarter, planMonth, status, usercomment, entryType, period, region, periodYear, periodMonth, currency, userId, entryTs)) {
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
