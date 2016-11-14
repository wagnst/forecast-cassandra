package fourschlag.services.db;

public enum ClusterEndpoints {
    NODE1("141.19.145.142"),
    NODE2("141.19.145.134"),
    NODE3("141.19.145.144"),
    NODE4("141.19.145.132");

    private final String address;

    ClusterEndpoints(final String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return getAddress();
    }
}
