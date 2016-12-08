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
    /*CQL-Query to get the ActualSales data*/
    @Query("SELECT sales_volumes, net_sales, cm1, currency FROM actual_sales WHERE product_main_group = :product_main_group AND period = :period AND region = :region AND sales_type = :sales_type AND data_source = :data_source ALLOW FILTERING;")
    ActualSalesEntity getSalesKPIs(
            @Param("product_main_group") String productMainGroup,
            @Param("period") int period,
            @Param("region") String region,
            @Param("sales_type") String salesType,
            @Param("data_source") String dataSource);

    /*CQL-Query to get the ActualSales Product Main Group and region*/
    @Query("SELECT DISTINCT product_main_group, region FROM actual_sales;")
    Result<ActualSalesEntity> getDistinctPmgAndRegions();
}
