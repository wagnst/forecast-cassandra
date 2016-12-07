package fourschlag.entities.jpaTables;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class SalesEntity extends KpiEntity {
    @Column(name = "sales_volumes")
    private double salesVolumes;

    @Column(name = "net_sales")
    private double netSales;

    @Column(name = "cm1")
    private double cm1;

    public SalesEntity() {
    }

    public SalesEntity(double salesVolumes, double netSales, double cm1, String currency) {
        super(currency);
        this.salesVolumes = salesVolumes;
        this.netSales = netSales;
        this.cm1 = cm1;
    }

    public SalesEntity(double cm1, String currency) {
        super(currency);
        this.cm1 = cm1;
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
