package fourschlag.entities;


import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.UUID;

@Table(keyspace = "original_version", name = "forecast_sales")
public class ForecastSalesEntity {

    @PartitionKey
    private UUID uuid;
    private double sales_volumes;
    private double net_sales;
    private double cm1;
    private int topdown_adjust_sales_volumes;
    private int topdown_adjust_net_sales;
    private int topdown_adjust_cm1;
    private String product_main_group;
    private String region;
    private String sales_type;
    private String entry_type;
    private int period;
    private int period_year;
    private int period_month;
    private int plan_period;
    private int plan_year;
    private int plan_half_year;
    private int plan_quarter;
    private int plan_month;
    private String currency;
    private String status;
    private String usercomment;
    private String userid;
    private String entry_ts;

    public ForecastSalesEntity() {
    }

    ;

    public ForecastSalesEntity(UUID uuid, double sales_volumes, double net_sales, double cm1, int topdown_adjust_sales_volumes, int topdown_adjust_net_sales, int topdown_adjust_cm1, String product_main_group, String region, String sales_type, String entry_type, int period, int period_year, int period_month, int plan_period, int plan_year, int plan_half_year, int plan_quarter, int plan_month, String currency, String status, String usercomment, String userid, String entry_ts) {
        this.uuid = uuid;
        this.sales_volumes = sales_volumes;
        this.net_sales = net_sales;
        this.cm1 = cm1;
        this.topdown_adjust_sales_volumes = topdown_adjust_sales_volumes;
        this.topdown_adjust_net_sales = topdown_adjust_net_sales;
        this.topdown_adjust_cm1 = topdown_adjust_cm1;
        this.product_main_group = product_main_group;
        this.region = region;
        this.sales_type = sales_type;
        this.entry_type = entry_type;
        this.period = period;
        this.period_year = period_year;
        this.period_month = period_month;
        this.plan_period = plan_period;
        this.plan_year = plan_year;
        this.plan_half_year = plan_half_year;
        this.plan_quarter = plan_quarter;
        this.plan_month = plan_month;
        this.currency = currency;
        this.status = status;
        this.usercomment = usercomment;
        this.userid = userid;
        this.entry_ts = entry_ts;
    }


    public UUID getUuid() {
        return uuid;
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

    public int getTopdown_adjust_sales_volumes() {
        return topdown_adjust_sales_volumes;
    }

    public int getTopdown_adjust_net_sales() {
        return topdown_adjust_net_sales;
    }

    public int getTopdown_adjust_cm1() {
        return topdown_adjust_cm1;
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

    public String getEntry_type() {
        return entry_type;
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

    public int getPlan_period() {
        return plan_period;
    }

    public int getPlan_year() {
        return plan_year;
    }

    public int getPlan_half_year() {
        return plan_half_year;
    }

    public int getPlan_quarter() {
        return plan_quarter;
    }

    public int getPlan_month() {
        return plan_month;
    }

    public String getCurrency() {
        return currency;
    }

    public String getStatus() {
        return status;
    }

    public String getUsercomment() {
        return usercomment;
    }

    public String getUserid() {
        return userid;
    }

    public String getEntry_ts() {
        return entry_ts;
    }
}
