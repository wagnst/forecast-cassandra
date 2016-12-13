package fourschlag.services.data.service;

import fourschlag.entities.tables.kpi.sales.ForecastSalesEntity;
import fourschlag.entities.types.*;
import fourschlag.services.data.requests.ExchangeRateRequest;
import fourschlag.services.data.requests.OrgStructureAndRegionRequest;
import fourschlag.services.data.requests.SalesRequest;
import fourschlag.services.data.requests.kpi.SalesKpiRequest;
import fourschlag.services.db.CassandraConnection;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
        ExchangeRateRequest exchangeRates = new ExchangeRateRequest(getConnection(), toCurrency);

        /* Create Request to be able to retrieve all distinct regions and products from different tables */
        OrgStructureAndRegionRequest orgAndRegionRequest = new OrgStructureAndRegionRequest(getConnection());

        Map<String, Set<String>> productAndRegions = new SalesRequest(getConnection()).getPmgAndRegions();

        /* Nested for-loops implemented as parallel streams to iterate over all combinations of PMG, regions and sales types */
        resultStream = productAndRegions.keySet().stream().parallel()
                .flatMap(product -> productAndRegions.get(product).stream()
                        .flatMap(region -> Arrays.stream(SalesType.values())
                                .flatMap(salesType -> new SalesKpiRequest(getConnection(),
                                        product, planPeriod, currentPeriod, region, salesType,
                                        exchangeRates, orgAndRegionRequest).calculateKpis())));

        /* Finally the result stream will be returned */
        return resultStream;
    }


    /**
     * @return a list of all ForecastSalesEntities
     */
    public List<ForecastSalesEntity> getAllForecastSales() {
        return new SalesRequest(getConnection()).getAllForecastSales();
    }


    /**
     * @return a specific ForecastSalesEntity
     */
    public ForecastSalesEntity getSpecificForecastSales(String productMainGroup, String region, Period period,
                                                        SalesType salesType, Period planPeriod, EntryType entryType) {
        return new SalesRequest(getConnection()).getSpecificForecastSales(productMainGroup, region, period, salesType,
                planPeriod, entryType);
    }

    /**
     * @return a list of specific ForecastSalesEntities
     */
    public List<ForecastSalesEntity> getMultipleForecastSales(String productMainGroup, String region, Period period,
                                                              SalesType salesType, EntryType entryType, Period planPeriodFrom,
                                                              Period planPeriodTo) {

        return new SalesRequest(getConnection()).getMultipleForecastSales(productMainGroup, region, period, salesType,
                entryType, planPeriodFrom, planPeriodTo);
    }

    public List<ForecastSalesEntity> getBudgetForecastSales(String productMainGroup, String region, SalesType salesType,
                                                            Period planPeriodFrom, Period planPeriodTo) {

        return new SalesRequest(getConnection()).getBudgetForecastSales(productMainGroup, region, salesType,
                planPeriodFrom, planPeriodTo);
    }

    /**
     * Method sets new values or records to forecast_sales table
     *
     * @return boolean value if action was successful or not
     */
    public boolean setForecastSales(double topdownAdjustSalesVolumes, double topdownAdjustNetSales, double topdownAdjustCm1, Period planPeriod, String entryType, String status, String usercomment, String productMainGroup, String salesType,
                                    double salesVolumes, double netSales, double cm1, Period period, String region, String currency, String userId, String entryTs) {

        return new SalesRequest(getConnection()).setForecastSales(topdownAdjustSalesVolumes, topdownAdjustNetSales, topdownAdjustCm1, planPeriod,
                entryType, status, usercomment, productMainGroup, salesType, salesVolumes, netSales, cm1, period, region, currency, userId, entryTs);
    }
}