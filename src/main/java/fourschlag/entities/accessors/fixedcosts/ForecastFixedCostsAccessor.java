package fourschlag.entities.accessors.fixedcosts;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import fourschlag.entities.tables.kpi.fixedcosts.ForecastFixedCostsEntity;

/**
 * Provides functionality to query the database for ForecastFixedCosts
 */

@Accessor
public interface ForecastFixedCostsAccessor {
    /*CQL-Query to get the ForecastFixedCosts data */
    @Query("SELECT fix_pre_man_cost, ship_cost, sell_cost, diff_act_pre_man_cost, idle_equip_cost, rd_cost, admin_cost_bu, admin_cost_od, admin_cost_company, other_op_cost_bu, other_op_cost_od, other_op_cost_company, spec_items, provisions, currency_gains, val_adjust_inventories, other_fix_cost, depreciation, cap_cost, equity_income, topdown_adjust_fix_costs, currency FROM forecast_fixed_costs WHERE sbu = :sbu AND subregion = :subregion AND period = :period AND plan_period = :plan_period AND entry_type = :entry_type")
    ForecastFixedCostsEntity getFixedCostsKpis(
            @Param("sbu") String sbu,
            @Param("subregion") String subregion,
            @Param("period") int period,
            @Param("plan_period") int planPeriod,
            @Param("entry_type") String entryType
    );

    /*CQL-Query to get drill-down relevant source data*/
    @Query("SELECT fix_pre_man_cost, ship_cost, sell_cost, diff_act_pre_man_cost, idle_equip_cost, rd_cost, admin_cost_bu, admin_cost_od, admin_cost_company, other_op_cost_bu, other_op_cost_od, other_op_cost_company, spec_items, provisions, currency_gains, val_adjust_inventories, other_fix_cost, depreciation, cap_cost, equity_income, topdown_adjust_fix_costs, currency, plan_period FROM forecast_fixed_costs WHERE subregion = :subregion AND sbu = :sbu AND period = :period AND entry_type = :entry_type AND plan_period >= :plan_period_from AND plan_period <= :plan_period_to")
    Result<ForecastFixedCostsEntity> getMultipleFixedCostsKpis(
            @Param("subregion") String subregion,
            @Param("sbu") String sbu,
            @Param("period") int period,
            @Param("entry_type") String entryType,
            @Param("plan_period_from") int planPeriodFrom,
            @Param("plan_period_to") int planPeriodTo
    );

    /*CQL-Query to get the ForecastFixedCosts sbu and subregion*/
    @Query("SELECT DISTINCT sbu, subregion FROM forecast_fixed_costs")
    Result<ForecastFixedCostsEntity> getDistinctSbuAndSubregions();

    /*CQL-Query to get all data from forecast_fixed_costs table*/
    @Query("SELECT * FROM forecast_fixed_costs")
    Result<ForecastFixedCostsEntity> getAllForecastFixedCosts();

    /*CQL-Query to get a unique record (depends on primary keys
        sbu, subregion, period, entry_type and plan_period */
    @Query("SELECT * FROM forecast_fixed_costs WHERE sbu = :sbu AND subregion = :subregion AND period = :period AND entry_type = :entry_type AND plan_period = :plan_period")
    ForecastFixedCostsEntity getSpecificForecastFixedCosts(
            @Param("sbu") String sbu,
            @Param("subregion") String subregion,
            @Param("period") int period,
            @Param("plan_period") int planPeriod,
            @Param("entry_type") String entryType
    );

    /*CQL-Query to get drill-down relevant source data*/
    @Query("SELECT * FROM forecast_fixed_costs WHERE subregion = :subregion AND sbu = :sbu AND period = :period AND entry_type = :entry_type AND plan_period >= :plan_period_from AND plan_period <= :plan_period_to")
    Result<ForecastFixedCostsEntity> getMultipleForecastFixedCosts(
            @Param("subregion") String subregion,
            @Param("sbu") String sbu,
            @Param("period") int period,
            @Param("entry_type") String entryType,
            @Param("plan_period_from") int planPeriodFrom,
            @Param("plan_period_to") int planPeriodTo
    );

