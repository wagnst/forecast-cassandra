package fourschlag.services.data.service;

import fourschlag.services.db.JpaConnection;

/**
 * Super class Service. Contains information about the connection to the
 * database.
 */
public abstract class Service {
    private JpaConnection connection;

    public Service(JpaConnection connection) {
        this.connection = connection;
    }

    public JpaConnection getConnection() {
        return connection;
    }
}