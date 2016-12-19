package fourschlag.entities.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Extends ValidatedResult. Extends ValidatedResult by an extra map for topdown
 * results.
 */
public class ValidatedResultTopdown extends ValidatedResult {
    private Map<KeyPerformanceIndicators, Double> topdownResult;

    /**
     * Constructor for ValidatedResultTopdown.
     *
     * @param kpiArray the KPIs that will be put in the map
     */
    public ValidatedResultTopdown(KeyPerformanceIndicators[] kpiArray) {
        super(kpiArray);
        topdownResult = new HashMap<>(getKpiResult());
    }

    /**
     * Constructor for ValidatedResultTopdown when there already is a kpi result
     * map.
     *
     * @param kpiResult already existent Map with kpi results
     */
    public ValidatedResultTopdown(Map<KeyPerformanceIndicators, Double> kpiResult) {
        super(kpiResult);
        topdownResult = new HashMap<KeyPerformanceIndicators, Double>() {{
            for (KeyPerformanceIndicators kpi : getKpiResult().keySet()) {
                put(kpi, 0.0);
            }
        }};
    }

    /**
     * Getter for the TopdownResult
     *
     * @return Map of the TopdownResults
     */
    public Map<KeyPerformanceIndicators, Double> getTopdownResult() {
        return topdownResult;
    }

}
