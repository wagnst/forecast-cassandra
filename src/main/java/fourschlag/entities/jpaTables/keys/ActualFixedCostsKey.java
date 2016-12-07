package fourschlag.entities.jpaTables.keys;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ActualFixedCostsKey implements Serializable {
    @Column(name = "period")
    private int period;

    @Column(name = "sbu")
    private String sbu;

    @Column(name = "subregion")
    private String subregion;

    public ActualFixedCostsKey() {
    }

    public ActualFixedCostsKey(String sbu, String subregion) {
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
}
