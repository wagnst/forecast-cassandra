package fourschlag.services.data.service;

import fourschlag.entities.types.Currency;
import fourschlag.entities.types.OutputDataType;
import fourschlag.entities.types.Period;
import fourschlag.entities.types.SalesType;
import fourschlag.services.data.requests.ExchangeRateRequest;
import fourschlag.services.data.requests.OrgStructureAndRegionRequest;
import fourschlag.services.data.requests.SalesRequest;
import fourschlag.services.data.requests.kpi.SalesKpiRequest;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Extends Service. Provides functionality to get sales KPIs
 */
public class SalesService extends Service {

    /**
     * Calculates all Sales KPIs for a time span (planYear) and from certain
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
    public Stream<OutputDataType> getSalesKPIs(Period planPeriod, Period currentPeriod, Currency toCurrency) {
        /* Prepare result stream that will be returned later */
        Stream<OutputDataType> resultStream;
        /* Create instance of ExchangeRateRequest with the desired currency */
        ExchangeRateRequest exchangeRates = new ExchangeRateRequest(toCurrency);

        /* Create Request to be able to retrieve all distinct regions and products from different tables */
        OrgStructureAndRegionRequest orgAndRegionRequest = new OrgStructureAndRegionRequest();

        Map<String, Set<String>> productAndRegions = new SalesRequest().getPmgAndRegionsFromSales();

        /* Nested for-loops implemented as parallel streams to iterate over all combinations of PMG, regions and sales types */
        resultStream = productAndRegions.keySet().stream().parallel()
                .flatMap(product -> productAndRegions.get(product).stream()
                        .flatMap(region -> Arrays.stream(SalesType.values())
                                .flatMap(salesType -> new SalesKpiRequest(product, planPeriod, currentPeriod, region,
                                        salesType, exchangeRates, orgAndRegionRequest).calculateKpis())));

        /* Finally the result stream will be returned */
        return resultStream;
    }
}