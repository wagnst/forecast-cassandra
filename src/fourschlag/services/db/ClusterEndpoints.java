package fourschlag.services.db;

public enum ClusterEndpoints {
    MASTER("141.19.145.142");

    private final String address;

    ClusterEndpoints(final String address) {
        this.address = address;
    }

    public String getAdress() {
        return address;
    }

    @Override
    public String toString() {
        return getAdress();
    }
}
