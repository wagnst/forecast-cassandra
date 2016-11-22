package fourschlag.services.data.requests;

import fourschlag.entities.types.KeyPerformanceIndicators;
import fourschlag.services.db.CassandraConnection;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Extends Request. Contains HashMap that is used by all children of KpiRequest
 * to store the KPIs.
 */
public class KpiRequest extends Request {

    protected Map<KeyPerformanceIndicators, LinkedList<Double>> monthlyKpiValues = new HashMap<>();
    protected Map<KeyPerformanceIndicators, LinkedList<Double>> bjValues = new HashMap<>();

    /**
     * Constructor for KpiRequest
     *
     * @param connection Cassandra connection that is supposed to be used
     */
    public KpiRequest(CassandraConnection connection) {
        super(connection);
    }
}
