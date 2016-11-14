package fourschlag.entities.tables;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.Table;

@Table(name = "exchange_rate")
public class ExchangeRateEntity {
    private int period;
    @Column(name = "period_year")
    private int periodYear;
    @Column(name = "period_month")
    private int periodMonth;
    @Column(name = "from_currency")
    private String fromCurrency;
    @Column(name = "to_currency")
    private String toCurrency;
    private double rate;
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

    public int getPeriod() {
        return period;
    }

    public int getPeriodYear() {
        return periodYear;
    }

    public int getPeriodMonth() {
        return periodMonth;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public double getRate() {
        return rate;
    }

    public String getUserid() {
        return userid;
    }

    public String getEntryTS() {
        return entryTS;
    }
}
