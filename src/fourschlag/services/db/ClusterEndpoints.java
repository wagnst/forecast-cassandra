package fourschlag.services.db;

/**
 * Enum that contains all endpoints of the cassandra cluster
 */
public enum ClusterEndpoints {
    NODE1("141.19.145.142"),
    NODE2("141.19.145.134"),
    NODE3("141.19.145.144"),
    NODE4("141.19.145.132");

    private final String address;

    ClusterEndpoints(final String address) {
        this.address = address;
    }

    /**
     * Getter for the ip address of the node
     *
     * @return ip address of node
     */
    public String getAddress() {
        return address;
    }

    /**
     * Overriden toString method that returns the ip adress of the node
     *
     * @return ip address of node
     */
    @Override
    public String toString() {
        return getAddress();
    }
}
