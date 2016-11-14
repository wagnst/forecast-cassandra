package fourschlag.services.data.requests;

import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.MappingManager;
import fourschlag.services.db.CassandraConnection;

public class Request {
    private Session session;
    private MappingManager manager;

    public Request(CassandraConnection connection) {
        this.session = connection.getSession();
        this.manager = connection.getManager();
    }

    public Session getSession() {
        return this.session;
    }

    public MappingManager getManager() {
        return manager;
    }
}
