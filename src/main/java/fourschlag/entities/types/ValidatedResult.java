package fourschlag.entities.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by thor on 28.11.2016.
 */
public class ValidatedResult {
    private Map<KeyPerformanceIndicators, Double> kpiResult;

    public ValidatedResult(KeyPerformanceIndicators[] kpiArray) {
        kpiResult = new HashMap<KeyPerformanceIndicators, Double>(){{
            for (KeyPerformanceIndicators kpi : kpiArray) {
                put(kpi, 0.0);
            }
        }};
    }

    public ValidatedResult(Map<KeyPerformanceIndicators, Double> kpiResult) {
        this.kpiResult = kpiResult;
    }

    public Map<KeyPerformanceIndicators, Double> getKpiResult() {
        return kpiResult;
    }

}
