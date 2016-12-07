package fourschlag.services.data.requests;

import fourschlag.entities.jpalAccessors.ActualSalesAccessor;
import fourschlag.entities.jpalAccessors.ForecastSalesAccessor;
import fourschlag.entities.jpaTables.ActualSalesEntity;
import fourschlag.entities.jpaTables.ForecastSalesEntity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SalesRequest extends Request {
    private ActualSalesAccessor actualSalesAccessor;
    private ForecastSalesAccessor forecastSalesAccessor;

    private Map<String, Set<String>> productMap;

    public SalesRequest() {
        actualSalesAccessor = new ActualSalesAccessor();
        forecastSalesAccessor = new ForecastSalesAccessor();
    }

    public Map<String, Set<String>> getPmgAndRegionsFromSales() {
        if (productMap == null) {
            queryPmgAndRegionsFromSales();
        }
        return productMap;
    }

    private void queryPmgAndRegionsFromSales() {
        Iterable<ActualSalesEntity> entitiesFromActual = actualSalesAccessor.getDistinctPmgAndRegions();
        Iterable<ForecastSalesEntity> entitiesFromForecast = forecastSalesAccessor.getDistinctPmgAndRegions();
        productMap = new HashMap<>();

        for (ActualSalesEntity entity : entitiesFromActual) {
            if (productMap.containsKey(entity.getPrimaryKey().getProductMainGroup())) {
                productMap.get(entity.getPrimaryKey().getProductMainGroup()).add(entity.getPrimaryKey().getRegion());
            } else {
                productMap.put(entity.getPrimaryKey().getProductMainGroup(), new HashSet<String>() {{
                    add(entity.getPrimaryKey().getRegion());
                }});
            }
        }

        for (ForecastSalesEntity entity : entitiesFromForecast) {
            if (productMap.containsKey(entity.getPrimaryKey().getProductMainGroup())) {
                productMap.get(entity.getPrimaryKey().getProductMainGroup()).add(entity.getPrimaryKey().getRegion());
            } else {
                productMap.put(entity.getPrimaryKey().getProductMainGroup(), new HashSet<String>() {{
                    add(entity.getPrimaryKey().getRegion());
                }});
            }
        }
    }
}
