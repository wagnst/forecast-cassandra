package fourschlag.entities.jpaTables;

import fourschlag.entities.jpaTables.keys.ActualSalesKey;

import javax.persistence.*;


@Entity
@Table (name = "actual_sales")
public class ActualSalesEntity extends SalesEntity {
    @EmbeddedId
    private ActualSalesKey primaryKey;

    @Column(name = "period_half_year")
    private int periodHalfYear;

    @Column(name = "period_quarter")
    private int periodQuarter;

    @Column(name = "period_year")
    private int periodYear;

    @Column(name = "period_month")
    private int periodMonth;

    @Column(name = "userid")
    private String userId;

    @Column(name = "entry_ts")
    private String entryTs;

    public ActualSalesEntity() {
    }

    public ActualSalesKey getPrimaryKey() {
        return primaryKey;
    }

    public int getPeriodHalfYear() {
        return periodHalfYear;
    }

    public int getPeriodQuarter() {
        return periodQuarter;
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
}
