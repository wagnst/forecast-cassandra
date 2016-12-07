package fourschlag.entities.mysqlTables;

import fourschlag.entities.mysqlTables.keys.ActualFixedCostsKey;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "actual_fixed_costs")
public class ActualFixedCostsEntity extends KpiEntity{
    @EmbeddedId
    private ActualFixedCostsKey primaryKey;

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

    @Column(name = "period_half_year")
    private int periodHalfYear;

    @Column(name = "period_quarter")
    private int periodQuarter;



}
