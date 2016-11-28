package fourschlag.entities.types;

import java.util.HashMap;
import java.util.Map;

public class ValidatedResultTopdown extends ValidatedResult{
    private Map<KeyPerformanceIndicators, Double> topdownResult;

    public ValidatedResultTopdown(KeyPerformanceIndicators[] kpiArray) {
        super(kpiArray);
        topdownResult = new HashMap<>(getKpiResult());
    }

    public ValidatedResultTopdown(Map<KeyPerformanceIndicators, Double> kpiResult) {
        super(kpiResult);
        topdownResult = new HashMap<>(getKpiResult());
    }

    public Map<KeyPerformanceIndicators, Double> getTopdownResult() {
        return topdownResult;
    }

    public void setTopdownResult(Map<KeyPerformanceIndicators, Double> topdownResult) {
        this.topdownResult = topdownResult;
    }
}
