package fourschlag.entities.accessors.fixedcosts;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import fourschlag.entities.tables.kpi.fixedcosts.ActualFixedCostsEntity;

/**
 * Provides functionality to query the database for the ActualFixedCosts
 */

@Accessor
public interface ActualFixedCostsAccessor {
    /*CQL-Query to get the ActualFixedCosts data */
    @Query("SELECT fix_pre_man_cost, ship_cost, sell_cost, diff_act_pre_man_cost, idle_equip_cost, rd_cost, admin_cost_bu, admin_cost_od, admin_cost_company, other_op_cost_bu, other_op_cost_od, other_op_cost_company, spec_items, provisions, currency_gains, val_adjust_inventories, other_fix_cost, depreciation, cap_cost, equity_income, currency FROM actual_fixed_costs WHERE sbu = :sbu AND subregion = :subregion AND period = :period")

    /*Gets the FixedCostsKpis*/
    ActualFixedCostsEntity getFixedCostsKpis(
            @Param("sbu") String sbu,
            @Param("subregion") String subregion,
            @Param("period") int period);

    @Query("SELECT fix_pre_man_cost, ship_cost, sell_cost, diff_act_pre_man_cost, idle_equip_cost, rd_cost, admin_cost_bu, admin_cost_od, admin_cost_company, other_op_cost_bu, other_op_cost_od, other_op_cost_company, spec_items, provisions, currency_gains, val_adjust_inventories, other_fix_cost, depreciation, cap_cost, equity_income, currency, period FROM actual_fixed_costs WHERE sbu = :sbu AND subregion = :subregion AND period >= :period_from AND period <= :period_to")
    Result<ActualFixedCostsEntity> getMultipleFixedCostsKpis(
            @Param("sbu") String sbu,
            @Param("subregion") String subregion,
            @Param("period_from") int periodFrom,
            @Param("period_to") int periodTo);

    /*CQL-Query to get the ActualFixedCosts sbu and subregion*/
    @Query("SELECT DISTINCT sbu, subregion FROM actual_fixed_costs")
    Result<ActualFixedCostsEntity> getDistinctSbuAndSubregions();
}
