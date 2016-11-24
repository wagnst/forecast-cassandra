package fourschlag.services.db;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.policies.DefaultRetryPolicy;
import com.datastax.driver.mapping.MappingManager;

import java.io.IOException;
import java.net.InetAddress;

/**
 * CassandraConnection
 */
public class CassandraConnection {

    private static CassandraConnection instance = null;
    private Cluster cluster;
    private Session session;
    private MappingManager manager;

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

    /**
     * Decide if we are in HS network or on localhost and return a connection
     *
     * @return Cassandra connection instance
     */
    public static CassandraConnection getInstance() {
        if (instance == null) {
            /* check if NODE cluster is available in current network, else fall back to localhost */
            try {
                InetAddress.getByName(String.valueOf(ClusterEndpoints.NODE1)).isReachable(5);
                //use node1
                instance = new CassandraConnection(ClusterEndpoints.NODE1, KeyspaceNames.ORIGINAL_VERSION);
            } catch (IOException e) {
                try {
                    InetAddress.getByName(String.valueOf(ClusterEndpoints.DEV)).isReachable(5);
                    //use localhost
                    instance = new CassandraConnection(ClusterEndpoints.DEV, KeyspaceNames.DEMO);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }

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