    /*CQL-Query to update a record via primary keys
        sbu, subregion, period, entry_type and plan_period */
    @Query("UPDATE forecast_fixed_costs SET fix_pre_man_cost=:fix_pre_man_cost, ship_cost=:ship_cost, sell_cost=:sell_cost, diff_act_pre_man_cost=:diff_act_pre_man_cost, idle_equip_cost=:idle_equip_cost, rd_cost=:rd_cost, admin_cost_bu=:admin_cost_bu, admin_cost_od=:admin_cost_od, admin_cost_company=:admin_cost_company, other_op_cost_bu=:other_op_cost_bu, other_op_cost_od=:other_op_cost_od, other_op_cost_company=:other_op_cost_company, spec_items=:spec_items, provisions=:provisions, currency_gains=:currency_gains, val_adjust_inventories=:val_adjust_inventories, other_fix_cost=:other_fix_cost, depreciation=:depreciation, cap_cost=:cap_cost, equity_income=:equity_income, topdown_adjust_fix_costs=:topdown_adjust_fix_costs, plan_year=:plan_year, plan_half_year=:plan_half_year, plan_quarter=:plan_quarter, plan_month=:plan_month, status=:status, usercomment=:usercomment, region=:region, period_year=:period_year, period_month=:period_month, currency=:currency, userid=:userid, entry_ts=:entry_ts WHERE sbu = :sbu AND subregion = :subregion AND period = :period AND plan_period = :plan_period AND entry_type = :entry_type")
    ResultSet updateForecastFixedCosts(
            /* FixedCostsEntity */
            @Param("sbu") String sbu,
            @Param("subregion") String subregion,
            @Param("fix_pre_man_cost") double fixPreManCost,
            @Param("ship_cost") double shipCost,
            @Param("sell_cost") double sellCost,
            @Param("diff_act_pre_man_cost") double diffActPreManCost,
            @Param("idle_equip_cost") double idleEquipCost,
            @Param("rd_cost") double rdCost,
            @Param("admin_cost_bu") double adminCostBu,
            @Param("admin_cost_od") double adminCostOd,
            @Param("admin_cost_company") double adminCostCompany,
            @Param("other_op_cost_bu") double otherOpCostBu,
            @Param("other_op_cost_od") double otherOpCostOd,
            @Param("other_op_cost_company") double otherOpCostCompany,
            @Param("spec_items") double specItems,
            @Param("provisions") double provisions,
            @Param("currency_gains") double currencyGains,
            @Param("val_adjust_inventories") double valAdjustInventories,
            @Param("other_fix_cost") double otherFixCost,
            @Param("depreciation") double depreciation,
            @Param("cap_cost") double capCost,
            @Param("equity_income") double equityIncome,
            /* ForecastFixedCostsEntity */
            @Param("topdown_adjust_fix_costs") double topdownAdjustFixCosts,
            @Param("plan_period") int planPeriod,
            @Param("plan_year") int planYear,
            @Param("plan_half_year") int planHalfYear,
            @Param("plan_quarter") int planQuarter,
            @Param("plan_month") int planMonth,
            @Param("status") String status,
            @Param("usercomment") String usercomment,
            @Param("entry_type") String entryType,
            /* KpiEntity */
            @Param("period") int period,
            @Param("region") String region,
            @Param("period_year") int periodYear,
            @Param("period_month") int periodMonth,
            @Param("currency") String currency,
            @Param("userid") String userId,
            @Param("entry_ts") String entryTs
    );

    /*CQL-Query to insert data into forecast_fixed_costs table*/
    @Query("INSERT INTO forecast_fixed_costs (sbu, subregion, fix_pre_man_cost, ship_cost, sell_cost, diff_act_pre_man_cost, idle_equip_cost, rd_cost, admin_cost_bu, admin_cost_od, admin_cost_company, other_op_cost_bu, other_op_cost_od, other_op_cost_company, spec_items, provisions, currency_gains, val_adjust_inventories, other_fix_cost, depreciation, cap_cost, equity_income, topdown_adjust_fix_costs, plan_period, plan_year, plan_half_year, plan_quarter, plan_month, status, usercomment, entry_type, period, region, period_year, period_month, currency, userid, entry_ts) VALUES (:sbu, :subregion, :fix_pre_man_cost, :ship_cost, :sell_cost, :diff_act_pre_man_cost, :idle_equip_cost, :rd_cost, :admin_cost_bu, :admin_cost_od, :admin_cost_company, :other_op_cost_bu, :other_op_cost_od, :other_op_cost_company, :spec_items, :provisions, :currency_gains, :val_adjust_inventories, :other_fix_cost, :depreciation, :cap_cost, :equity_income, :topdown_adjust_fix_costs, :plan_period, :plan_year, :plan_half_year, :plan_quarter, :plan_month, :status, :usercomment, :entry_type, :period, :region, :period_year, :period_month, :currency, :userid, :entry_ts)")
    ResultSet setForecastFixedCost(
            /* FixedCostsEntity */
            @Param("sbu") String sbu,
            @Param("subregion") String subregion,
            @Param("fix_pre_man_cost") double fixPreManCost,
            @Param("ship_cost") double shipCost,
            @Param("sell_cost") double sellCost,
            @Param("diff_act_pre_man_cost") double diffActPreManCost,
            @Param("idle_equip_cost") double idleEquipCost,
            @Param("rd_cost") double rdCost,
            @Param("admin_cost_bu") double adminCostBu,
            @Param("admin_cost_od") double adminCostOd,
            @Param("admin_cost_company") double adminCostCompany,
            @Param("other_op_cost_bu") double otherOpCostBu,
            @Param("other_op_cost_od") double otherOpCostOd,
            @Param("other_op_cost_company") double otherOpCostCompany,
            @Param("spec_items") double specItems,
            @Param("provisions") double provisions,
            @Param("currency_gains") double currencyGains,
            @Param("val_adjust_inventories") double valAdjustInventories,
            @Param("other_fix_cost") double otherFixCost,
            @Param("depreciation") double depreciation,
            @Param("cap_cost") double capCost,
            @Param("equity_income") double equityIncome,
            /* ForecastFixedCostsEntity */
            @Param("topdown_adjust_fix_costs") double topdownAdjustFixCosts,
            @Param("plan_period") int planPeriod,
            @Param("plan_year") int planYear,
            @Param("plan_half_year") int planHalfYear,
            @Param("plan_quarter") int planQuarter,
            @Param("plan_month") int planMonth,
            @Param("status") String status,
            @Param("usercomment") String usercomment,
            @Param("entry_type") String entryType,
            /* KpiEntity */
            @Param("period") int period,
            @Param("region") String region,
            @Param("period_year") int periodYear,
            @Param("period_month") int periodMonth,
            @Param("currency") String currency,
            @Param("userid") String userId,
            @Param("entry_ts") String entryTs
    );

    /*CQL-Query to delete an entry with its primary key*/
    @Query("DELETE FROM forecast_fixed_costs WHERE sbu = :sbu AND subregion = :subregion AND period = :period AND entry_type = :entry_type AND plan_period = :plan_period")
    ResultSet deleteForecastFixedCosts(
            @Param("sbu") String sbu,
            @Param("subregion") String subregion,
            @Param("period") int period,
            @Param("entry_type") String entryType,
            @Param("plan_period") int planPeriod
    );
}