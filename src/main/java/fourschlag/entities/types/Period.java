package fourschlag.entities.types;

/**
 * Class that contains information about a period with year and month
 */
public class Period {
    protected int year;
    protected int month;
    /* The attribute period is always in the format YYYYMM -> example: 201609 */
    protected int period;

    /**
     * Constructor for Period
     *
     * @param year  Year that will be used
     * @param month Month that will be used
     */
    public Period(int year, int month) {
        /* IF year is not valid THEN throw an IllegalArgumentException */
        if (!isYearValid(year)) {
            throw new IllegalArgumentException("Year must be between 1900 and 2100...");
        }
        /* IF month is not valid THEN throw an IllegalArgumentException */
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
     * Creates an Period instance from a year. The month is set to 1.
     *
     * @param year year to create the instance from
     * @return Period instance
     */
    public static Period getPeriodByYear(int year) {
        return new Period(year, 1);
    }

    /**
     * Checks if a value is a valid year. Year must be between 1900 and 2099.
     *
     * @param year year to be checked
     * @return True if year is valid; False if year is not valid
     */
    private boolean isYearValid(int year) {
        return (year > 1900 && year < 2100);
    }

    /**
     * Checks if a value is valid year. Month must be between 1 and 12
     *
     * @param month month to be checked
     * @return True if month is valid; False if month is not valid
     */
    protected boolean isMonthValid(int month) {
        return (month > 0 && month < 13);
    }

    /**
     * Checks if a period is valid. Period must be a six digit integer.
     *
     * @param period period to be checked
     * @return True if period is valid; False if period is not valid
     */
    private boolean isPeriodValid(int period) {
        return (period > 100000 && period < 999999);
    }

    /**
     * Increments the current Period in the smallest possible way.
     * WARNING: THIS METHOD IS NOT IMMUTABLE
     *
     * @return this
     */
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
     * Increments the current period and creates a new instance so the current period is not changed.
     *
     * @return instance of Period
     */
    public Period immutableIncrement() {
        if (this.month < 12) {
            return new Period(year, month);
        } else {
            return new Period(year + 1, 1);
        }
    }

    /**
     * Increments the period multiple times.
     * WARNING: THIS METHOD IS NOT IMMUTABLE
     *
     * @param multiplier number of increments
     * @return this
     */
    public Period incrementMultipleTimes(int multiplier) {
        for (int i = 0; i < multiplier; i++) {
            increment();
        }
        return this;
    }

    /**
     * Increments the period multiple times but does not change this instance
     *
     * @param multiplier number of increments
     * @return new instance of Period
     */
    public Period immutableIncrementMultipleTimes(int multiplier) {
        Period tempPeriod = this;
        for (int i = 0; i < multiplier; i++) {
            tempPeriod = immutableIncrement();
        }
        return tempPeriod;
    }

    /**
     * Calculates a period where the month is 00
     *
     * @return period with month == 00
     */
    public int getZeroMonthPeriod() {
        /* Multiply year with 100 to get zeroMonthPeriod. Example: 2015 --> 201500 */
        return year * 100;
    }

    /**
     * Calculates the previous method
     *
     * @return previous method
     */
    public int getPreviousPeriod() {
        if (month > 1) {
            return period - 1;
        } else {
            return period - 89;
        }
    }

    public int getHalfYear() {
        if (month <= 6) {
            return 1;
        } else {
            return 2;
        }
    }

    public int getQuarter() {
        if (month <= 3) {
            return 1;
        } else if (month <= 6) {
            return 2;
        } else if (month <= 9) {
            return 3;
        } else {
            return 4;
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