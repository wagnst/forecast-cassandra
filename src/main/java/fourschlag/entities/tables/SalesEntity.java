package fourschlag.entities.tables;


import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;

import java.util.UUID;

public class SalesEntity extends Entity {


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

    public String getProductMainGroup() {
        return productMainGroup;
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

}
