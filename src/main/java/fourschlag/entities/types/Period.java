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
     * @param year Year that will be used
     * @param month Month that will be used
     */
    public Period(int year, int month) {
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
        this.period = period;
        this.year = period / 100;
        this.month = period - this.year * 100;
    }

    /**
     * method to get the period by year
     *
     * @param year Year that is currently used
     *
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
     * @return
     */
    public int getZeroMonthPeriod() {
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

    /**
     * toString method ....
     *
     * @return
     */
    @Override
    public String toString() {
        return "Periode: " + period;
    }
}
