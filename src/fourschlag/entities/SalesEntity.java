package fourschlag.entities;


import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Transient;

import java.util.UUID;

public class SalesEntity {

    /* comment in if uuid is part of primary key
    @PartitionKey
    @Column(name = "uuid")
        */
    @Transient
    private UUID uuid;

    @PartitionKey
    @Column(name = "product_main_group")
    private String productMainGroup;

    @PartitionKey(1)
    @Column(name = "period")
    private int period;

    @PartitionKey(2)
    @Column(name = "region")
    private String region;

    @PartitionKey(3)
    @Column(name = "sales_type")
    private String salesType;

    @Column(name = "sales_volumes")
    private double salesVolumes;

    @Column(name = "net_sales")
    private double netSales;

    @Column(name = "cm1")
    private double cm1;

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

    public SalesEntity() {
    }

    public SalesEntity(UUID uuid, double salesVolumes, double netSales, double cm1,
                       String productMainGroup, String region, String salesType,
                       int period, int periodYear, int periodMonth,
                       String currency, String userId, String entryTs) {
        this.uuid = uuid;
        this.salesVolumes = salesVolumes;
        this.netSales = netSales;
        this.cm1 = cm1;
        this.productMainGroup = productMainGroup;
        this.region = region;
        this.salesType = salesType;
        this.period = period;
        this.periodYear = periodYear;
        this.periodMonth = periodMonth;
        this.currency = currency;
        this.userId = userId;
        this.entryTs = entryTs;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getProductMainGroup() {
        return productMainGroup;
    }

    public int getPeriod() {
        return period;
    }

    public String getRegion() {
        return region;
    }

    public String getSalesType() {
        return salesType;
    }

    public double getSalesVolumes() {
        return salesVolumes;
    }

    public double getNetSales() {
        return netSales;
    }

    public double getCm1() {
        return cm1;
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
