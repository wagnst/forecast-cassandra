package fourschlag.services.db;

import java.util.HashSet;
import java.util.Set;

public class ConnectionPool {
    private static Set<CassandraConnection> connections = new HashSet<>();
    private static ConnectionPool instance = null;

    public static CassandraConnection getConnection(ClusterEndpoints endpoint, KeyspaceNames keyspace, boolean authentification) {
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

    /* TODO: method need to be fixed */
    public static void removeConnection(ClusterEndpoints endpoint, KeyspaceNames keyspace) {
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
