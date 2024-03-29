package fourschlag.entities.types;

/**
 * Enum with all known sales types
 */
public enum SalesType {
    THIRD_PARTY("3rd_party"),
    TRANSFER("transfer");

    private final String type;

    SalesType(String type) {
        this.type = type;
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
    public boolean IsEqualStringandEnum(String str) {
        return this.type.equals(str);
    }

    @Override
    public String toString() {
        return getType();
    }
}
