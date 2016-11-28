package fourschlag.services.data;

import fourschlag.services.db.CassandraConnection;

/**
 * Super class Service. Contains information about the connection to the
 * database.
 */
public abstract class Service {

    private CassandraConnection connection;

    /**
     * Constructor for Service
     *
     * @param connection Cassandra connection that is supposed to be used
     */
    public Service(CassandraConnection connection) {
        this.connection = connection;
    }

    /**
     * Getter for the connection to the database
     *
     * @return Connection to the database
     */
    public CassandraConnection getConnection() {
        return connection;
    }

}