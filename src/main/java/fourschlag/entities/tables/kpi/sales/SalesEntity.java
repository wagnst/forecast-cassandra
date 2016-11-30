package fourschlag.entities.tables.kpi.sales;


import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import fourschlag.entities.tables.kpi.KpiEntity;

import java.util.UUID;

/**
 * Super class SalesEntity. Extends KpiEntity. Provides the data from the Sales table
 */
public class SalesEntity extends KpiEntity {


    @PartitionKey
    @Column(name = "product_main_group")
    private String productMainGroup;

    @Column(name = "sales_type")
    private String salesType;

    @Column(name = "sales_volumes")
    private double salesVolumes;

    @Column(name = "net_sales")
    private double netSales;

    @Column(name = "cm1")
    private double cm1;



    public SalesEntity() {
    }


    public SalesEntity(UUID uuid, double salesVolumes, double netSales, double cm1,
                       String productMainGroup, String region, String salesType,
                       int period, int periodYear, int periodMonth,
                       String currency, String userId, String entryTs) {
        super(uuid, period, region, periodYear, periodMonth, currency, userId, entryTs);
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
