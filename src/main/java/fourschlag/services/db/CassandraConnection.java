package fourschlag.services.db;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.policies.DefaultRetryPolicy;
import com.datastax.driver.mapping.MappingManager;

/**
 * CassandraConnection
 */
public class CassandraConnection {

    private Cluster cluster;
    private Session session;
    private MappingManager manager;
    private static CassandraConnection instance = null;

    /**
     * Constructor for Cassandra Connection.
     *
     * @param endpoint Endpoint of the database cluster to connect to
     * @param keyspace Keyspace in the database to connect to
     */
    private CassandraConnection(ClusterEndpoints endpoint, KeyspaceNames keyspace) {
        cluster = Cluster
                .builder()
                .addContactPoint(endpoint.getAddress())
                .withRetryPolicy(DefaultRetryPolicy.INSTANCE)
                .build();
        session = cluster.connect(keyspace.getKeyspace());
        manager = new MappingManager(session);
    }

    public static CassandraConnection getInstance() {
        if (instance == null) {
            instance = new CassandraConnection(ClusterEndpoints.DEMO, KeyspaceNames.DEMO);
        }
        return instance;
    }

    /**
     * Getter for the session
     *
     * @return session for the current connection
     */
    public Session getSession() {
        return session;
    }

    /**
     * Closes the current connection to the database
     */
    public void closeConnection() {
        cluster.close();
    }

    public void closeSession() {
        session.close();
    }

    /* TODO: Method to rebuild connection */

    /**
     * Getter for the MappingManager, that is needed to create Accessor objects
     *
     * @return MappingManager for the current session
     */
    public MappingManager getManager() {
        return manager;
    }
}