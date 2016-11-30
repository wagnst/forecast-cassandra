package fourschlag.entities.tables;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.Table;

/**
 * Provides the data from the ExchangeRates table
 */

@Table(name = "exchange_rate")
public class ExchangeRateEntity {
    @Column(name = "period")
    private int period;
    @Column(name = "period_year")
    private int periodYear;
    @Column(name = "period_month")
    private int periodMonth;
    @Column(name = "from_currency")
    private String fromCurrency;
    @Column(name = "to_currency")
    private String toCurrency;
    @Column(name = "rate")
    private double rate;
    @Column(name = "userid")
    private String userid;
    @Column(name = "entry_ts")
    private String entryTS;


    public ExchangeRateEntity() {
    }


    public ExchangeRateEntity(int period, int periodYear, int periodMonth, String fromCurrency, String toCurrency,
                              double rate, String userid, String entryTS) {
        this.period = period;
        this.periodYear = periodYear;
        this.periodMonth = periodMonth;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.rate = rate;
        this.userid = userid;
        this.entryTS = entryTS;
    }

    /**
     * Getter for the Period
     *
     * @return Period that is currently used
     */
    public int getPeriod() {
        return period;
    }

    /**
     * Getter for the PeriodYear
     *
     * @return PeriodYear that is currently used
     */
    public int getPeriodYear() {
        return periodYear;
    }

    /**
     * Getter for the PeriodMonth
     *
     * @return PeriodMonth that is currently used
     */
    public int getPeriodMonth() {
        return periodMonth;
    }

    /**
     * Getter for the FromCurrency
     *
     * @return FromCurrency that is currently used
     */
    public String getFromCurrency() {
        return fromCurrency;
    }

    /**
     * Getter for the toCurrency
     *
     * @return toCurrency that is currently used
     */
    public String getToCurrency() {
        return toCurrency;
    }

    /**
     * Getter for the Rate
     *
     * @return Rate that is currently used
     */
    public double getRate() {
        return rate;
    }

    /**
     * Getter for the Userid
     *
     * @return Userid that is currently used
     */
    public String getUserid() {
        return userid;
    }

    /**
     * Getter for the EntryTS
     *
     * @return EntryTS that is currently used
     */
    public String getEntryTS() {
        return entryTS;
    }
}
