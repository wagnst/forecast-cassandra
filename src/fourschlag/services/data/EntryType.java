package fourschlag.services.data;

public enum EntryType {
    ACTUAL("actual"),
    FORECAST("forecast"),
    ACTUAL_FORECAST("actual/forecast");

    private final String type;

    private EntryType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
