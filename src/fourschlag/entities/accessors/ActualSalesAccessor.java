package fourschlag.entities.accessors;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import fourschlag.entities.ActualSalesEntity;

@Accessor
public interface ActualSalesAccessor {
    //Test-Query
    @Query("SELECT sbu FROM actual_sales;")
    Result<ActualSalesEntity> getSomething();

    @Query("SELECT sales_volumes, net_sales, cm1, sbu, period, product_main_group, sales_type, region, currency, data_source FROM actual_sales WHERE product_main_group = :product_main_group AND period = :period ALLOW FILTERING;")
    Result<ActualSalesEntity> getKPIs(@Param("product_main_group") String product_main_group, @Param("period") int period);

    @Query("SELECT sales_volumes, sbu, sales_type FROM actual_sales WHERE product_main_group = :product_main_group AND period = :period AND region = :region AND sales_type = '3rd_party' AND data_source = 'BW B' ALLOW FILTERING;")
    ActualSalesEntity getSalesVolumes(@Param("product_main_group") String product_main_group, @Param("period") int period, @Param("region") String region);
}
