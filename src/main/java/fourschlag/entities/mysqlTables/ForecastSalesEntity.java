package fourschlag.entities.mysqlTables;

import fourschlag.entities.mysqlTables.keys.ForecastSalesKey;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "forecast_sales")
public class ForecastSalesEntity extends KpiEntity {

    @EmbeddedId
    private ForecastSalesKey primaryKey;

    @Column(name = "period_year")
    private int periodYear;

    @Column(name = "period_month")
    private int periodMonth;

    @Column(name = "currency")
    private String currency;

    @Column(name = "userid")
    private String userId;

    @Column(name = "entry_ts")
    private String entryTs;

    @Column(name = "sales_volumes")
    private double salesVolumes;

    @Column(name = "net_sales")
    private double netSales;

    @Column(name = "cm1")
    private double cm1;

    @Column(name = "topdown_adjust_sales_volumes")
    private double topdownAdjustSalesVolumes;

    @Column(name = "topdown_adjust_net_sales")
    private double topdownAdjustNetSales;

    @Column(name = "topdown_adjust_cm1")
    private double topdownAdjustCm1;

    @Column(name = "plan_year")
    private int planYear;

    @Column(name = "plan_half_year")
    private int planHalfYear;

    @Column(name = "plan_quarter")
    private int planQuarter;

    @Column(name = "plan_month")
    private int planMonth;

    @Column(name = "status")
    private String status;

    @Column(name = "usercomment")
    private String usercomment;

    public ForecastSalesKey getPrimaryKey() {
        return primaryKey;
    }

    public int getPeriodYear() {
        return periodYear;
    }

    public int getPeriodMonth() {
        return periodMonth;
    }

    public String getCurrency() {
        return currency;
    }

    public String getUserId() {
        return userId;
    }

    public String getEntryTs() {
        return entryTs;
    }

    public double getSalesVolumes() {
        return salesVolumes;
    }

    public double getNetSales() {
        return netSales;
    }

    public double getCm1() {
        return cm1;
    }

    public double getTopdownAdjustSalesVolumes() {
        return topdownAdjustSalesVolumes;
    }

    public double getTopdownAdjustNetSales() {
        return topdownAdjustNetSales;
    }

    public double getTopdownAdjustCm1() {
        return topdownAdjustCm1;
    }

    public int getPlanYear() {
        return planYear;
    }

    public int getPlanHalfYear() {
        return planHalfYear;
    }

    public int getPlanQuarter() {
        return planQuarter;
    }

    public int getPlanMonth() {
        return planMonth;
    }

    public String getStatus() {
        return status;
    }

    public String getUsercomment() {
        return usercomment;
    }

    public void setCm1(double cm1) {
        this.cm1 = cm1;
    }
}
