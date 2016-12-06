package fourschlag.entities.accessors.sales;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import fourschlag.entities.tables.kpi.sales.ForecastSalesEntity;

/**
 * Provides functionality to query the database for ForecastSales
 */

@Accessor
public interface ForecastSalesAccessor {
    /*CQL-Query to get the ForecastSales data */
    @Query("SELECT sales_volumes, net_sales, cm1, topdown_adjust_sales_volumes, topdown_adjust_net_sales, topdown_adjust_cm1, currency FROM forecast_sales WHERE product_main_group = :product_main_group AND period = :period AND plan_period = :plan_period AND region = :region AND sales_type = :sales_type AND entry_type = :entry_type")
    ForecastSalesEntity getSalesKpis(
            @Param("product_main_group") String productMainGroup,
            @Param("period") int period,
            @Param("plan_period") int planPeriod,
            @Param("region") String region,
            @Param("sales_type") String salesType,
            @Param("entry_type") String entryType);

    /*CQL-Query to get the ForecastSales data */
    @Query("SELECT cm1, topdown_adjust_cm1, currency FROM forecast_sales WHERE product_main_group = :product_main_group AND period = :period AND plan_period = :plan_period AND region = :region AND sales_type = :sales_type AND entry_type = 'forecast'")
    ForecastSalesEntity getCm1(
            @Param("product_main_group") String productMainGroup,
            @Param("period") int period,
            @Param("plan_period") int planPeriod,
            @Param("region") String region,
            @Param("sales_type") String salesType);

    /*CQL-Query to get the ForecastSales Product Main Group and Region */
    @Query("SELECT DISTINCT product_main_group, region FROM forecast_sales")
    Result<ForecastSalesEntity> getDistinctPmgAndRegions();

    /*CQL-Query to get all data from forecast_sales table*/
    @Query("SELECT * FROM forecast_sales")
    Result<ForecastSalesEntity> getAllForecastSales();

    /*CQL-Query to get a unique record (depends on primary keys
        product_main_group,region,period,sales_type,plan_period,entry_type */
    @Query("SELECT * FROM forecast_sales WHERE product_main_group = :product_main_group AND region = :region AND period = :period AND sales_type = :sales_type AND plan_period = :plan_period AND entry_type = :entry_type")
    Result<ForecastSalesEntity> getForecastSales(
            @Param("product_main_group") String productMainGroup,
            @Param("region") String region,
            @Param("period") int period,
            @Param("sales_type") String salesType,
            @Param("plan_period") int planPeriod,
            @Param("entry_type") String entryType
    );

    @Query("SELECT * FROM forecast_sales WHERE product_main_group = :product_main_group AND region = :region AND period = :period AND sales_type = :sales_type AND entry_type = :entry_type AND plan_period >= :plan_period_from AND plan_period <= :plan_period_to")
    Result<ForecastSalesEntity> getForecastSales(
            @Param("product_main_group") String productMainGroup,
            @Param("region") String region,
            @Param("period") int period,
            @Param("sales_type") String salesType,
            @Param("entry_type") String entryType,
            @Param("plan_period_from") int planPeriodFrom,
            @Param("plan_period_to") int planPeriodTo
    );

    /*CQL-Query to update a record via primary keys
        product_main_group,region,period,sales_type,plan_period,entry_type */
    @Query("UPDATE forecast_sales SET topdown_adjust_sales_volumes=:topdown_adjust_sales_volumes, topdown_adjust_net_sales=:topdown_adjust_net_sales, topdown_adjust_cm1=:topdown_adjust_cm1, plan_year=:plan_year, plan_half_year=:plan_half_year, plan_quarter=:plan_quarter, plan_month=:plan_month, status=:status, usercomment=:usercomment, sales_volumes=:sales_volumes, net_sales=:net_sales, cm1=:cm1, period_year=:period_year, period_month=:period_month, currency=:currency, userid=:userid, entry_ts=:entry_ts WHERE product_main_group = :product_main_group AND region = :region AND period = :period AND sales_type = :sales_type AND plan_period = :plan_period AND entry_type = :entry_type")
    ResultSet updateForecastSales(
            /* ForecastSalesEntity */
            @Param("topdown_adjust_sales_volumes") double topdownAdjustSalesVolumes,
            @Param("topdown_adjust_net_sales") double topdownAdjustNetSales,
            @Param("topdown_adjust_cm1") double topdownAdjustCm1,
            @Param("plan_period") int planPeriod,
            @Param("plan_year") int planYear,
            @Param("plan_half_year") int planHalfYear,
            @Param("plan_quarter") int planQuarter,
            @Param("plan_month") int planMonth,
            @Param("entry_type") String entryType,
            @Param("status") String status,
            @Param("usercomment") String usercomment,
            /* SalesEntity */
            @Param("product_main_group") String productMainGroup,
            @Param("sales_type") String salesType,
            @Param("sales_volumes") double salesVolumes,
            @Param("net_sales") double netSales,
            @Param("cm1") double cm1,
            /* KpiEntity */
            @Param("period") int period,
            @Param("region") String region,
            @Param("period_year") int periodYear,
            @Param("period_month") int periodMonth,
            @Param("currency") String currency,
            @Param("userid") String userId,
            @Param("entry_ts") String entryTs
    );

    /*CQL-Query to inser data into forecast_sales table*/
    @Query("INSERT INTO forecast_sales (topdown_adjust_sales_volumes, topdown_adjust_net_sales, topdown_adjust_cm1, plan_period, plan_year, plan_half_year, plan_quarter, plan_month, entry_type, status, usercomment, product_main_group, sales_type, sales_volumes, net_sales, cm1, period, region, period_year, period_month, currency, userid, entry_ts) VALUES (:topdown_adjust_sales_volumes, :topdown_adjust_net_sales, :topdown_adjust_cm1, :plan_period, :plan_year, :plan_half_year, :plan_quarter, :plan_month, :entry_type, :status, :usercomment, :product_main_group, :sales_type, :sales_volumes, :net_sales, :cm1, :period, :region, :period_year, :period_month, :currency, :userid, :entry_ts)")
    ResultSet setForecastSales(
            /* ForecastSalesEntity */
            @Param("topdown_adjust_sales_volumes") double topdownAdjustSalesVolumes,
            @Param("topdown_adjust_net_sales") double topdownAdjustNetSales,
            @Param("topdown_adjust_cm1") double topdownAdjustCm1,
            @Param("plan_period") int planPeriod,
            @Param("plan_year") int planYear,
            @Param("plan_half_year") int planHalfYear,
            @Param("plan_quarter") int planQuarter,
            @Param("plan_month") int planMonth,
            @Param("entry_type") String entryType,
            @Param("status") String status,
            @Param("usercomment") String usercomment,
            /* SalesEntity */
            @Param("product_main_group") String productMainGroup,
            @Param("sales_type") String salesType,
            @Param("sales_volumes") double salesVolumes,
            @Param("net_sales") double netSales,
            @Param("cm1") double cm1,
            /* KpiEntity */
            @Param("period") int period,
            @Param("region") String region,
            @Param("period_year") int periodYear,
            @Param("period_month") int periodMonth,
            @Param("currency") String currency,
            @Param("userid") String userId,
            @Param("entry_ts") String entryTs
    );
}