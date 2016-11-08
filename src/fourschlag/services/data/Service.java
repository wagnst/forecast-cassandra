package fourschlag.services.data;

import com.datastax.driver.core.Session;
import fourschlag.services.db.CassandraConnection;

public class Service {

    private CassandraConnection connection;
    private Session session;

    public Service() {
        connection = new CassandraConnection();
        session = connection.getSession();
    }

    public Session getSession() {
        return this.session;
    }

    public void closeConnection() {
        this.connection.closeConnection();
    }
}