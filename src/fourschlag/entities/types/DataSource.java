package fourschlag.entities.types;

public enum DataSource {
    BW_A("BW A"),
    BW_B("BW B");

    private String dataSource;

    DataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getDataSource() {
        return dataSource;
    }

    @Override
    public String toString() {
        return dataSource;
    }
}
