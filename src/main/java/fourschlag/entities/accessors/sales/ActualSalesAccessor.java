package fourschlag.entities.accessors.sales;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import fourschlag.entities.tables.kpi.sales.ActualSalesEntity;

/**
 * Provides functionality to query the database for the ActualSales
 */

@Accessor
public interface ActualSalesAccessor {
    /**
     * Queries the table for a specific entry, but selects only fields that are need for KPI calculation
     *
     * @param productMainGroup primary key field product_main_group for where clause
     * @param period primary key field period for where clause
     * @param region primary key field region for where clause
     * @param salesType primary key field sales_type for where clause
     * @param dataSource primary key field data_source for where clause
     * @return One database row mapped as ActualSalesEntity. Is null if no data was found.
     */
    @Query("SELECT sales_volumes, net_sales, cm1, currency FROM actual_sales WHERE product_main_group = :product_main_group AND period = :period AND region = :region AND sales_type = :sales_type AND data_source = :data_source;")
    ActualSalesEntity getSalesKPIs(
            @Param("product_main_group") String productMainGroup,
            @Param("period") int period,
            @Param("region") String region,
            @Param("sales_type") String salesType,
            @Param("data_source") String dataSource);

    /**
     * Queries the table for all distinct combinations of PMGs and Regions.
     *
     * @return Iterable of entities mapped as ActualSalesEntity with all combinations
     */
    @Query("SELECT DISTINCT product_main_group, region FROM actual_sales;")
    Result<ActualSalesEntity> getDistinctPmgAndRegions();


    /**
     * Queries the table for multiple entries, but selects only fields that are need for KPI calculation.
     * The query is restricted by an EQ relation on the period field.
     *
     * @param productMainGroup primary key field product_main_group for where clause
     * @param region primary key field region for where clause
     * @param salesType primary key field sales_type for where clause
     * @param dataSource primary key field data_source for where clause
     * @param periodFrom primary key field period to start with for where clause
     * @param periodTo primary key field period to end with for where clause
     * @return Iterable of entities mapped as ActualFixedCostsEntity. Is empty if no data was found.
     */
    @Query("SELECT sales_volumes, net_sales, cm1, period, currency FROM actual_sales WHERE product_main_group = :product_main_group AND region = :region AND sales_type = :sales_type AND data_source = :data_source AND period >= :period_from AND period <= :period_to;")
    Result<ActualSalesEntity> getMultipleSalesKpis(
            @Param("product_main_group") String productMainGroup,
            @Param("region") String region,
            @Param("sales_type") String salesType,
            @Param("data_source") String dataSource,
            @Param("period_from") int periodFrom,
            @Param("period_to") int periodTo);
}
