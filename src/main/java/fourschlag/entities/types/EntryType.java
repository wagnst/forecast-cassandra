package fourschlag.entities.types;

/**
 * Enum with all know entry types
 */
public enum EntryType {
    ACTUAL("actual", false),
    FORECAST("forecast", true),
    ACTUAL_FORECAST("actual/forecast", false),
    BUDGET("budget", true),
    TOPDOWN("topdown", false);

    private final String type;
    private final boolean inTable;

    EntryType(String type, boolean inTable) {
        this.type = type;
        this.inTable = inTable;
    }

    /**
     * Searches the enum for a specific entry type by its name
     *
     * @param str name of the entry type
     *
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

    public boolean isInTable() {
        return inTable;
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