package fourschlag.entities.mysqlTables.keys;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ForecastSalesKey implements Serializable{
    @Column(name = "period")
    private int period;

    @Column(name = "region")
    private String region;

    @Column(name = "product_main_group")
    private String productMainGroup;

    @Column(name = "sales_type")
    private String salesType;

    @Column(name = "plan_period")
    private int planPeriod;

    @Column(name = "entry_type")
    private String entryType;

    public int getPeriod() {
        return period;
    }

    public String getRegion() {
        return region;
    }

    public String getProductMainGroup() {
        return productMainGroup;
    }

    public String getSalesType() {
        return salesType;
    }

    public int getPlanPeriod() {
        return planPeriod;
    }

    public String getEntryType() {
        return entryType;
    }
}
