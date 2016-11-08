package fourschlag.entities;


import com.datastax.driver.mapping.annotations.PartitionKey;

public class SalesEntity {
    private double sales_volumes;
    private double net_sales;
    private double cm1;
    @PartitionKey
    private String product_main_group;
    private String region;
    private String sales_type;
    private int period;
    private int period_year;
    private int period_month;
    private String currency;
    private String userid;
    private String entry_ts;

    public SalesEntity() {
    }

    public SalesEntity(double sales_volumes, double net_sales, double cm1, String product_main_group, String region, String sales_type, int period, int period_year, int period_month, String currency, String userid, String entry_ts) {
        this.sales_volumes = sales_volumes;
        this.net_sales = net_sales;
        this.cm1 = cm1;
        this.product_main_group = product_main_group;
        this.region = region;
        this.sales_type = sales_type;
        this.period = period;
        this.period_year = period_year;
        this.period_month = period_month;
        this.currency = currency;
        this.userid = userid;
        this.entry_ts = entry_ts;
    }

    public double getSales_volumes() {
        return sales_volumes;
    }

    public double getNet_sales() {
        return net_sales;
    }

    public double getCm1() {
        return cm1;
    }

    public String getProduct_main_group() {
        return product_main_group;
    }

    public String getRegion() {
        return region;
    }
    public String getSales_type() {
        return sales_type;
    }

    public int getPeriod() {
        return period;
    }

    public int getPeriod_year() {
        return period_year;
    }

    public int getPeriod_month() {
        return period_month;
    }

    public String getCurrency() {
        return currency;
    }

    public String getUserid() {
        return userid;
    }

    public String getEntry_ts() {
        return entry_ts;
    }
}
