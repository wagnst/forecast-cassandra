package fourschlag.entities.jpaTables.keys;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ForecastFixedCostsKey implements Serializable {
    @Column(name = "period")
    private int period;

    @Column(name = "sbu")
    private String sbu;

    @Column(name = "subregion")
    private String subregion;

    @Column(name = "plan_period")
    private int planPeriod;

    @Column(name = "entry_type")
    private String entryType;

    public ForecastFixedCostsKey() {
    }

    public ForecastFixedCostsKey(String sbu, String subregion) {
        this.sbu = sbu;
        this.subregion = subregion;
    }

    public int getPeriod() {
        return period;
    }

    public String getSbu() {
        return sbu;
    }

    public String getSubregion() {
        return subregion;
    }

    public int getPlanPeriod() {
        return planPeriod;
    }

    public String getEntryType() {
        return entryType;
    }
}
