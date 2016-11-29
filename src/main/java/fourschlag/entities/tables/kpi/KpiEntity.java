package fourschlag.entities.tables.kpi;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.Transient;

import java.util.UUID;

/**
 * Created by thor on 23.11.2016.
 */
public class KpiEntity {
    /* comment in if uuid is part of primary key
@PartitionKey
@Column(name = "uuid")
    */
    @Transient
    private UUID uuid;

    @Column(name = "period")
    private int period;

    @Column(name = "region")
    private String region;

    @Column(name = "period_year")
    private int periodYear;

    @Column(name = "period_month")
    private int periodMonth;

    @Column(name = "currency")
    private String currency;

    @Column(name = "userid")
    private String userId;

    @Column(name = "entry_ts")
    private String entryTs;

    public KpiEntity() {
    }

    public KpiEntity(UUID uuid, int period, String region, int periodYear, int periodMonth, String currency, String userId, String entryTs) {
        this.uuid = uuid;
        this.period = period;
        this.region = region;
        this.periodYear = periodYear;
        this.periodMonth = periodMonth;
        this.currency = currency;
        this.userId = userId;
        this.entryTs = entryTs;
    }

    public int getPeriod() {
        return period;
    }

    public String getRegion() {
        return region;
    }

    public int getPeriodYear() {
        return periodYear;
    }

    public int getPeriodMonth() {
        return periodMonth;
    }

    public String getCurrency() {
        return currency;
    }

    public String getUserId() {
        return userId;
    }

    public String getEntryTs() {
        return entryTs;
    }
}
