package fourschlag.entities.types;

public class ZeroMonthPeriod extends Period {
    public ZeroMonthPeriod(Period period) {
        super(period.getZeroMonthPeriod());
    }

    public ZeroMonthPeriod(int year) {
        super(year, 0);
    }

    @Override
    public Period increment() {
        this.year++;
        this.period += 100;
        return this;
    }

    @Override
    public int getZeroMonthPeriod() {
        return period;
    }

    @Override
    public int getPreviousPeriod() {
        return period - 100;
    }

}
