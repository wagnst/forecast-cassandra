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
        /* Prepare result list that will be returned later */
        List<OutputDataType> resultList = new ArrayList<>();

        /* Create instance of ExchangeRateRequest with the desired currency */
        ExchangeRateRequest exchangeRates = new ExchangeRateRequest(getConnection(), toCurrency);
        /* Get all of the regions from the region table */
        Set<String> regions = new RegionRequest(getConnection()).getRegions();
        /* Get all of the Product Main Groups from the OrgStructure table*/
        Result<OrgStructureEntity> products = new OrgStructureRequest(getConnection()).getProductMainGroups();

        /* Create instance of Period with the given int value */
        Period currentPeriod = new Period(currentPeriodInt);

        /* TODO: Threading can be implemented here */
        /* Now we loop over all products, regions and finally sales types */
        for (OrgStructureEntity product : products) {
            for (String region : regions) {
                /* TODO: Maybe get Fixed Cost KPIs here too (generic refactor) */
                /* use sales_types from enum, instead of mapped ones */
                for (SalesType salesType : SalesType.values())
                    /* TODO: Set instead of List for result list? --> Performance? */
                    /* A new SalesRequest is created each time with all current parameters.
                     * Then the method getSalesKpis is called and we add the List that is returned to our result list
                     */
                    resultList.addAll(new SalesRequest(getConnection(),
                            product.getProductMainGroup(), product.getSbu(), planYear, currentPeriod, region, salesType,
                            exchangeRates).getSalesKpis());
            }
        }
        /* Finally the result list will be returned */
        return resultList;
    }
}