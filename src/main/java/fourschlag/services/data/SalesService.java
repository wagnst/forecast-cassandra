package fourschlag.services.data;

import fourschlag.entities.types.OutputDataType;
import fourschlag.entities.types.Period;
import fourschlag.entities.types.SalesType;
import fourschlag.services.data.requests.ExchangeRateRequest;
import fourschlag.services.data.requests.OrgStructureAndRegionRequest;
import fourschlag.services.data.requests.SalesRequest;
import fourschlag.services.db.CassandraConnection;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Extends Service. Provides functionality to get sales KPIs
 */
public class SalesService extends Service {

    /**
     * Constructor for SalesService
     *
     * @param connection Cassandra connection that is supposed to be used
     */
    public SalesService(CassandraConnection connection) {
        super(connection);
    }

    /**
     * Calculates all Sales KPIs for a time span (planYear) and from certain
     * point of view (currentPeriod).
     *
     * @param planYear         Indicates the time span for which the KPIs are
     *                         supposed to be queried
     * @param currentPeriodInt The point of view in time from which the data is
     *                         supposed to be looked at
     * @param toCurrency       The desired output currency
     *
     * @return List of the OutputDataTypes that contain all KPIs for the given
     * parameters
     */
    public Stream<OutputDataType> getSalesKPIs(int planYear, int currentPeriodInt, String toCurrency) {
        /* Prepare result list that will be returned later */
        Stream<OutputDataType> resultStream;

        /* Create instance of ExchangeRateRequest with the desired currency */
        ExchangeRateRequest exchangeRates = new ExchangeRateRequest(getConnection(), toCurrency);

        OrgStructureAndRegionRequest orgAndRegionRequest = new OrgStructureAndRegionRequest(getConnection());

        /* Get all of the regions from the region table */
        Set<String> regions = orgAndRegionRequest.getRegionsAsSetFromSales();
        /* Get all of the Product Main Groups from the OrgStructure table*/
        Set<String> products = orgAndRegionRequest.getProductMainGroupsAsSetFromSales();

        /* Create instance of Period with the given int value */
        Period currentPeriod = new Period(currentPeriodInt);

        /* Nested for-loops implemented as parallel streams to iterate over all combinations of PMG, regions and sales types */
        resultStream = products.stream().parallel()
                .flatMap(product -> regions.stream()
                        .flatMap(region -> Arrays.stream(SalesType.values())
                                .flatMap(salesType -> new SalesRequest(getConnection(),
                                        product, planYear, currentPeriod, region, salesType,
                                        exchangeRates, orgAndRegionRequest).calculateKpis())));

        /* Finally the result list will be returned */
        return resultStream;
    }
}