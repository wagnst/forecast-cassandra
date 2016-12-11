package fourschlag.entities.types;


/**
 * Extends Period. Sets the initial Month.
 */
public class ZeroMonthPeriod extends Period {

    public ZeroMonthPeriod(Period period) {
        super(period.getZeroMonthPeriod());
    }

    public ZeroMonthPeriod(int year) {
        super(year, 0);
    }

    @Override
    protected boolean isMonthValid(int month) {
        return (month == 0);
    }

    /**
     * Increments the initial Month
     *
     * @return incremented Period
     */
    @Override
    public ZeroMonthPeriod increment() {
        this.year++;
        this.period += 100;
        return this;
    }

    @Override
    public ZeroMonthPeriod incrementMultipleTimes(int multiplier) {
        for (int i = 0; i < multiplier; i++) {
            increment();
        }
        return this;
    }

    /**
     * Getter for the ZeroMonthPeriod
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
