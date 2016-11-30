package fourschlag.entities.types;

/**
 * Enum that provides the sales types.
 */

public enum SalesType {
    THIRD_PARTY("3rd_party"),
    TRANSFER("transfer");

    private final String type;

    /**
     * Constructor for SalesType
     *
     * @param type The SalesType which is supposte to be used
     */
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
