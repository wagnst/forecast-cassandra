package fourschlag.entities.tables.kpi.fixedcosts;


import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.Table;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Extends FixedCostsEntity. Provides the forecast data from the fixed costs
 * table.
 */

@Table(name = "forecast_fixed_costs")
public class ForecastFixedCostsEntity extends FixedCostsEntity {

    @Column(name = "topdown_adjust_fix_costs")
    @JsonProperty("TOPDOWN_ADJUST_FIX_COSTS")
    private double topdownAdjustFixCosts;

    @Column(name = "plan_period")
    @JsonProperty("PLAN_PERIOD")
    private int planPeriod;

    @Column(name = "plan_year")
    @JsonProperty("PLAN_YEAR")
    private int planYear;

    @Column(name = "plan_half_year")
    @JsonProperty("PLAN_HALF_YEAR")
    private int planHalfYear;

    @Column(name = "plan_quarter")
    @JsonProperty("PLAN_QUARTER")
    private int planQuarter;

    @Column(name = "plan_month")
    @JsonProperty("PLAN_MONTH")
    private int planMonth;

    @Column(name = "status")
    @JsonProperty("STATUS")
    private String status;

    @Column(name = "usercomment")
    @JsonProperty("USERCOMMENT")
    private String usercomment;

    @Column(name = "entry_type")
    @JsonProperty("ENTRY_TYPE")
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

    /**
     * Getter for the TopdownAdjustFixCosts
     *
     * @return TopdownAdjustFixCosts that are currently used
     */
    public double getTopdownAdjustFixCosts() {
        return topdownAdjustFixCosts;
    }

    /**
     * Getter for the PlanPeriod
     *
     * @return PlanPeriod that is currently used
     */
    public int getPlanPeriod() {
        return planPeriod;
    }

    /**
     * Getter for the PlanYear
     *
     * @return PlanYear that is currently used
     */
    public int getPlanYear() {
        return planYear;
    }

    /**
     * Getter for the PlanHalfYear
     *
     * @return PlanHalfYear that is currently used
     */
    public int getPlanHalfYear() {
        return planHalfYear;
    }

    /**
     * Getter for the PlanQuarter
     *
     * @return PlanQuarter that is currently used
     */
    public int getPlanQuarter() {
        return planQuarter;
    }

    /**
     * Getter for the PlanMonth
     *
     * @return PlanMonth that is currently used
     */
    public int getPlanMonth() {
        return planMonth;
    }

    /**
     * Getter for the Status
     *
     * @return The current Status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Getter for the Usercomment
     *
     * @return the current Usercomment
     */
    public String getUsercomment() {
        return usercomment;
    }

    /**
     * Getter for the EntryType
     *
     * @return EntryType that is currently used
     */
    public String getEntryType() {
        return entryType;
    }
}
