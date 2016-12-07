package fourschlag.entities.mysqlTables;

import fourschlag.entities.mysqlTables.keys.ForecastFixedCostsKey;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table (name = "forecast_fixed_costs")
public class ForecastFixedCostsEntity extends KpiEntity{

    @EmbeddedId
    private ForecastFixedCostsKey primaryKey;

    @Column(name = "region")
    private String region;

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

    @Column(name = "fix_pre_man_cost")
    private double fixPreManCost;

    @Column(name = "ship_cost")
    private double shipCost;

    @Column(name = "sell_cost")
    private double sellCost;

    @Column(name = "diff_act_pre_man_cost")
    private double diffActPreManCost;

    @Column(name = "idle_equip_cost")
    private double idleEquipCost;

    @Column(name = "rd_cost")
    private double rdCost;

    @Column(name = "admin_cost_bu")
    private double adminCostBu;

    @Column(name = "admin_cost_od")
    private double adminCostOd;

    @Column(name = "admin_cost_company")
    private double adminCostCompany;

    @Column(name = "other_op_cost_bu")
    private double otherOpCostBu;

    @Column(name = "other_op_cost_od")
    private double otherOpCostOd;

    @Column(name = "other_op_cost_company")
    private double otherOpCostCompany;

    @Column(name = "spec_items")
    private double specItems;

    @Column(name = "provisions")
    private double provisions;

    @Column(name = "currency_gains")
    private double currencyGains;

    @Column(name = "val_adjust_inventories")
    private double valAdjustInventories;

    @Column(name = "other_fix_cost")
    private double otherFixCost;

    @Column(name = "depreciation")
    private double depreciation;

    @Column(name = "cap_cost")
    private double capCost;

    @Column(name = "equity_income")
    private double equityIncome;

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

    public String getCurrency() {
        return currency;
    }

    public String getUserId() {
        return userId;
    }

    public String getEntryTs() {
        return entryTs;
    }

    public double getFixPreManCost() {
        return fixPreManCost;
    }

    public double getShipCost() {
        return shipCost;
    }

    public double getSellCost() {
        return sellCost;
    }

    public double getDiffActPreManCost() {
        return diffActPreManCost;
    }

    public double getIdleEquipCost() {
        return idleEquipCost;
    }

    public double getRdCost() {
        return rdCost;
    }

    public double getAdminCostBu() {
        return adminCostBu;
    }

    public double getAdminCostOd() {
        return adminCostOd;
    }

    public double getAdminCostCompany() {
        return adminCostCompany;
    }

    public double getOtherOpCostBu() {
        return otherOpCostBu;
    }

    public double getOtherOpCostOd() {
        return otherOpCostOd;
    }

    public double getOtherOpCostCompany() {
        return otherOpCostCompany;
    }

    public double getSpecItems() {
        return specItems;
    }

    public double getProvisions() {
        return provisions;
    }

    public double getCurrencyGains() {
        return currencyGains;
    }

    public double getValAdjustInventories() {
        return valAdjustInventories;
    }

    public double getOtherFixCost() {
        return otherFixCost;
    }

    public double getDepreciation() {
        return depreciation;
    }

    public double getCapCost() {
        return capCost;
    }

    public double getEquityIncome() {
        return equityIncome;
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
