package fourschlag.services.web.ws;

import fourschlag.entities.types.Currency;
import fourschlag.entities.types.OutputDataType;
import fourschlag.entities.types.Period;
import fourschlag.entities.types.comparators.OutputDataTypeComparator;
import fourschlag.services.data.service.FixedCostsService;
import fourschlag.services.data.service.SalesService;
import fourschlag.services.db.CassandraConnection;
import fourschlag.services.db.ClusterEndpoints;
import fourschlag.services.db.ConnectionPool;
import fourschlag.services.db.KeyspaceNames;
import fourschlag.services.web.Params;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static fourschlag.services.web.ws.ParameterUtil.*;

/**
 * ForecastWS offers web service to get KPIs from a database
 */
@Path("/{keyspace}/forecast/")
public class ForecastWS {

    private CassandraConnection connection;
    private SalesService salesService;
    private FixedCostsService fixedCostsService;

    /**
     * Constructor to initialize the database connection and services
     */
    public ForecastWS(@PathParam("keyspace") String keyspace) {
        connection = ConnectionPool.getConnection(ClusterEndpoints.NODE1, KeyspaceNames.valueOf(keyspace.toUpperCase()), true);
        salesService = new SalesService(connection);
        fixedCostsService = new FixedCostsService(connection);
    }

    /* TODO: Maybe close session each time, but not connection */
    /* TODO: Create Connection pool and remove the connection from this WS */

    /**
     * This method collects all to be calculated forecast KPI's in
     * the different service classes
     *
     * @param currency desired currency parameter
     * @param planYear desired plan-year parameter
     * @param period   desired period parameter
     * @return WS Response as JSON containing all calculated KPI's
     */
    @GET
    @Path("/period/{period}/planyear/{planyear}/currency/{currency}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getKPIs(
            @PathParam("currency") String currency,
            @PathParam("planyear") int planYear,
            @PathParam("period") int period) {

        //TODO: period must be the present or the past, but must not be the future --> Not sure..ask Henrik

        if (validateCurrency(currency) && validatePlanYear(planYear) && validatePeriod(period)) {
            Currency curr = Currency.getCurrencyByAbbreviation(currency);

            Period currentPeriod = new Period(period);
            Period planPeriod = Period.getPeriodByYear(planYear);

            /* Get all Sales KPIs and save them to a stream */
            Stream<OutputDataType> salesKpis = salesService.getSalesKPIs(planPeriod, currentPeriod, curr);
            /* Also save the Fixed Costs KPIs to a stream */
            Stream<OutputDataType> fixedCostsKpis = fixedCostsService.getFixedCostsKpis(planPeriod, currentPeriod, curr);
            /* Combine both streams to one */
            List<OutputDataType> resultList = Stream.concat(salesKpis, fixedCostsKpis)
                /* Sort the whole stream */
                    .sorted(new OutputDataTypeComparator())
                /* Convert the stream to a List */
                    .collect(Collectors.toList());

        /* Close both streams */
            salesKpis.close();
            fixedCostsKpis.close();
        /* Return the result list with a code 200 */
            return Response.ok(resultList, Params.MEDIATYPE).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request: Parameters are not valid").build();
        }
    }

    /**
     * This method collects only sales dependent forecast KPI's in
     * the different service classes
     *
     * @param currency desired currency parameter
     * @param planYear desired plan-year parameter
     * @param period   desired period parameter
     * @return WS Response as JSON containing all calculated KPI's
     */
    @GET
    @Path("/period/{period}/planyear/{planyear}/currency/{currency}/sales")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSalesKPIs(
            @PathParam("currency") String currency,
            @PathParam("planyear") int planYear,
            @PathParam("period") int period) {

        //TODO: period must be the present or the past, but must not be the future --> Not sure..ask Henrik

        if (validateCurrency(currency) && validatePlanYear(planYear) && validatePeriod(period)) {
            Currency curr = Currency.getCurrencyByAbbreviation(currency);
            Period currentPeriod = new Period(period);
            Period planPeriod = Period.getPeriodByYear(planYear);

            /* Get all Sales KPIs and save them to a stream */
            List<OutputDataType> resultList = salesService.getSalesKPIs(planPeriod, currentPeriod, curr)
                    /* Sort the whole stream */
                    .sorted(new OutputDataTypeComparator())
                    /* Convert the stream to a List */
                    .collect(Collectors.toList());

            /* Return the result list with a code 200 */
            return Response.ok(resultList, Params.MEDIATYPE).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request: Parameters are not valid").build();
        }
    }

    /**
     * This method collects fixed costs related forecast KPI's in
     * the different service classes
     *
     * @param currency desired currency parameter
     * @param planYear desired plan-year parameter
     * @param period   desired period parameter
     * @return WS Response as JSON containing all calculated KPI's
     */
    @GET
    @Path("/period/{period}/planyear/{planyear}/currency/{currency}/fixedcosts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFixedCostsKPIs(
            @PathParam("currency") String currency,
            @PathParam("planyear") int planYear,
            @PathParam("period") int period) {

        //TODO: period must be the present or the past, but must not be the future --> Not sure..ask Henrik

        if (validateCurrency(currency) && validatePlanYear(planYear) && validatePeriod(period)) {
            Currency curr = Currency.getCurrencyByAbbreviation(currency);
            Period currentPeriod = new Period(period);
            Period planPeriod = Period.getPeriodByYear(planYear);

            /* Get all Fixed-Costs KPIs and save them to a stream */
            List<OutputDataType> resultList = fixedCostsService.getFixedCostsKpis(planPeriod, currentPeriod, curr)
                    /* Sort the whole stream */
                    .sorted(new OutputDataTypeComparator())
                    /* Convert the stream to a List */
                    .collect(Collectors.toList());

            /* Return the result list with a code 200 */
            return Response.ok(resultList, Params.MEDIATYPE).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request: Parameters are not valid").build();
        }
    }
}