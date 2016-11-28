package fourschlag.services.data;

import fourschlag.entities.types.Currency;
import fourschlag.entities.types.OutputDataType;
import fourschlag.entities.types.Period;
import fourschlag.services.data.requests.ExchangeRateRequest;
import fourschlag.services.data.requests.FixedCostsRequest;
import fourschlag.services.data.requests.OrgStructureAndRegionRequest;
import fourschlag.services.db.CassandraConnection;

import java.util.Set;
import java.util.stream.Stream;

public class FixedCostsService extends Service {

    /**
     * Constructor
     *
     * @param connection Cassandra connection that is supposed to be used
     */
    public FixedCostsService(CassandraConnection connection) {super(connection);}

    /**
     * Calculates all Fixed Costs KPIs for a time span (planYear) and from certain
     * point of view (currentPeriod).
     *
     * @param planYear         Indicates the time span for which the KPIs are
     *                         supposed to be queried
     * @param currentPeriodInt The point of view in time from which the data is
     *                         supposed to be looked at
     * @param toCurrency       The desired output currency
     * @return stream of OutputDataTypes that contain all KPIs for the given
     * parameters
     */
    public Stream<OutputDataType> getFixedCostsKpis(int planYear, int currentPeriodInt, Currency toCurrency) {
        /* Prepare result stream that will be returned later */
        Stream<OutputDataType> resultStream;

        /* Create instance of ExchangeRateRequest with the desired currency */
        ExchangeRateRequest exchangeRates = new ExchangeRateRequest(getConnection(), toCurrency);

        /* Create Request to be able to retrieve all distinct regions and products from different tables */
        OrgStructureAndRegionRequest orgAndRegionRequest = new OrgStructureAndRegionRequest(getConnection());

        /* Get all of the subregions from the fixed costs tables --> Fixed Costs are identified by the subregion and sbu */
        Set<String> subregions = orgAndRegionRequest.getSubregionsAsSetFromFixedCosts();
        /* Get all of the SBUs from the fixed costs tables */
        Set<String> sbus = orgAndRegionRequest.getSbuAsSetFromFixedCost();

        /* Create instance of Period with the given int value */
        Period currentPeriod = new Period(currentPeriodInt);

        /* Nested for-loops that iterate over all sbus and subregions. For every combination a FixedCostsRequest is created */
        resultStream = sbus.stream().parallel()
                .flatMap(sbu -> subregions.stream()
                        .flatMap(subregion -> new FixedCostsRequest(getConnection(), sbu, planYear, currentPeriod,
                                subregion, exchangeRates, orgAndRegionRequest).calculateKpis()));

        /* Finally the result stream is returned */
        return resultStream;
    }
}
