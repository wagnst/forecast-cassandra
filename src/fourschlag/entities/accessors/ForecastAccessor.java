package fourschlag.entities.accessors;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Query;
import fourschlag.entities.ActualSalesEntity;

/**
 * Created by thor on 07.11.2016.
 */
public interface ForecastAccessor {
    //Test-Query
    @Query("SELECT sales_volume, net_sales, period FROM actual_sales WHERE product_main_group = 'Oettinger Schwarzbier';")
    Result<ActualSalesEntity> getSomething();
}
