package fourschlag.entities.jpaTables;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class KpiEntity {
    @Column(name = "currency")
    private String currency;

    public KpiEntity() {
    }

    public KpiEntity(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }
}
