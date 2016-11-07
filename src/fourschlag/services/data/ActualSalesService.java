package fourschlag.services.data;

import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import fourschlag.entities.ActualSalesEntity;
import fourschlag.entities.accessors.ActualSalesAccessor;

import java.util.ArrayList;
import java.util.List;

public class ActualSalesService extends Service {
    private ActualSalesAccessor accessor;

    public ActualSalesService() {
        super("141.19.145.142", "original_version");
        MappingManager manager = new MappingManager(this.getSession());
        accessor = manager.createAccessor(ActualSalesAccessor.class);
    }

    public List<String> getSomething() {
        List<String> resultList = new ArrayList<>();
        Result<ActualSalesEntity> queryResult = accessor.getSomething();

        for (ActualSalesEntity e : queryResult) {
            resultList.add(e.getSbu());
        }

        return resultList;
    }

    public List<ActualSalesEntity> getKPIs(String product_main_group, int period) {
        List<ActualSalesEntity> resultList = new ArrayList<>();
        for (ActualSalesEntity e : accessor.getKPIs(product_main_group, period)) {
            resultList.add(e);
        }
        return resultList;
    }
}

