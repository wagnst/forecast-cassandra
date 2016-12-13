package fourschlag.entities.types;

/**
 * Enum with all know entry types
 */
public enum EntryType {
    ACTUAL("actual"),
    FORECAST("forecast"),
    ACTUAL_FORECAST("actual/forecast"),
    BUDGET("budget"),
    TOPDOWN("topdown");

    private final String type;

    EntryType(String type) {
        this.type = type;
    }

    /**
     * Searches the enum for a specific entry type by its name
     *
     * @param str name of the entry type
     * @return EntryType or null if nothing was found
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
     * Getter for the Type
     *
     * @return Name of the entry type as String
     */
    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return getType();
    }
}