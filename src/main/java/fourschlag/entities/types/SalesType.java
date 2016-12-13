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

    /**
     * Compares a String to the values of this enum, Source:
     * http://stackoverflow.com/questions/9858118/whats-the-proper-way-to-compare-a-string-to-an-enum-value
     *
     * @param str String to be compared to this enum
     *
     * @return true or false depending of String equals enum
     */
    /*
    TODO: method need be to fixed
     */
    public boolean IsEqualStringandEnum(String str) {
        return this.type.equals(str);
    }

    /**
     * Compares a String with the each content of this enum and returns null or
     * itself
     *
     * @param str String which shall be compared
     *
     * @return null or the valid enum
     */
    public static SalesType getSalesTypeByString(String str) {
        for (SalesType st : SalesType.values()) {
            if (st.getType().equals(str)) {
                return st;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return getType();
    }
}
