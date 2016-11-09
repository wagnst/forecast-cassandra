package fourschlag.entities.accessors;

import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import fourschlag.entities.ActualSalesEntity;

@Accessor
public interface ForecastSalesAccessor {
    @Query("SELECT sales_volumes, net_sales, cm1 FROM forecast_sales " +
            "WHERE product_main_group = :product_main_group " +
            "AND period = :period " +
            "AND plan_year = :plan_year " +
            "AND region = :region " +
            "AND sales_type = :sales_type " +
            "AND entry_type = :entry_type" +
            "ALLOW FILTERING;")
    ActualSalesEntity getSalesKPI(
            @Param("product_main_group") String productMainGroup,
            @Param("period") int period,
            @Param("plan_year") int planYear,
            @Param("region") String region,
            @Param("sales_type") String salesType,
            @Param("entry_type") String entryType);
}
