package fourschlag.services.data;

import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import fourschlag.entities.ActualSalesEntity;
import fourschlag.entities.TestEntity;
import fourschlag.entities.accessors.ForecastAccessor;
import fourschlag.entities.accessors.TestEntityAccessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thor on 07.11.2016.
 */
public class ForecastService extends Service {
    private ForecastAccessor accessor;

    public ForecastService() {
        super("141.19.145.142", "forecast1");
        MappingManager manager = new MappingManager(this.getSession());
        accessor = manager.createAccessor(ForecastAccessor.class);
    }

    public List<ActualSalesEntity> getSomething() {
        List<ActualSalesEntity> resultList = new ArrayList<>();
        Result<ActualSalesEntity> queryResult = accessor.getSomething();

        for (ActualSalesEntity e : queryResult) {
            resultList.add(e);
        }

        return resultList;
    }
}
