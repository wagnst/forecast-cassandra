package fourschlag.entities.jpaTables;

import fourschlag.entities.jpaTables.keys.ForecastFixedCostsKey;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table (name = "forecast_fixed_costs")
public class ForecastFixedCostsEntity extends FixedCostsEntity {
    @EmbeddedId
    private ForecastFixedCostsKey primaryKey;

    @Column(name = "region")
    private String region;

    @Column(name = "period_year")
    private int periodYear;

    @Column(name = "period_month")
    private int periodMonth;

    @Column(name = "userid")
    private String userId;

    @Column(name = "entry_ts")
    private String entryTs;

    @Column(name = "topdown_adjust_fix_costs")
    private double topdownAdjustFixCosts;

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

    public ForecastFixedCostsEntity() {
    }

    public ForecastFixedCostsEntity (String sbu, String subregion) {
        this.primaryKey = new ForecastFixedCostsKey(sbu, subregion);
    }

    public ForecastFixedCostsEntity(double fixPreManCost, double shipCost, double sellCost, double diffActPreManCost,
                                    double idleEquipCost, double rdCost, double adminCostBu, double adminCostOd,
                                    double adminCostCompany, double otherOpCostBu, double otherOpCostOd,
                                    double otherOpCostCompany, double specItems, double provisions, double currencyGains,
                                    double valAdjustInventories, double otherFixCost, double depreciation, double capCost,
                                    double equityIncome, double topdownAdjustFixCosts, String currency) {
        super(fixPreManCost, shipCost, sellCost, diffActPreManCost, idleEquipCost, rdCost, adminCostBu, adminCostOd,
                adminCostCompany, otherOpCostBu, otherOpCostOd, otherOpCostCompany, specItems, provisions, currencyGains,
                valAdjustInventories, otherFixCost, depreciation, capCost, equityIncome, currency);
        this.topdownAdjustFixCosts = topdownAdjustFixCosts;
    }

    public ForecastFixedCostsKey getPrimaryKey() {
        return primaryKey;
    }

    public String getRegion() {
        return region;
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

    public double getTopdownAdjustFixCosts() {
        return topdownAdjustFixCosts;
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
