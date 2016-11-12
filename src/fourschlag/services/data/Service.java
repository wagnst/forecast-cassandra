package fourschlag.services.data;

import com.datastax.driver.core.Session;
import fourschlag.services.db.CassandraConnection;

public class Service {
    CassandraConnection connection;
    Session session;

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
}