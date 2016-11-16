package fourschlag.entities.types;

public class Period {

    private int year;
    private int month;
    private int period;

    public Period(int year, int month) {
        this.year = year;
        this.month = month;
        this.period = year * 100 + month;
    }

    public Period(Period p){
        this.year = p.year;
        this.month = p.month;
        this.period = p.period;
    }

    public Period(int period) {
        this.period = period;
        this.year = period / 100;
        this.month = period - this.year * 100;
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
        return year*100;
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
