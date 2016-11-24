package fourschlag.entities.accessors;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import fourschlag.entities.tables.ForecastFixedCostsEntity;

@Accessor
public interface ForecastFixedCostsAccessor {
    @Query("SLECT fix_pre_man_cost, ship_cost, sell_cost, diff_act_pre_man_cost, idle_equip_cost, rd_cost, admin_cost_bu, admin_cost_od, admin_cost_company, other_op_cost_bu, other_op_cost_od, other_op_cost_company, spec_items, provisions, currency_gains, val_adjust_inventories, other_fix_cost, depreciation, cap_cost, equity_income, currency FROM forecast_fixed_costs WHERE sbu = :sbu AND subregion = :subregion AND period = :period AND plan_period = :plan_period AND entry_type = :entry_type")
    ForecastFixedCostsEntity getFixedCostsKpis(
            @Param("sbu") String sbu,
            @Param("subregion") String subregion,
            @Param("period") int period,
            @Param("plan_period") int planPeriod,
            @Param("entry_type") String entryType
    );

    @Query("SELECT DISTINCT sbu, subregion FROM forecast_fixed_costs")
    Result<ForecastFixedCostsEntity> getSbuAndSubregions();
}
