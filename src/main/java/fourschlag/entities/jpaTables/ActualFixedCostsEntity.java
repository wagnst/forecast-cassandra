package fourschlag.entities.jpaTables;

import fourschlag.entities.jpaTables.keys.ActualFixedCostsKey;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "actual_fixed_costs")
public class ActualFixedCostsEntity extends FixedCostsEntity {
    @EmbeddedId
    private ActualFixedCostsKey primaryKey;

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

    @Column(name = "period_half_year")
    private int periodHalfYear;

    @Column(name = "period_quarter")
    private int periodQuarter;

    public ActualFixedCostsEntity() {}

    public ActualFixedCostsEntity(String sbu, String subregion) {
        this.primaryKey = new ActualFixedCostsKey(sbu, subregion);
    }

    public ActualFixedCostsEntity(double fixPreManCost, double shipCost, double sellCost, double diffActPreManCost,
                                  double idleEquipCost, double rdCost, double adminCostBu, double adminCostOd,
                                  double adminCostCompany, double otherOpCostBu, double otherOpCostOd,
                                  double otherOpCostCompany, double specItems, double provisions, double currencyGains,
                                  double valAdjustInventories, double otherFixCost, double depreciation, double capCost,
                                  double equityIncome, String currency) {
        super(fixPreManCost, shipCost, sellCost, diffActPreManCost, idleEquipCost, rdCost, adminCostBu, adminCostOd,
                adminCostCompany, otherOpCostBu, otherOpCostOd, otherOpCostCompany, specItems, provisions, currencyGains,
                valAdjustInventories, otherFixCost, depreciation, capCost, equityIncome, currency);
    }

    public ActualFixedCostsKey getPrimaryKey() {
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

    public int getPeriodHalfYear() {
        return periodHalfYear;
    }

    public int getPeriodQuarter() {
        return periodQuarter;
    }
}
