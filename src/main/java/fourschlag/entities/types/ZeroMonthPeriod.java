package fourschlag.entities.types;


/**
 * Extends Period. The month is always 00.
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

    @Override
    public ZeroMonthPeriod increment() {
        this.year++;
        this.period += 100;
        return this;
    }

    @Override
    public ZeroMonthPeriod immutableIncrement() {
        return new ZeroMonthPeriod(year + 1);
    }

    @Override
    public ZeroMonthPeriod incrementMultipleTimes(int multiplier) {
        for (int i = 0; i < multiplier; i++) {
            increment();
        }
        return this;
    }

    @Override
    public ZeroMonthPeriod immutableIncrementMultipleTimes(int multiplier) {
        ZeroMonthPeriod tempPeriod = this;
        for (int i = 0; i < multiplier; i++) {
            tempPeriod = immutableIncrement();
        }
        return tempPeriod;
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
     * @return the previous period
     */
    @Override
    public int getPreviousPeriod() {
        return period - 100;
    }

}
