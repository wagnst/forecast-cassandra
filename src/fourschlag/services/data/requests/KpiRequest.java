package fourschlag.services.data.requests;

import fourschlag.entities.types.KeyPerformanceIndicators;
import fourschlag.services.db.CassandraConnection;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static fourschlag.entities.types.KeyPerformanceIndicators.*;

public class KpiRequest extends Request {
    protected static Map<KeyPerformanceIndicators, LinkedList<Double>> monthlyKpiValues = new HashMap<KeyPerformanceIndicators, LinkedList<Double>>() {{
        put(SALES_VOLUME, new LinkedList<>());
        put(NET_SALES, new LinkedList<>());
        put(CM1, new LinkedList<>());
        put(PRICE, new LinkedList<>());
        put(VAR_COSTS, new LinkedList<>());
        put(CM1_SPECIFIC, new LinkedList<>());
        put(CM1_PERCENT, new LinkedList<>());
    }};
    private static int NUMBER_OF_MONTHS = 18;

    public KpiRequest(CassandraConnection connection) {
        super(connection);
    }

    public static int getNumberOfMonths() {
        return NUMBER_OF_MONTHS;
    }
}
