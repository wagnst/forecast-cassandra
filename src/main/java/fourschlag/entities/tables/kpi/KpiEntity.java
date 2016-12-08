package fourschlag.entities.tables.kpi;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.Transient;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Super class KpiEntity. Defines the SalesKPIs
 */

public class KpiEntity {
    /* comment in if uuid is part of primary key
@PartitionKey
@Column(name = "uuid")
    */
    @Transient
    private UUID uuid;

    @Column(name = "period")
    @JsonProperty("PERIOD")
    private int period;

    @Column(name = "region")
    @JsonProperty("REGION")
    private String region;

    @Column(name = "period_year")
    @JsonProperty("PERIOD_YEAR")
    private int periodYear;

    @Column(name = "period_month")
    @JsonProperty("PERIOD_MONTH")
    private int periodMonth;

    @Column(name = "currency")
    @JsonProperty("CURRENCY")
    private String currency;

    @Column(name = "userid")
    @JsonProperty("USERID")
    private String userId;

    @Column(name = "entry_ts")
    @JsonProperty("ENTRY_TS")
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

    /**
     * Getter for the Period
     *
     * @return Period that is currently used
     */
    public int getPeriod() {
        return period;
    }

    /**
     * Getter for the Region
     *
     * @return Region that is currently used
     */
    public String getRegion() {
        return region;
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
     * Getter for the Currency
     *
     * @return Currency that is currently used
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Getter for the UserId
     *
     * @return UserId that is currently used
     */

    public String getUserId() {
        return userId;
    }

    /**
     * Getter for the EntryTs
     *
     * @return EntryTs that is currently used
     */
    public String getEntryTs() {
        return entryTs;
    }
}
