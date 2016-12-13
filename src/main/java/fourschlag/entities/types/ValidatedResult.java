package fourschlag.entities.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that can be used to store a validated KPI result.
 */
public class ValidatedResult {
    private Map<KeyPerformanceIndicators, Double> kpiResult;

    /**
     * Constructor for ValidatedResult. Fills the map with KPIs as keys and 0.0 as values.
     *
     * @param kpiArray the KPIs that will be put in the map
     */
    public ValidatedResult(KeyPerformanceIndicators[] kpiArray) {
        kpiResult = new HashMap<KeyPerformanceIndicators, Double>() {{
            for (KeyPerformanceIndicators kpi : kpiArray) {
                put(kpi, 0.0);
            }
        }};
    }

    /**
     * Constructor for ValidatedResult.
     *
     * @param kpiResult already existent kpiResult map
     */
    ValidatedResult(Map<KeyPerformanceIndicators, Double> kpiResult) {
        this.kpiResult = kpiResult;
    }

    public Map<KeyPerformanceIndicators, Double> getKpiResult() {
        return kpiResult;
    }

}
