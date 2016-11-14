package fourschlag.services.data;

import com.datastax.driver.core.Session;
import fourschlag.services.db.CassandraConnection;

public class Service {
    private CassandraConnection connection;
    private Session session;

    private static int NUMBER_OF_MONTHS = 18;

    public Service() {
        connection = new CassandraConnection();
        session = connection.getSession();
    }

    public void closeConnection() {
        this.connection.closeConnection();
    }

    public Session getSession() {
        return session;
    }

    public CassandraConnection getConnection() {
        return connection;
    }

    public static int getNumberOfMonths() {
        return NUMBER_OF_MONTHS;
    }
}