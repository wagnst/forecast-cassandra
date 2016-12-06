package fourschlag.entities.tables.kpi.fixedcosts;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.Table;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Extends FixedCostsEntity. Provides the data from the ActualFixedCosts table
 */

@Table(name = "actual_fixed_costs")
public class ActualFixedCostsEntity extends FixedCostsEntity {

    @Column(name = "period_half_year")
    @JsonProperty("PERIOD_HALF_YEAR")
    private int periodHalfYear;

    @Column(name = "period_quarter")
    @JsonProperty("PERIOD_QUARTER")
    private int periodQuarter;

    public ActualFixedCostsEntity() {
    }

    public ActualFixedCostsEntity(UUID uuid, int period, String region, int periodYear, int periodMonth, String currency, String userId, String entryTs, String sbu, String subregion, double fixPreManCost, double shipCost, double sellCost, double diffActPreManCost, double idleEquipCost, double rdCost, double adminCostBu, double adminCostOd, double adminCostCompany, double otherOpCostBu, double otherOpCostOd, double otherOpCostCompany, double specItems, double provisions, double currencyGains, double valAdjustInventories, double otherFixCost, double depreciation, double capCost, double equityIncome, int periodHalfYear, int periodQuarter) {
        super(uuid, period, region, periodYear, periodMonth, currency, userId, entryTs, sbu, subregion, fixPreManCost, shipCost, sellCost, diffActPreManCost, idleEquipCost, rdCost, adminCostBu, adminCostOd, adminCostCompany, otherOpCostBu, otherOpCostOd, otherOpCostCompany, specItems, provisions, currencyGains, valAdjustInventories, otherFixCost, depreciation, capCost, equityIncome);
        this.periodHalfYear = periodHalfYear;
        this.periodQuarter = periodQuarter;
    }

    /**
     * Getter for the half year of a desired period
     *
     * @return An integer of the half year currently used
     */
    public int getPeriodHalfYear() {
        return periodHalfYear;
    }

    /**
     * Setter for the half year of a desired period
     *
     * @param periodHalfYear An integer value to be set for the half year
     */
    public void setPeriodHalfYear(int periodHalfYear) {
        this.periodHalfYear = periodHalfYear;
    }

    /**
     * Getter for the quarter year of a desired period
     *
     * @return An integer of the quarter year currently used
     */
    public int getPeriodQuarter() {
        return periodQuarter;
    }

    /**
     * Setter for the quarter of a desired period
     *
     * @param periodQuarter An integer value for the quarter
     */
    public void setPeriodQuarter(int periodQuarter) {
        this.periodQuarter = periodQuarter;
    }
}

