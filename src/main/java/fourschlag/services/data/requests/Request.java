package fourschlag.services.data.requests;

import fourschlag.services.db.JpaConnection;

/**
 * Super class Request. Contains information about the current session.
 */
public abstract class Request {
    private JpaConnection connection;

    public Request(JpaConnection connection) {
        this.connection = connection;
    }

    public JpaConnection getConnection() {
        return connection;
    }
}
