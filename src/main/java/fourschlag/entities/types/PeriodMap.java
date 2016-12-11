package fourschlag.entities.types;

import java.util.LinkedHashMap;

public class PeriodMap<V> extends LinkedHashMap<Integer, V> {
    public PeriodMap(Period periodFrom, Period periodTo) {
        super();
        while (periodFrom.getPeriod() < periodTo.getPeriod()) {
            put(periodFrom.getPeriod(), null);
            periodFrom.increment();
        }
    }
}
