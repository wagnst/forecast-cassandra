package fourschlag.entities.types;

import java.util.HashMap;

public class PeriodMap<V> extends HashMap<Integer, V> {
    public PeriodMap(Period periodFrom, Period periodTo) {
        super();
        while (periodFrom.getPeriod() < periodTo.getPeriod()) {
            put(periodFrom.getPeriod(), null);
            periodFrom.increment();
        }
    }
}
