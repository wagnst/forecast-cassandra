package fourschlag.services.data.requests;

import fourschlag.services.db.CassandraConnection;

public class KpiRequest extends Request {
    private static int NUMBER_OF_MONTHS = 18;

    public static int getNumberOfMonths() {
        return NUMBER_OF_MONTHS;
    }

    public KpiRequest(CassandraConnection connection) {
        super(connection);
    }
}
