package fourschlag.entities;


import com.datastax.driver.mapping.annotations.PartitionKey;

import java.util.UUID;

public class ForecastFixedCostsEntity {

    @PartitionKey
    private UUID uuid;
    private double fix_pre_man_cost;
    private double ship_cost;
    private double sell_cost;
    private double diff_act_pre_man_cost;
    private double idle_equip_cost;
    private double rd_cost;
    private double admin_cost_bu;
    private double admin_cost_od;
    private double other_op_cost_bu;
    private double other_op_cost_od;
    private double spec_items;
    private double provisions;
    private double currency_gains;
    private double val_adjust_inventories;
    private double other_fix_cost;
    private double topdown_adjust_fix_costs;
    private double depreciation;
    private double cap_cost;
    private String sbu;
    private String region;
    private String subregion;
    private String entry_type;
    private int period;
    private int period_year;
    private int period_month;
    private int plan_period;
    private int plan_year;
    private int plan_half_year;
    private int plan_quarter;
    private int plan_month;
    private String currency;
    private String status;
    private String usercomment;
    private String userid;
    private String entry_ts;
    private double admin_cost_company;
    private double other_op_cost_company;
    private double equity_income;

    public ForecastFixedCostsEntity() {};

    public ForecastFixedCostsEntity(UUID uuid, double fix_pre_man_cost, double ship_cost, double sell_cost, double diff_act_pre_man_cost, double idle_equip_cost, double rd_cost, double admin_cost_bu, double admin_cost_od, double other_op_cost_bu, double other_op_cost_od, double spec_items, double provisions, double currency_gains, double val_adjust_inventories, double other_fix_cost, double topdown_adjust_fix_costs, double depreciation, double cap_cost, String sbu, String region, String subregion, String entry_type, int period, int period_year, int period_month, int plan_period, int plan_year, int plan_half_year, int plan_quarter, int plan_month, String currency, String status, String usercomment, String userid, String entry_ts, double admin_cost_company, double other_op_cost_company, double equity_income) {
        this.uuid = uuid;
        this.fix_pre_man_cost = fix_pre_man_cost;
        this.ship_cost = ship_cost;
        this.sell_cost = sell_cost;
        this.diff_act_pre_man_cost = diff_act_pre_man_cost;
        this.idle_equip_cost = idle_equip_cost;
        this.rd_cost = rd_cost;
        this.admin_cost_bu = admin_cost_bu;
        this.admin_cost_od = admin_cost_od;
        this.other_op_cost_bu = other_op_cost_bu;
        this.other_op_cost_od = other_op_cost_od;
        this.spec_items = spec_items;
        this.provisions = provisions;
        this.currency_gains = currency_gains;
        this.val_adjust_inventories = val_adjust_inventories;
        this.other_fix_cost = other_fix_cost;
        this.topdown_adjust_fix_costs = topdown_adjust_fix_costs;
        this.depreciation = depreciation;
        this.cap_cost = cap_cost;
        this.sbu = sbu;
        this.region = region;
        this.subregion = subregion;
        this.entry_type = entry_type;
        this.period = period;
        this.period_year = period_year;
        this.period_month = period_month;
        this.plan_period = plan_period;
        this.plan_year = plan_year;
        this.plan_half_year = plan_half_year;
        this.plan_quarter = plan_quarter;
        this.plan_month = plan_month;
        this.currency = currency;
        this.status = status;
        this.usercomment = usercomment;
        this.userid = userid;
        this.entry_ts = entry_ts;
        this.admin_cost_company = admin_cost_company;
        this.other_op_cost_company = other_op_cost_company;
        this.equity_income = equity_income;
    }

    public UUID getUuid() {
        return uuid;
    }

    public double getFix_pre_man_cost() {
        return fix_pre_man_cost;
    }

    public double getShip_cost() {
        return ship_cost;
    }

    public double getSell_cost() {
        return sell_cost;
    }

    public double getDiff_act_pre_man_cost() {
        return diff_act_pre_man_cost;
    }

    public double getIdle_equip_cost() {
        return idle_equip_cost;
    }

    public double getRd_cost() {
        return rd_cost;
    }

    public double getAdmin_cost_bu() {
        return admin_cost_bu;
    }

    public double getAdmin_cost_od() {
        return admin_cost_od;
    }

    public double getOther_op_cost_bu() {
        return other_op_cost_bu;
    }

    public double getOther_op_cost_od() {
        return other_op_cost_od;
    }

    public double getSpec_items() {
        return spec_items;
    }

    public double getProvisions() {
        return provisions;
    }

    public double getCurrency_gains() {
        return currency_gains;
    }

    public double getVal_adjust_inventories() {
        return val_adjust_inventories;
    }

    public double getOther_fix_cost() {
        return other_fix_cost;
    }

    public double getTopdown_adjust_fix_costs() {
        return topdown_adjust_fix_costs;
    }

    public double getDepreciation() {
        return depreciation;
    }

    public double getCap_cost() {
        return cap_cost;
    }

    public String getSbu() {
        return sbu;
    }

    public String getRegion() {
        return region;
    }

    public String getSubregion() {
        return subregion;
    }

    public String getEntry_type() {
        return entry_type;
    }

    public int getPeriod() {
        return period;
    }

    public int getPeriod_year() {
        return period_year;
    }

    public int getPeriod_month() {
        return period_month;
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

    public String getCurrency() {
        return currency;
    }

    public String getStatus() {
        return status;
    }

    public String getUsercomment() {
        return usercomment;
    }

    public String getUserid() {
        return userid;
    }

    public String getEntry_ts() {
        return entry_ts;
    }

    public double getAdmin_cost_company() {
        return admin_cost_company;
    }

    public double getOther_op_cost_company() {
        return other_op_cost_company;
    }

    public double getEquity_income() {
        return equity_income;
    }
}
