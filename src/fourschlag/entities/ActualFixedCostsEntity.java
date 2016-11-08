package fourschlag.entities;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.UUID;

@Table(name = "actual_fixed_costs")
public class ActualFixedCostsEntity {

    @PartitionKey
    @Column(name = "uuid")
    private UUID uuid;

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

    @Column(name = "other_op_cost_bu")
    private double otherOpCostBu;

    @Column(name = "other_op_cost_od")
    private double otherOpCostOd;

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

    @Column(name = "depcrecation")
    private double depreciation;

    @Column(name = "cap_cost")
    private double capCost;

    @Column(name = "sbu")
    private String sbu;

    @Column(name = "region")
    private String region;

    @Column(name = "subregion")
    private String subregion;

    @Column(name = "period")
    private int period;

    @Column(name = "period_year")
    private int periodYear;

    @Column(name = "period_half_year")
    private int periodHalfYear;

    @Column(name = "period_quarter")
    private int periodQuarter;

    @Column(name = "period_month")
    private int periodMonth;

    @Column(name = "currency")
    private String currency;

    @Column(name = "userid")
    private String userId;

    @Column(name = "entry_ts")
    private String entryTs;

    @Column(name = "admin_cost_company")
    private double adminCostCompany;

    @Column(name = "other_op_cost_company")
    private double otherOpCostCompany;

    @Column(name = "equity_income")
    private double equityIncome;

    public ActualFixedCostsEntity() {
    }

    public ActualFixedCostsEntity(UUID uuid, double fix_pre_man_cost, double shipCost, double sellCost, double diffActPreManCost, double idleEquipCost, double rdCost, double adminCostBu, double admin_cost_od, double other_op_cost_bu, double otherOpCostOd, double specItems, double provisions, double currencyGains, double valAdjustInventories, double otherFixCost, double depreciation, double capCost, String sbu, String region, String subregion, int period, int periodYear, int periodHalfYear, int periodQuarter, int periodMonth, String currency, String userId, String entryTs, double adminCostCompany, double otherOpCostCompany, double equityIncome) {
        this.uuid = uuid;
        this.fixPreManCost = fix_pre_man_cost;
        this.shipCost = shipCost;
        this.sellCost = sellCost;
        this.diffActPreManCost = diffActPreManCost;
        this.idleEquipCost = idleEquipCost;
        this.rdCost = rdCost;
        this.adminCostBu = adminCostBu;
        this.adminCostOd = admin_cost_od;
        this.otherOpCostBu = other_op_cost_bu;
        this.otherOpCostOd = otherOpCostOd;
        this.specItems = specItems;
        this.provisions = provisions;
        this.currencyGains = currencyGains;
        this.valAdjustInventories = valAdjustInventories;
        this.otherFixCost = otherFixCost;
        this.depreciation = depreciation;
        this.capCost = capCost;
        this.sbu = sbu;
        this.region = region;
        this.subregion = subregion;
        this.period = period;
        this.periodYear = periodYear;
        this.periodHalfYear = periodHalfYear;
        this.periodQuarter = periodQuarter;
        this.periodMonth = periodMonth;
        this.currency = currency;
        this.userId = userId;
        this.entryTs = entryTs;
        this.adminCostCompany = adminCostCompany;
        this.otherOpCostCompany = otherOpCostCompany;
        this.equityIncome = equityIncome;
    }

    public UUID getUuid() {
        return uuid;
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

    public double getOtherOpCostBu() {
        return otherOpCostBu;
    }

    public double getOtherOpCostOd() {
        return otherOpCostOd;
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

    public String getSbu() {
        return sbu;
    }

    public String getRegion() {
        return region;
    }

    public String getSubregion() {
        return subregion;
    }

    public int getPeriod() {
        return period;
    }

    public int getPeriodYear() {
        return periodYear;
    }

    public int getPeriodHalfYear() {
        return periodHalfYear;
    }

    public int getPeriodQuarter() {
        return periodQuarter;
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

    public double getAdminCostCompany() {
        return adminCostCompany;
    }

    public double getOtherOpCostCompany() {
        return otherOpCostCompany;
    }

    public double getEquityIncome() {
        return equityIncome;
    }
}

