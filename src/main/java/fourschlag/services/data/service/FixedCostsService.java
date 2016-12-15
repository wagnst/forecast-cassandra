package fourschlag.services.data.service;

import fourschlag.entities.types.Currency;
import fourschlag.entities.types.OutputDataType;
import fourschlag.entities.types.Period;
import fourschlag.services.data.requests.ExchangeRateRequest;
import fourschlag.services.data.requests.FixedCostsRequest;
import fourschlag.services.data.requests.OrgStructureAndRegionRequest;
import fourschlag.services.data.requests.kpi.FixedCostsKpiRequest;
import fourschlag.services.db.JpaConnection;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Extends Service. Provides functionality to get fixed costs KPIs
 */
public class FixedCostsService extends Service {

    public FixedCostsService(JpaConnection connection) {
        super(connection);
    }

    /**
     * Calculates all Fixed Costs KPIs for a time span (planYear) and from certain
     * point of view (currentPeriod).
     *
     * @param planPeriod    Indicates the time span for which the KPIs are
     *                      supposed to be queried
     * @param currentPeriod The point of view in time from which the data is
     *                      supposed to be looked at
     * @param toCurrency    The desired output currency
     * @return stream of OutputDataTypes that contain all KPIs for the given
     * parameters
     */
    public Stream<OutputDataType> getFixedCostsKpis(Period planPeriod, Period currentPeriod, Currency toCurrency) {
        /* Prepare result stream that will be returned later */
        Stream<OutputDataType> resultStream;

        /* Create instance of ExchangeRateRequest with the desired currency */
        ExchangeRateRequest exchangeRates = new ExchangeRateRequest(getConnection(), toCurrency);

        /* Create Request to be able to retrieve all distinct regions and products from different tables */
        OrgStructureAndRegionRequest orgAndRegionRequest = new OrgStructureAndRegionRequest(getConnection());

        Map<String, Set<String>> sbuAndSubregions = new FixedCostsRequest(getConnection()).getSubregionsAndSbuFromFixedCosts();

        /* Nested for-loops that iterate over all sbus and subregions. For every combination a FixedCostsKpiRequest is created */
        resultStream = sbuAndSubregions.keySet().stream().parallel()
                .flatMap(sbu -> sbuAndSubregions.get(sbu).stream()
                        .flatMap(subregion -> new FixedCostsKpiRequest(getConnection(), sbu, planPeriod, currentPeriod, subregion,
                                exchangeRates, orgAndRegionRequest).calculateKpis()));


        System.out.println("Finished with Fixed Costs");
        /* Finally the result stream is returned */
        return resultStream;
    }
}
