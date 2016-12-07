package fourschlag.entities.jpaTables.keys;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ActualSalesKey implements Serializable {
    @Column(name = "data_source", nullable = false)
    private String dataSource;

    @Column(name = "product_main_group", nullable = false)
    private String productMainGroup;

    @Column(name = "sales_type", nullable = false)
    private String salesType;

    @Column(name = "period", nullable = false)
    private int period;

    @Column(name = "region", nullable = false)
    private String region;

    public ActualSalesKey(String productMainGroup, String region) {
        this.productMainGroup = productMainGroup;
        this.region = region;
    }

    public ActualSalesKey() {}

    public String getDataSource() {
        return dataSource;
    }

    public String getProductMainGroup() {
        return productMainGroup;
    }

    public String getSalesType() {
        return salesType;
    }

    public int getPeriod() {
        return period;
    }

    public String getRegion() {
        return region;
    }
}
