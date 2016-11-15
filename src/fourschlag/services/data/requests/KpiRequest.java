package fourschlag.services.data.requests;

import fourschlag.entities.types.KeyPerformanceIndicators;
import fourschlag.services.db.CassandraConnection;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static fourschlag.entities.types.KeyPerformanceIndicators.*;

/**
 * Extends Request. Contains HashMap that is used by all children of KpiRequest to store the KPIs.
 */
public class KpiRequest extends Request {
    protected Map<KeyPerformanceIndicators, LinkedList<Double>> monthlyKpiValues;

    /**
     * Constructor for KpiRequest
     * @param connection Cassandra connection that is supposed to be used
     */
    public KpiRequest(CassandraConnection connection) {
        super(connection);
        monthlyKpiValues = new HashMap<KeyPerformanceIndicators, LinkedList<Double>>() {{
            put(SALES_VOLUME, new LinkedList<>());
            put(NET_SALES, new LinkedList<>());
            put(CM1, new LinkedList<>());
            put(PRICE, new LinkedList<>());
            put(VAR_COSTS, new LinkedList<>());
            put(CM1_SPECIFIC, new LinkedList<>());
            put(CM1_PERCENT, new LinkedList<>());
        }};
    }
}
