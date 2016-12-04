package fourschlag.entities.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Extends ValidatedResult. Provides functionality to validate the results.
 */
public class ValidatedResultTopdown extends ValidatedResult {
    private Map<KeyPerformanceIndicators, Double> topdownResult;

    /**
     * Constructor for ValidatedResultTopdown
     *
     * @param kpiArray The KPIs to be validated
     */
    public ValidatedResultTopdown(KeyPerformanceIndicators[] kpiArray) {
        super(kpiArray);
        topdownResult = new HashMap<>(getKpiResult());
    }

    /**
     * Constructor for ValidatedResultTopdown
     *
     * @param kpiResult The KPIs to be validated
     */
    public ValidatedResultTopdown(Map<KeyPerformanceIndicators, Double> kpiResult) {
        super(kpiResult);
        topdownResult = new HashMap<>(getKpiResult());
    }

    /**
     * Getter for the TopdownResult
     *
     * @return Map of the TopdownResults
     */
    public Map<KeyPerformanceIndicators, Double> getTopdownResult() {
        return topdownResult;
    }

    public void setTopdownResult(Map<KeyPerformanceIndicators, Double> topdownResult) {
        this.topdownResult = topdownResult;
    }
}
