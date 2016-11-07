package fourschlag.entities.accessors;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import fourschlag.entities.ActualSalesEntity;

/**
 * Created by thor on 07.11.2016.
 */
@Accessor
public interface ActualSalesAccessor {
    //Test-Query
    @Query("SELECT sbu FROM actual_sales;")
    Result<ActualSalesEntity> getSomething();

    @Query("SELECT sales_volumes, net_sales, cm1, sbu, period FROM actual_sales WHERE product_main_group = :product_main_group AND period = :period ALLOW FILTERING;")
    Result<ActualSalesEntity> getKPIs(@Param("product_main_group") String product_main_group, @Param("period") int period);
}
