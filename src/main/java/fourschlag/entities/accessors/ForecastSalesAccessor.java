package fourschlag.entities.accessors;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import fourschlag.entities.tables.ForecastSalesEntity;

@Accessor
public interface ForecastSalesAccessor {
    @Query("SELECT sales_volumes, net_sales, cm1, currency FROM forecast_sales WHERE product_main_group = :product_main_group AND period = :period AND plan_period = :plan_period AND region = :region AND sales_type = :sales_type AND entry_type = 'forecast' ALLOW FILTERING;")
    ForecastSalesEntity getSalesKpis(
            @Param("product_main_group") String productMainGroup,
            @Param("period") int period,
            @Param("plan_period") int planPeriod,
            @Param("region") String region,
            @Param("sales_type") String salesType);

    @Query("SELECT cm1, currency FROM forecast_sales WHERE product_main_group = :product_main_group AND period = :period AND plan_period = :plan_period AND region = :region AND sales_type = :sales_type ALLOW FILTERING;")
    ForecastSalesEntity getCm1(
            @Param("product_main_group") String productMainGroup,
            @Param("period") int period,
            @Param("plan_period") int planPeriod,
            @Param("region") String region,
            @Param("sales_type") String salesType);

    @Query("SELECT sales_volumes, net_sales, cm1, currency FROM forecast_sales WHERE product_main_group = :product_main_group AND period = :period AND plan_period = :plan_period AND region = :region AND sales_type = :sales_type AND entry_type = 'budget' ALLOW FILTERING;")
    ForecastSalesEntity getBudgetYear(
            @Param("product_main_group") String productMainGroup,
            @Param("period") int period,
            @Param("plan_period") int planPeriod,
            @Param("region") String region,
            @Param("sales_type") String salesType);

    @Query("SELECT DISTINCT product_main_group, region FROM forecast_sales;")
    Result<ForecastSalesEntity> getProductMainGroups();
}
