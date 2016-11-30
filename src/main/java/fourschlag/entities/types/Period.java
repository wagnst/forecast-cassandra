package fourschlag.entities.types;

public class Period {

    protected int year;
    protected int month;
    protected int period;

    private boolean isYearValid(int year) {
        return (year > 1900 && year < 2100);
    }

    protected boolean isMonthValid(int month) {
        return (month > 0 && month < 13);
    }

    private boolean isPeriodValid(int period) {
        return (period > 100000 && period < 999999);
    }

    public Period(int year, int month) {
        if (!isYearValid(year)) {
            throw new IllegalArgumentException("Year must be between 1900 and 2100...");
        }
        if (!isMonthValid(month)) {
            throw new IllegalArgumentException("Month must a number between 1 and 12...");
        }
        this.year = year;
        this.month = month;
        this.period = year * 100 + month;
    }

    public Period(Period p) {
        this.year = p.year;
        this.month = p.month;
        this.period = p.period;
    }

    public Period(int period) {
        if (!isPeriodValid(period)) {
            throw new IllegalArgumentException("Period must be a 6 digit number, where the first 4 digits represent the" +
                    "year and the last 2 digits represent the month");
        }
        int tempYear = period / 100;
        if (!isYearValid(tempYear)) {
            throw new IllegalArgumentException("Year must be between 1900 and 2100...");
        }
        int tempMonth = period - tempYear * 100;
        if (!isMonthValid(tempMonth)) {
            throw new IllegalArgumentException("Month must a number between 1 and 12...");
        }
        this.year = tempYear;
        this.month = tempMonth;
        this.period = period;
    }

    public static Period getPeriodByYear(int year) {
        return new Period(year, 1);
    }

    public Period increment() {
        if (this.month < 12) {
            this.month++;
            this.period++;
        } else {
            this.year++;
            this.month = 1;
            this.period = year * 100 + month;
        }
        return this;
    }

    public Period getFirstPeriodOfYear() {
        return new Period(year * 100 + 1);
    }

    public int getZeroMonthPeriod() {
        return year * 100;
    }

    public int getPreviousPeriod() {
        if (month > 1) {
            return period - 1;
        } else {
            return period - 89;
        }
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    @Override
    public String toString() {
        return "Periode: " + period;
    }
}
