package service;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class CassandraConnection {
    private Cluster cluster;
    private Session session;

    public CassandraConnection(String ipAddress, String keyspace) {
        cluster = Cluster.builder().addContactPoint(ipAddress).build();
        session = cluster.connect(keyspace);
    }

    public Session getSession() {
        return session;
    }

    public void closeConnection() {
        cluster.close();
    }
}
