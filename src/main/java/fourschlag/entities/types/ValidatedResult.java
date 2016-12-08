package fourschlag.entities.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides functionality to validate the KPI results
 */


public class ValidatedResult {
    private Map<KeyPerformanceIndicators, Double> kpiResult;

    /**
     * Constructor for ValidatedResult
     *
     * @param kpiArray The KPIs to be validated
     */
    public ValidatedResult(KeyPerformanceIndicators[] kpiArray) {
        kpiResult = new HashMap<KeyPerformanceIndicators, Double>() {{
            for (KeyPerformanceIndicators kpi : kpiArray) {
                put(kpi, 0.0);
            }
        }};
    }

    /**
     * Constructor for ValidatedResult
     *
     * @param kpiResult The KPIs to be validated
     */
    public ValidatedResult(Map<KeyPerformanceIndicators, Double> kpiResult) {
        this.kpiResult = kpiResult;
    }

    public Map<KeyPerformanceIndicators, Double> getKpiResult() {
        return kpiResult;
    }

}
