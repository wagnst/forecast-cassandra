package fourschlag.entities.types;

/**
 * Provides functionality for the Period
 */

public class Period {

    protected int year;
    protected int month;
    protected int period;

    /**
     * Constructor for Period
     *
     * @param year  Year that will be used
     * @param month Month that will be used
     */
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

    /**
     * Constructor for Period
     *
     * @param p Period Object that contains all data for the period
     */
    public Period(Period p) {
        this.year = p.year;
        this.month = p.month;
        this.period = p.period;
    }

    /**
     * Constructor for Period
     *
     * @param period Period as an integer value
     */
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

    /**
     * method to get the period by year
     *
     * @param year Year that is currently used
     * @return Period Object whith the ....
     */
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

    /**
     * method to get the first period of the year
     *
     * @return Period Object ...
     */
    public Period getFirstPeriodOfYear() {
        return new Period(year * 100 + 1);
    }

    /**
     * method to get the ....
     *
     * @return
     */
    public int getZeroMonthPeriod() {
        /* Multiply year with 100 to get zeroMonthPeriod. Example: 2015 --> 201500 */
        return year * 100;
    }

    /**
     * method to get the previous perid
     *
     * @return ..
     */
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

    public int getMonth() {
        return month;
    }

    public int getPeriod() {
        return period;
    }

    @Override
    public String toString() {
        return "Periode: " + period;
    }
}
