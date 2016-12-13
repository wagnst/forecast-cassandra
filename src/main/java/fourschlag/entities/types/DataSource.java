package fourschlag.entities.types;

/**
 * Enum with all know data sources
 */
public enum DataSource {
    BW_A("BW A"),
    BW_B("BW B");

    private String dataSource;

    DataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Getter for the DataSource
     *
     * @return name of the data source as String
     */
    public String getDataSource() {
        return dataSource;
    }

    @Override
    public String toString() {
        return dataSource;
    }
}
