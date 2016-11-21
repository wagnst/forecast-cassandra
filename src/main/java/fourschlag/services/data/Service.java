package fourschlag.services.data;

import fourschlag.services.db.CassandraConnection;

/**
 * Super class Service. Contains information about the connection to the
 * database.
 */
public class Service {

    private static int NUMBER_OF_MONTHS = 18;
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
     * Getter for the number of months the Service is querying for
     *
     * @return number of months
     */
    public static int getNumberOfMonths() {
        return NUMBER_OF_MONTHS;
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