package fourschlag.entities;


import com.datastax.driver.mapping.annotations.Table;

@Table(keyspace = "original_version", name = "forecast_sales")
public class ForecastSalesEntity extends SalesEntity{

    private int topdown_adjust_sales_volumes;
    private int topdown_adjust_net_sales;
    private int topdown_adjust_cm1;
    private int plan_period;
    private int plan_year;
    private int plan_half_year;
    private int plan_quarter;
    private int plan_month;
    private String status;
    private String usercomment;

    public ForecastSalesEntity() {
    }

    public ForecastSalesEntity(double sales_volumes, double net_sales, double cm1, int topdown_adjust_sales_volumes, int topdown_adjust_net_sales, int topdown_adjust_cm1, String product_main_group, String region, String sales_type, String entry_type, int period, int period_year, int period_month, int plan_period, int plan_year, int plan_half_year, int plan_quarter, int plan_month, String currency, String status, String usercomment, String userid, String entry_ts) {
        super(sales_volumes, net_sales, cm1, product_main_group, region, sales_type, period, period_year, period_month, currency, userid, entry_ts);
        this.topdown_adjust_sales_volumes = topdown_adjust_sales_volumes;
        this.topdown_adjust_net_sales = topdown_adjust_net_sales;
        this.topdown_adjust_cm1 = topdown_adjust_cm1;
        this.plan_period = plan_period;
        this.plan_year = plan_year;
        this.plan_half_year = plan_half_year;
        this.plan_quarter = plan_quarter;
        this.plan_month = plan_month;
        this.status = status;
        this.usercomment = usercomment;
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

    public String getStatus() {
        return status;
    }

    public String getUsercomment() {
        return usercomment;
    }
}
