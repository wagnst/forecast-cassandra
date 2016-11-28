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

    public FixedCostsService(CassandraConnection connection) {super(connection);}

    public Stream<OutputDataType> getFixedCostsKpis(int planYear, int currentPeriodInt, Currency toCurrency) {
        /* Prepare result list that will be returned later */
        Stream<OutputDataType> resultStream;

        /* Create instance of ExchangeRateRequest with the desired currency */
        ExchangeRateRequest exchangeRates = new ExchangeRateRequest(getConnection(), toCurrency);

        OrgStructureAndRegionRequest orgAndRegionRequest = new OrgStructureAndRegionRequest(getConnection());

        /* Get all of the regions from the region table */
        Set<String> subregions = orgAndRegionRequest.getSubregionsAsSetFromFixedCosts();
        /* Get all of the Product Main Groups from the OrgStructure table*/
        Set<String> sbus = orgAndRegionRequest.getSbuAsSetFromFixedCost();

        /* Create instance of Period with the given int value */
        Period currentPeriod = new Period(currentPeriodInt);

        resultStream = sbus.stream().parallel()
                .flatMap(sbu -> subregions.stream()
                        .flatMap(subregion -> new FixedCostsRequest(getConnection(), sbu, planYear, currentPeriod,
                                subregion, exchangeRates, orgAndRegionRequest).calculateKpis()));

        return resultStream;
    }
}
