package fourschlag.entities.jpaTables;

import fourschlag.entities.jpaTables.keys.ForecastSalesKey;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "forecast_sales")
public class ForecastSalesEntity extends SalesEntity {

    @EmbeddedId
    private ForecastSalesKey primaryKey;

    @Column(name = "period_year")
    private int periodYear;

    @Column(name = "period_month")
    private int periodMonth;

    @Column(name = "userid")
    private String userId;

    @Column(name = "entry_ts")
    private String entryTs;

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

    public ForecastSalesEntity() {
    }

    public ForecastSalesEntity(String productMainGroup, String region) {
        this.primaryKey = new ForecastSalesKey(productMainGroup, region);
    }

    public ForecastSalesEntity(double salesVolumes, double netSales, double cm1, double topdownAdjustSalesVolumes, double topdownAdjustNetSales, double topdownAdjustCm1 , String currency) {
        super(salesVolumes, netSales, cm1, currency);
        this.topdownAdjustSalesVolumes = topdownAdjustSalesVolumes;
        this.topdownAdjustNetSales = topdownAdjustNetSales;
        this.topdownAdjustCm1 = topdownAdjustCm1;
    }

    public ForecastSalesEntity(double cm1, double topdownAdjustCm1, String currency) {
        super(cm1, currency);
        this.topdownAdjustCm1 = topdownAdjustCm1;
    }

    public ForecastSalesKey getPrimaryKey() {
        return primaryKey;
    }

    public int getPeriodYear() {
        return periodYear;
    }

    public int getPeriodMonth() {
        return periodMonth;
    }

    public String getUserId() {
        return userId;
    }

    public String getEntryTs() {
        return entryTs;
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

}
