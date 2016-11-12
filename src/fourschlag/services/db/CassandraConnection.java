package fourschlag.services.db;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.policies.DefaultRetryPolicy;

public class CassandraConnection {
    private Cluster cluster;
    private Session session;
    private static CassandraConnection instance = null;

    private CassandraConnection() {
        cluster = Cluster
                .builder()
                .addContactPoint(ClusterEndpoints.NODE1.getAdress())
                .withRetryPolicy(DefaultRetryPolicy.INSTANCE)
                .build();
        session = cluster.connect(KeyspaceNames.ORIGINAL_VERSION.getKeyspace());
    }

    public static CassandraConnection getInstance() {
        if (instance == null) {
            instance = new CassandraConnection();
        }
        return instance;
    }

    public Session getSession() {
        return session;
    }

    public void closeConnection() {
        cluster.close();
    }

}