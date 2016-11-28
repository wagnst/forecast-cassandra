package fourschlag.services.db;

/**
 * Enum that contains all keyspaces of the Cassandra Databases
 */
public enum KeyspaceNames {

    ORIGINAL_VERSION("original_version"),
    DEMO("demo");

    private final String keyspace;

    /**
     * Constructor
     *
     * @param keyspace name of the keyspace in the database
     */
    KeyspaceNames(final String keyspace) {
        this.keyspace = keyspace;
    }

    /**
     * Getter for the keyspace name
     *
     * @return name of the keyspace
     */
    public String getKeyspace() {
        return keyspace;
    }

    /**
     * Overriden toString method
     *
     * @return name of the keyspace
     */
    @Override
    public String toString() {
        return getKeyspace();
    }

}
