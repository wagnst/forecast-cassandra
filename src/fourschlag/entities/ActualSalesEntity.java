package fourschlag.entities;

import com.datastax.driver.mapping.annotations.Table;

@Table(keyspace = "original_version", name = "actual_sales")
public class ActualSalesEntity extends SalesEntity {

    //TODO: Unterstriche entfernen (mit Annotationen mappen)
    private String sbu;
    private int period_half_year;
    private int period_quarter;
    private String data_source;

    public ActualSalesEntity() {}

    public ActualSalesEntity(double sales_volumes, double net_sales, double cm1, String product_main_group, String region, String sbu, String sales_type, String data_source, int period, int period_year, int period_half_year, int period_quarter, int period_month, String currency, String userid, String entry_ts) {
        super(sales_volumes, net_sales, cm1, product_main_group, region, sales_type, period, period_year, period_month, currency, userid, entry_ts);
        this.data_source = data_source;
        this.sbu = sbu;
        this.period_half_year = period_half_year;
        this.period_quarter = period_quarter;
    }

    public String getData_source() {
        return data_source;
    }

    public String getSbu() {
        return sbu;
    }

    public int getPeriod_half_year() {
        return period_half_year;
    }

    public int getPeriod_quarter() {
        return period_quarter;
    }
}