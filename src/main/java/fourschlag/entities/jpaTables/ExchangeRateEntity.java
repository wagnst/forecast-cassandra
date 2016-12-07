package fourschlag.entities.jpaTables;

import fourschlag.entities.jpaTables.keys.ExchangeRateKey;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "exchange_rate")
public class ExchangeRateEntity {

    @EmbeddedId
    private ExchangeRateKey primaryKey;

    @Column(name = "period_year")
    private int periodYear;

    @Column(name = "period_month")
    private int periodMonth;

    @Column(name = "userid")
    private String userid;

    @Column(name = "entry_ts")
    private String entryTS;

    @Column(name = "rate")
    private double rate;

    public ExchangeRateKey getPrimaryKey() {
        return primaryKey;
    }

    public int getPeriodYear() {
        return periodYear;
    }

    public int getPeriodMonth() {
        return periodMonth;
    }

    public String getUserid() {
        return userid;
    }

    public String getEntryTS() {
        return entryTS;
    }

    public double getRate() {
        return rate;
    }
}
