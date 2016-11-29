package fourschlag.entities.tables.kpi.fixedcosts;


import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.Table;

import java.util.UUID;

@Table(name = "forecast_fixed_costs")
public class ForecastFixedCostsEntity extends FixedCostsEntity {

    @Column(name = "topdown_adjust_fix_costs")
    private double topdownAdjustFixCosts;

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

    @Column(name = "status")
    private String status;

    @Column(name = "usercomment")
    private String usercomment;

    @Column(name = "entry_type")
    private String entryType;

    public ForecastFixedCostsEntity() {
    }

    public ForecastFixedCostsEntity(UUID uuid, int period, String region, int periodYear, int periodMonth, String currency, String userId, String entryTs, String sbu, String subregion, double fixPreManCost, double shipCost, double sellCost, double diffActPreManCost, double idleEquipCost, double rdCost, double adminCostBu, double adminCostOd, double adminCostCompany, double otherOpCostBu, double otherOpCostOd, double otherOpCostCompany, double specItems, double provisions, double currencyGains, double valAdjustInventories, double otherFixCost, double depreciation, double capCost, double equityIncome, double topdownAdjustFixCosts, int planPeriod, int planYear, int planHalfYear, int planQuarter, int planMonth, String status, String usercomment, String entryType) {
        super(uuid, period, region, periodYear, periodMonth, currency, userId, entryTs, sbu, subregion, fixPreManCost, shipCost, sellCost, diffActPreManCost, idleEquipCost, rdCost, adminCostBu, adminCostOd, adminCostCompany, otherOpCostBu, otherOpCostOd, otherOpCostCompany, specItems, provisions, currencyGains, valAdjustInventories, otherFixCost, depreciation, capCost, equityIncome);
        this.topdownAdjustFixCosts = topdownAdjustFixCosts;
        this.planPeriod = planPeriod;
        this.planYear = planYear;
        this.planHalfYear = planHalfYear;
        this.planQuarter = planQuarter;
        this.planMonth = planMonth;
        this.status = status;
        this.usercomment = usercomment;
        this.entryType = entryType;
    }

    public double getTopdownAdjustFixCosts() {
        return topdownAdjustFixCosts;
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

    public String getStatus() {
        return status;
    }

    public String getUsercomment() {
        return usercomment;
    }

    public String getEntryType() {
        return entryType;
    }
}
