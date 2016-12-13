package fourschlag.entities.tables.kpi.sales;


import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import fourschlag.entities.tables.kpi.KpiEntity;

/**
 * Super class SalesEntity. Extends KpiEntity.
 */
public class SalesEntity extends KpiEntity {
    @PartitionKey
    @Column(name = "product_main_group")
    @JsonProperty("PRODUCT_MAIN_GROUP")
    private String productMainGroup;

    @Column(name = "sales_type")
    @JsonProperty("SALES_TYPE")
    private String salesType;

    @Column(name = "sales_volumes")
    @JsonProperty("SALES_VOLUMES")
    private double salesVolumes;

    @Column(name = "net_sales")
    @JsonProperty("NET_SALES")
    private double netSales;

    @Column(name = "cm1")
    @JsonProperty("CM1")
    private double cm1;

    public SalesEntity() {
    }

    public SalesEntity(double salesVolumes, double netSales, double cm1,
                       String productMainGroup, String region, String salesType,
                       int period, int periodYear, int periodMonth,
                       String currency, String userId, String entryTs) {
        super(period, region, periodYear, periodMonth, currency, userId, entryTs);
        this.salesVolumes = salesVolumes;
        this.netSales = netSales;
        this.cm1 = cm1;
        this.productMainGroup = productMainGroup;
        this.salesType = salesType;
    }

    /**
     * Getter for the ProductMainGroup
     *
     * @return ProductMainGroup that is currently used
     */
    public String getProductMainGroup() {
        return productMainGroup;
    }

    /**
     * Getter for the SalesType
     *
     * @return SalesType that is currently used
     */
    public String getSalesType() {
        return salesType;
    }

    /**
     * Getter for the SalesVolumes
     *
     * @return SalesVolumes that are currently used
     */
    public double getSalesVolumes() {
        return salesVolumes;
    }

    /**
     * Getter for the NetSales
     *
     * @return NetSales that are currently used
     */
    public double getNetSales() {
        return netSales;
    }

    /**
     * Getter for the Cm1
     *
     * @return Cm1 that is currently used
     */
    public double getCm1() {
        return cm1;
    }

    /**
     * Setter for the Cm1
     *
     * @param cm1 double value to be set as cm1
     */
    public void setCm1(double cm1) {
        this.cm1 = cm1;
    }

}
