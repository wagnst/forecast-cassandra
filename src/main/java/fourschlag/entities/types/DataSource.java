package fourschlag.entities.types;

/**
 * Enum that provides the data source to be used for querying the database
 */
public enum DataSource {
    BW_A("BW A"),
    BW_B("BW B");

    private String dataSource;

    /**
     * Constructor for dataSource
     *
     * @param dataSource The Datasource to be taken for the current operation
     */
    DataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Getter for the DataSource
     *
     * @return DataSource that is currently used
     */
    public String getDataSource() {
        return dataSource;
    }

    /**
     * toString method to print out the currently used datasource
     *
     * @return Datasource currently used
     */
    @Override
    public String toString() {
        return dataSource;
    }
}
