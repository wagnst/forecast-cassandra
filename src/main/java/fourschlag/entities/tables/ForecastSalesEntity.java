package fourschlag.entities.tables;


import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.Table;

import java.util.UUID;

@Table(name = "forecast_sales")
public class ForecastSalesEntity extends SalesEntity {

    @Column(name = "topdown_adjust_sales_volumes")
    private double topdownAdjustSalesVolumes;

    @Column(name = "topdown_adjust_net_sales")
    private double topdownAdjustNetSales;

    @Column(name = "topdown_adjust_cm1")
    private double topdownAdjustCm1;

    @Column(name = "plan_period")
    private int planPeriod;

    @Column(name = "plan_year")
    private int planYear;

    @Column(name = "plan_half_year")
    private int planHalfYear;

    @Column(name = "plan_quarter")
    private int planQuarter;

    @Column(name = "plan_month")
    private int planMonth;

    @Column(name = "entry_type")
    private String entryType;

    @Column(name = "status")
    private String status;

    @Column(name = "usercomment")
    private String usercomment;

    public ForecastSalesEntity() {
    }

    public ForecastSalesEntity(UUID uuid, double salesVolumes, double netSales, double cm1, int topdownAdjustSalesVolumes, int topdownAdjustNetSales, int topdownAdjustCm1, String productMainGroup, String region, String salesType, String entryType, int period, int periodYear, int periodMonth, int planPeriod, int planYear, int planHalfYear, int planQuarter, int planMonth, String currency, String status, String usercomment, String userId, String entryTs) {
        super(uuid, salesVolumes, netSales, cm1, productMainGroup, region, salesType,
                period, periodYear, periodMonth, currency, userId, entryTs);
        this.topdownAdjustSalesVolumes = topdownAdjustSalesVolumes;
        this.topdownAdjustNetSales = topdownAdjustNetSales;
        this.topdownAdjustCm1 = topdownAdjustCm1;
        this.planPeriod = planPeriod;
        this.planYear = planYear;
        this.planHalfYear = planHalfYear;
        this.planQuarter = planQuarter;
        this.planMonth = planMonth;
        this.status = status;
        this.usercomment = usercomment;
        this.entryType = entryType;
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

    public int getPlanPeriod() {
        return planPeriod;
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

    public String getEntryType() {
        return entryType;
    }

    public String getStatus() {
        return status;
    }

    public String getUsercomment() {
        return usercomment;
    }

}
