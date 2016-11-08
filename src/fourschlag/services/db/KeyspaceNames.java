package fourschlag.services.db;

public enum KeyspaceNames {

    ORIGINAL_VERSION("original_version");

    private final String keyspace;

    KeyspaceNames(final String keyspace) {
        this.keyspace = keyspace;
    }

    public String getKeyspace() {
        return keyspace;
    }

    @Override
    public String toString() {
        return getKeyspace();
    }

}
