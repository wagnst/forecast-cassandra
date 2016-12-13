package fourschlag.entities.types;

import java.util.LinkedHashMap;

/**
 * Generic subclass of LinkedHashMap that uses integer values as keys and automatically fills the map.
 * @param <V> Type of the values used in the map
 */
public class PeriodMap<V> extends LinkedHashMap<Integer, V> {
    /**
     * Constructor for PeriodMap. Fills the map with integer values as keys of a certain time span. The value is set to null.
     *
     * @param periodFrom Period to start from
     * @param periodTo Period to end with
     */
    public PeriodMap(Period periodFrom, Period periodTo) {
        super();
        Period tempPeriodFrom = new Period(periodFrom);
        while (tempPeriodFrom.getPeriod() < periodTo.getPeriod()) {
            put(tempPeriodFrom.getPeriod(), null);
            tempPeriodFrom.increment();
        }
    }
}
