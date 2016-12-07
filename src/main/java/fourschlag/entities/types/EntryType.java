package fourschlag.entities.types;

/**
 * Enum that provides the possible entry types auf the data
 */


public enum EntryType {
    ACTUAL("actual"),
    FORECAST("forecast"),
    ACTUAL_FORECAST("actual/forecast"),
    BUDGET("budget"),
    TOPDOWN("topdown");

    private final String type;

    /**
     * Constructor for EntryType
     *
     * @param type The type of the Entry (actual, forecast, actual/forecast,
     *             budget, topdown)
     */
    EntryType(String type) {
        this.type = type;
    }

    /**
     * Getter for the Type
     *
     * @return EntryType that is currently used
     */
    public String getType() {
        return type;
    }

    /**
     * Compares a String with the each content of this enum and returns null or
     * itself
     *
     * @param str String which shall be compared
     *
     * @return null or the valid enum
     */
    public static EntryType getEntryTypeByString(String str) {
        for (EntryType et : EntryType.values()) {
            if (et.getType().equals(str)) {
                return et;
            }
        }
        return null;
    }

    /**
     * toString method to print out the currently used type
     *
     * @return EntryType that is currently used
     */
    @Override
    public String toString() {
        return getType();
    }
}