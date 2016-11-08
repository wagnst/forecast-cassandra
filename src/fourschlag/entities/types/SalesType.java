package fourschlag.entities.types;

public enum SalesType {
    THIRD_PARTY("3rd_party"),
    TRANSFER("transfer");

    private final String type;

    SalesType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return getType();
    }
}
