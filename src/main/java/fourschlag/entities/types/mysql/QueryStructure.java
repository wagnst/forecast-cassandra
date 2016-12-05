package fourschlag.entities.types.mysql;

public class QueryStructure {
    private String table;
    private String appendum;

    public QueryStructure(String table, String appendum) {
        this.table = table;
        this.appendum = appendum;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getAppendum() {
        return appendum;
    }

    public void setAppendum(String appendum) {
        this.appendum = appendum;
    }

    public String getQuery() {
        return "FROM " + getTable() + " WHERE " + getAppendum();
    }
}
