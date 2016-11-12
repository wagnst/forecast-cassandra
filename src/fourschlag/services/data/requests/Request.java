package fourschlag.services.data.requests;

import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.MappingManager;
import fourschlag.services.db.CassandraConnection;

public class Request {
    private CassandraConnection connection;
    private Session session;
    private MappingManager manager;

    public Request() {
        connection = new CassandraConnection();
        session = connection.getSession();
        MappingManager manager = new MappingManager(this.getSession());
    }

    public Session getSession() {
        return this.session;
    }

    public void closeConnection() {
        this.connection.closeConnection();
    }

    public MappingManager getManager() {
        return manager;
    }
}
