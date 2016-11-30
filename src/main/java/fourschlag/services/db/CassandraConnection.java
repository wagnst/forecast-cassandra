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

    private ClusterEndpoints endpoint;
    private KeyspaceNames keyspace;

    /**
     * Constructor for Cassandra Connection.
     *
     * @param endpoint Endpoint of the database cluster to connect to
     * @param keyspace Keyspace in the database to connect to
     * @param authentication True: authentication is required ; False: authentication is not required
     */
    public CassandraConnection(ClusterEndpoints endpoint, KeyspaceNames keyspace, boolean authentication) {
        this.endpoint = endpoint;
        this.keyspace = keyspace;
        /* in case database has "authenticator: PasswordAuthenticator" set, use given credentials */
        /* Build database cluster */
        if (authentication) {
            cluster = Cluster
                    .builder()
                    .addContactPoint(endpoint.getAddress())
                    .withCredentials(endpoint.getUsername(), endpoint.getPassword())
                    .withRetryPolicy(DefaultRetryPolicy.INSTANCE)
                    .build();
        } else {
            cluster = Cluster
                    .builder()
                    .addContactPoint(endpoint.getAddress())
                    .withRetryPolicy(DefaultRetryPolicy.INSTANCE)
                    .build();
        }
        /* Connect to the keyspace and retrieve the session object */
        session = cluster.connect(keyspace.getKeyspace());
        /* Create a new Mapping Manager */
        manager = new MappingManager(session);
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

    public ClusterEndpoints getEndpoint() {
        return endpoint;
    }

    public KeyspaceNames getKeyspace() {
        return keyspace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CassandraConnection that = (CassandraConnection) o;

        if (endpoint != that.endpoint) return false;
        return keyspace == that.keyspace;
    }

    @Override
    public int hashCode() {
        int result = endpoint != null ? endpoint.hashCode() : 0;
        result = 31 * result + (keyspace != null ? keyspace.hashCode() : 0);
        return result;
    }
}