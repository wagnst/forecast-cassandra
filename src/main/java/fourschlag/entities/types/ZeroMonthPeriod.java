package fourschlag.entities.types;


/**
 *  Extends Period. Sets the initial Month.
 *
 */
public class ZeroMonthPeriod extends Period {
    public ZeroMonthPeriod(Period period) {
        super(period.getZeroMonthPeriod());
    }

    public ZeroMonthPeriod(int year) {
        super(year, 0);
    }

    /**
     * Increments the initial Month
     *
     * @return incremented Period
     */
    @Override
    public Period increment() {
        this.year++;
        this.period += 100;
        return this;
    }

    /**
     *  Getter for the ZeroMonthPeriod
     *
     * @return the desired ZeroMonthPeriod
     */
    @Override
    public int getZeroMonthPeriod() {
        return period;
    }

    /**
     * Getter for the previous period
     *
     * @return the previous month
     */
    @Override
    public int getPreviousPeriod() {
        return period - 100;
    }

}
