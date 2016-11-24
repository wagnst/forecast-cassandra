package fourschlag.services.db;

/**
 * Enum that contains all endpoints of the cassandra cluster
 * <p>
 * The ENUM contains cluster endpoint addresses which can have
 * authentication enabled or not. In case, no auth is used, leave
 * the according parameters empty
 */
public enum ClusterEndpoints {

    NODE1("141.19.145.142", "cassandra", "cassandra"),
    NODE2("141.19.145.134", "cassandra", "cassandra"),
    NODE3("141.19.145.144", "cassandra", "cassandra"),
    NODE4("141.19.145.132", "cassandra", "cassandra"),
    DEV("127.0.0.1", "", "");

    private final String address;


    private final String username;
    private final String password;

    ClusterEndpoints(final String address, final String username, final String password) {
        this.address = address;
        this.username = username;
        this.password = password;
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
     * Getter for the username of the node
     *
     * @return username of node
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter for the password of the node
     *
     * @return password of the node
     */
    public String getPassword() {
        return password;
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
