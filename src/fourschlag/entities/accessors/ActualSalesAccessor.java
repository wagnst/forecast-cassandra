package fourschlag.entities.accessors;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
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
}
