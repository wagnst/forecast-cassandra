package fourschlag.services.data;

import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import fourschlag.entities.ActualSalesEntity;
import fourschlag.entities.accessors.ActualSalesAccessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thor on 07.11.2016.
 */
public class ActualSalesService extends Service {
    private ActualSalesAccessor accessor;

    public ActualSalesService() {
        super("141.19.145.142", "forecast1");
        MappingManager manager = new MappingManager(this.getSession());
        accessor = manager.createAccessor(ActualSalesAccessor.class);
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
