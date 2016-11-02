package service.db;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.policies.DefaultRetryPolicy;

public class CassandraConnection {
    private Cluster cluster;
    private Session session;

    public CassandraConnection(String ipAddress, String keyspace) {
        cluster = Cluster
                .builder()
                .addContactPoint(ipAddress)
                .withRetryPolicy(DefaultRetryPolicy.INSTANCE)
                .build();
        session = cluster.connect(keyspace);
    }

    public Session getSession() {
        return session;
    }

    public void closeConnection() {
        cluster.close();
    }

}