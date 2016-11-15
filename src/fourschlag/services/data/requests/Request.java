package fourschlag.services.data.requests;

import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.MappingManager;
import fourschlag.services.db.CassandraConnection;

/**
 * Super class Request. Contains information about the current session.
 */
public class Request {
    private Session session;
    private MappingManager manager;

    /**
     * Constructor for Request.
     * @param connection Cassandra connection that is supposed to be used
     */
    public Request(CassandraConnection connection) {
        this.session = connection.getSession();
        this.manager = connection.getManager();
    }

    /**
     * Getter for the session
     * @return session that is currently used
     */
    public Session getSession() {
        return this.session;
    }

    /**
     * Getter for the MappingManager
     * @return MappingManager that is currently used
     */
    public MappingManager getManager() {
        return manager;
    }
}
