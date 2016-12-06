package fourschlag.services.data.service;

import fourschlag.entities.tables.kpi.fixedcosts.ForecastFixedCostsEntity;
import fourschlag.entities.types.Currency;
import fourschlag.entities.types.OutputDataType;
import fourschlag.entities.types.Period;
import fourschlag.services.data.requests.ExchangeRateRequest;
import fourschlag.services.data.requests.OrgStructureAndRegionRequest;
import fourschlag.services.data.requests.kpi.FixedCostsRequest;
import fourschlag.services.data.requests.kpi.manipulation.FixedCostsManipulationRequest;
import fourschlag.services.db.CassandraConnection;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Extends Service. Provides functionality to get fixed costs KPIs
 */
public class FixedCostsService extends Service {

    /**
     * Constructor
     *
     * @param connection Cassandra connection that is supposed to be used
     */
    public FixedCostsService(CassandraConnection connection) {
        super(connection);
    }

    /**
     * Calculates all Fixed Costs KPIs for a time span (planYear) and from
     * certain point of view (currentPeriod).
     *
     * @param planPeriod    Indicates the time span for which the KPIs are
     *                      supposed to be queried
     * @param currentPeriod The point of view in time from which the data is
     *                      supposed to be looked at
     * @param toCurrency    The desired output currency
     *
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

        Map<String, Set<String>> sbuAndSubregions = orgAndRegionRequest.getSubregionsAndSbuFromFixedCosts();

        /* Nested for-loops that iterate over all sbus and subregions. For every combination a FixedCostsRequest is created */
        resultStream = sbuAndSubregions.keySet().stream().parallel()
                .flatMap(sbu -> sbuAndSubregions.get(sbu).stream()
                        .flatMap(subregion -> new FixedCostsRequest(getConnection(), sbu, planPeriod, currentPeriod,
                                subregion, exchangeRates, orgAndRegionRequest).calculateKpis()));

        /* Finally the result stream is returned */
        return resultStream;
    }

    /**
     * @return a list of all ForecastFixedCostsEntities
     */
    public List<ForecastFixedCostsEntity> getForecastFixedCosts() {
        return new FixedCostsRequest(getConnection()).getForecastFixedCosts();
    }

    /**
     * Method selects FixedCosts depending on parameters. In Request will be
     * decided if we have some special cases of entryType and depending on that
     * selecting everything required through the accessors
     *
     * @param subregion      desired subregion
     * @param sbu            desired sbu
     * @param period         desired period
     * @param entryType      desired entryType
     * @param planPeriodFrom in case we have entry_type eq budget leave ot
     *                       planPeriodTo
     * @param planPeriodTo   in case we have entry_type eq budget leave ot
     *                       planPeriodTo
     *
     * @return
     */
    public List<ForecastFixedCostsEntity> getForecastFixedCosts(String subregion, String sbu, int period, String entryType, int planPeriodFrom, int planPeriodTo) {
        return new FixedCostsRequest(getConnection()).getForecastFixedCosts(subregion, sbu, period, entryType, planPeriodFrom, planPeriodTo);
    }

    /**
     * @return a specific ForecastFixedCostsEntity
     */
    public ForecastFixedCostsEntity getForecastFixedCosts(String sbu, String subregion, int period, String entryType, int planPeriod) {
        return new FixedCostsRequest(getConnection()).getForecastFixedCosts(sbu, subregion, period, entryType, planPeriod);
    }

    /**
     * Method sets new values or records to forecast_fixed_costs table
     *
     * @return boolean value if action was successful or not
     */
    public boolean setForecastFixedCosts(String sbu, String subregion, double fixPreManCost, double shipCost, double sellCost, double diffActPreManCost,
                                         double idleEquipCost, double rdCost, double adminCostBu, double adminCostOd, double adminCostCompany, double otherOpCostBu, double otherOpCostOd,
                                         double otherOpCostCompany, double specItems, double provisions, double currencyGains, double valAdjustInventories, double otherFixCost,
                                         double deprecation, double capCost, double equitiyIncome, double topdownAdjustFixCosts, int planPeriod, int planYear, int planHalfYear, int planQuarter,
                                         int planMonth, String status, String usercomment, String entryType, int period, String region, int periodYear, int periodMonth, String currency,
                                         String userId, String entryTs) {
        return new FixedCostsManipulationRequest(getConnection()).setForecastFixedCosts(
                sbu, subregion, fixPreManCost, shipCost, sellCost, diffActPreManCost, idleEquipCost, rdCost, adminCostBu, adminCostOd, adminCostCompany, otherOpCostBu,
                otherOpCostOd, otherOpCostCompany, specItems, provisions, currencyGains, valAdjustInventories, otherFixCost, deprecation, capCost, equitiyIncome, topdownAdjustFixCosts, planPeriod,
                planYear, planHalfYear, planQuarter, planMonth, status, usercomment, entryType, period, region, periodYear, periodMonth, currency, userId, entryTs
        );
    }
}