package fourschlag.entities.tables;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.Table;

import java.util.UUID;

@Table(name = "actual_fixed_costs")
public class ActualFixedCostsEntity extends FixedCostsEntity {

    @Column(name = "period_half_year")
    private int periodHalfYear;

    @Column(name = "period_quarter")
    private int periodQuarter;

    public ActualFixedCostsEntity() {
    }

    public ActualFixedCostsEntity(UUID uuid, int period, String region, int periodYear, int periodMonth, String currency, String userId, String entryTs, String sbu, String subregion, double fixPreManCost, double shipCost, double sellCost, double diffActPreManCost, double idleEquipCost, double rdCost, double adminCostBu, double adminCostOd, double adminCostCompany, double otherOpCostBu, double otherOpCostOd, double otherOpCostCompany, double specItems, double provisions, double currencyGains, double valAdjustInventories, double otherFixCost, double depreciation, double capCost, double equityIncome, int periodHalfYear, int periodQuarter) {
        super(uuid, period, region, periodYear, periodMonth, currency, userId, entryTs, sbu, subregion, fixPreManCost, shipCost, sellCost, diffActPreManCost, idleEquipCost, rdCost, adminCostBu, adminCostOd, adminCostCompany, otherOpCostBu, otherOpCostOd, otherOpCostCompany, specItems, provisions, currencyGains, valAdjustInventories, otherFixCost, depreciation, capCost, equityIncome);
        this.periodHalfYear = periodHalfYear;
        this.periodQuarter = periodQuarter;
    }

    public int getPeriodHalfYear() {
        return periodHalfYear;
    }

    public void setPeriodHalfYear(int periodHalfYear) {
        this.periodHalfYear = periodHalfYear;
    }

    public int getPeriodQuarter() {
        return periodQuarter;
    }

    public void setPeriodQuarter(int periodQuarter) {
        this.periodQuarter = periodQuarter;
    }
}

