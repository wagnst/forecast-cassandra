package fourschlag.entities.types;

import java.util.LinkedHashMap;

public class PeriodMap<V> extends LinkedHashMap<Integer, V> {
    public PeriodMap(Period periodFrom, Period periodTo) {
        super();
        Period tempPeriodFrom = new Period(periodFrom);
        while (tempPeriodFrom.getPeriod() < periodTo.getPeriod()) {
            put(tempPeriodFrom.getPeriod(), null);
            tempPeriodFrom.increment();
        }
    }
}
