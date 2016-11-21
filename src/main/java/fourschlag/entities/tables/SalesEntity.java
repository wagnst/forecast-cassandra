package fourschlag.entities.tables;


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

    public void setCm1(double cm1) {
        this.cm1 = cm1;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SalesEntity that = (SalesEntity) o;

        if (period != that.period) return false;
        if (Double.compare(that.salesVolumes, salesVolumes) != 0) return false;
        if (Double.compare(that.netSales, netSales) != 0) return false;
        if (Double.compare(that.cm1, cm1) != 0) return false;
        if (periodYear != that.periodYear) return false;
        if (periodMonth != that.periodMonth) return false;
        if (uuid != null ? !uuid.equals(that.uuid) : that.uuid != null) return false;
        if (productMainGroup != null ? !productMainGroup.equals(that.productMainGroup) : that.productMainGroup != null)
            return false;
        if (region != null ? !region.equals(that.region) : that.region != null) return false;
        if (salesType != null ? !salesType.equals(that.salesType) : that.salesType != null) return false;
        if (currency != null ? !currency.equals(that.currency) : that.currency != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        return entryTs != null ? entryTs.equals(that.entryTs) : that.entryTs == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (productMainGroup != null ? productMainGroup.hashCode() : 0);
        result = 31 * result + period;
        result = 31 * result + (region != null ? region.hashCode() : 0);
        result = 31 * result + (salesType != null ? salesType.hashCode() : 0);
        temp = Double.doubleToLongBits(salesVolumes);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(netSales);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(cm1);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + periodYear;
        result = 31 * result + periodMonth;
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (entryTs != null ? entryTs.hashCode() : 0);
        return result;
    }
}
