package fourschlag.entities.types;

public enum EntryType {
    ACTUAL("actual"),
    FORECAST("forecast"),
    ACTUAL_FORECAST("actual/forecast"),
    BUDGET("budget");

    private final String type;

    EntryType(String type) {
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
