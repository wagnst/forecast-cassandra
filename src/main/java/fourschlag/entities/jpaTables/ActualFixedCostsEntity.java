package fourschlag.entities.jpaTables;

import fourschlag.entities.jpaTables.keys.ActualFixedCostsKey;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "actual_fixed_costs")
public class ActualFixedCostsEntity extends FixedCostsEntity {
    @EmbeddedId
    private ActualFixedCostsKey primaryKey;

    @Column(name = "region")
    private String region;

    @Column(name = "period_year")
    private int periodYear;

    @Column(name = "period_month")
    private int periodMonth;

    @Column(name = "userid")
    private String userId;

    @Column(name = "entry_ts")
    private String entryTs;

    @Column(name = "period_half_year")
    private int periodHalfYear;

    @Column(name = "period_quarter")
    private int periodQuarter;

    public ActualFixedCostsKey getPrimaryKey() {
        return primaryKey;
    }

    public String getRegion() {
        return region;
    }

    public int getPeriodYear() {
        return periodYear;
    }

    public int getPeriodMonth() {
        return periodMonth;
    }

    public String getUserId() {
        return userId;
    }

    public String getEntryTs() {
        return entryTs;
    }

    public int getPeriodHalfYear() {
        return periodHalfYear;
    }

    public int getPeriodQuarter() {
        return periodQuarter;
    }
}
