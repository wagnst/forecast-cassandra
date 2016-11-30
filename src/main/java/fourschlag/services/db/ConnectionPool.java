package fourschlag.services.db;

import java.util.HashSet;
import java.util.Set;

public class ConnectionPool {
    private Set<CassandraConnection> connections;
    private static ConnectionPool instance = null;

    private ConnectionPool() {
        this.connections = new HashSet<>();
    }

    public static ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    public CassandraConnection getConnection(ClusterEndpoints endpoint, KeyspaceNames keyspace, boolean authentification) {
        for (CassandraConnection connection : connections) {
            if (connection.getEndpoint() == endpoint) {
                if (connection.getKeyspace() == keyspace) {
                    return connection;
                }
            }
        }
        CassandraConnection connection = new CassandraConnection(endpoint, keyspace, authentification);
        connections.add(connection);
        return connection;
    }

    public void removeConnection(ClusterEndpoints endpoint, KeyspaceNames keyspace) {
        for (CassandraConnection connection : connections) {
            if (connection.getEndpoint() == endpoint) {
                if (connection.getKeyspace() == keyspace) {
                    connection.closeConnection();
                    connections.remove(connection);
                }
            }
        }
    }
}
