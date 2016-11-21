package fourschlag.entities.accessors;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import fourschlag.entities.tables.ActualSalesEntity;

@Accessor
public interface ActualSalesAccessor {
    @Query("SELECT sales_volumes, net_sales, cm1, currency FROM actual_sales WHERE product_main_group = :product_main_group AND period = :period AND region = :region AND sales_type = :sales_type AND data_source = :data_source ALLOW FILTERING;")
    ActualSalesEntity getSalesKPIs(
            @Param("product_main_group") String productMainGroup,
            @Param("period") int period,
            @Param("region") String region,
            @Param("sales_type") String salesType,
            @Param("data_source") String dataSource);

    @Query("SELECT DISTINCT product_main_group FROM actual_sales;")
    Result<ActualSalesEntity> getProductMainGroups();
}
