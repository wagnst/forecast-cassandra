package fourschlag.services.db;

import java.util.HashSet;
import java.util.Set;

public class ConnectionPool {
    private static Set<JpaConnection> connections = new HashSet<>();
    private static ConnectionPool instance = null;

    public static JpaConnection getConnection(String persistenceUnitName) {
        for (JpaConnection connection : connections) {
            if (connection.getPersistanceUnitName().equals(persistenceUnitName)) {
                    return connection;
            }
        }
        JpaConnection connection = new JpaConnection(persistenceUnitName);
        connections.add(connection);
        return connection;
    }
}
