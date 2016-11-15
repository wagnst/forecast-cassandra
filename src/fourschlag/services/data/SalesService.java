package fourschlag.services.data;

import com.datastax.driver.mapping.Result;
import fourschlag.entities.tables.OrgStructureEntity;
import fourschlag.entities.types.OutputDataType;
import fourschlag.entities.types.Period;
import fourschlag.entities.types.SalesType;
import fourschlag.services.data.requests.ExchangeRateRequest;
import fourschlag.services.data.requests.OrgStructureRequest;
import fourschlag.services.data.requests.RegionRequest;
import fourschlag.services.data.requests.SalesRequest;
import fourschlag.services.db.CassandraConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Extends Service. Provides functionality to get sales KPIs
 */
public class SalesService extends Service {

    /**
     * Constructor for SalesService
     * @param connection Cassandra connection that is supposed to be used
     */
    public SalesService(CassandraConnection connection) {
        super(connection);
    }

    /**
     * Calculates all Sales KPIs for a time span (planYear) and from certain point of view (currentPeriod).
     * @param planYear Indicates the time span for which the KPIs are supposed to be queried
     * @param currentPeriodInt The point of view in time from which the data is supposed to be looked at
     * @param toCurrency The desired output currency
     * @return List of the OutputDataTypes that contain all KPIs for the given parameters
     */
    public List<OutputDataType> getSalesKPIs(int planYear, int currentPeriodInt, String toCurrency) {
        List<OutputDataType> resultList = new ArrayList<>();

        ExchangeRateRequest exchangeRates = new ExchangeRateRequest(getConnection(), toCurrency);
        Set<String> regions = new RegionRequest(getConnection()).getRegions();
        Result<OrgStructureEntity> products = new OrgStructureRequest(getConnection()).getProductMainGroups();

        Period currentPeriod = new Period(currentPeriodInt);

        /* fill result list and calculate KPI's */
        /* TODO: Threading can be implemented here */
        for (OrgStructureEntity product : products) {
            for (String region : regions) {
                /* TODO: Maybe get Fixed Cost KPIs here too (generic refactor) */
                /* use sales_types from enum, instead of mapped ones */
                for (SalesType salesType : SalesType.values())
                    /* TODO: Set instead of List? --> Performance? */
                    resultList.addAll(new SalesRequest(getConnection(),
                            product.getProductMainGroup(), product.getSbu(), planYear, currentPeriod, region, salesType,
                            exchangeRates).getSalesKpis());
            }
        }

        return resultList;
    }
}