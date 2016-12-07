package fourschlag.services.data.requests;

import fourschlag.entities.jpaAccessors.ActualFixedCostsAccessor;
import fourschlag.entities.jpaAccessors.ForecastFixedCostsAccessor;
import fourschlag.entities.jpaTables.ActualFixedCostsEntity;
import fourschlag.entities.jpaTables.ForecastFixedCostsEntity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FixedCostsRequest extends Request {
    private ActualFixedCostsAccessor actualFixedCostsAccessor;
    private ForecastFixedCostsAccessor forecastFixedCostsAccessor;

    private Map<String, Set<String>> sbuMap;

    public FixedCostsRequest() {
        actualFixedCostsAccessor = new ActualFixedCostsAccessor();
        forecastFixedCostsAccessor = new ForecastFixedCostsAccessor();
    }

    public Map<String, Set<String>> getSubregionsAndSbuFromFixedCosts() {
        if (sbuMap == null) {
            querySubregionsAndSbuFromFixedCosts();
        }
        return sbuMap;
    }

    private void querySubregionsAndSbuFromFixedCosts() {
        Iterable<ActualFixedCostsEntity> entitiesFromActual = actualFixedCostsAccessor.getDistinctSbuAndSubregions();
        Iterable<ForecastFixedCostsEntity> entitiesFromForecast = forecastFixedCostsAccessor.getDistinctSbuAndSubregions();
        sbuMap = new HashMap<>();

        for (ActualFixedCostsEntity entity : entitiesFromActual) {
            if (sbuMap.containsKey(entity.getPrimaryKey().getSbu())) {
                sbuMap.get(entity.getPrimaryKey().getSbu()).add(entity.getPrimaryKey().getSubregion());
            } else {
                sbuMap.put(entity.getPrimaryKey().getSbu(), new HashSet<String>() {{
                    add(entity.getPrimaryKey().getSubregion());
                }});
            }
        }

        for (ForecastFixedCostsEntity entity : entitiesFromForecast) {
            if (sbuMap.containsKey(entity.getPrimaryKey().getSbu())) {
                sbuMap.get(entity.getPrimaryKey().getSbu()).add(entity.getPrimaryKey().getSubregion());
            } else {
                sbuMap.put(entity.getPrimaryKey().getSbu(), new HashSet<String>() {{
                    add(entity.getPrimaryKey().getSubregion());
                }});
            }
        }
    }
}
