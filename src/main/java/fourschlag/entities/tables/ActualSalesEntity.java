package fourschlag.entities.tables;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.Table;

import java.util.UUID;

@Table(name = "actual_sales")
public class ActualSalesEntity extends SalesEntity {

    @Column(name = "data_source")
    private String dataSource;

    @Column(name = "sbu")
    private String sbu;

    @Column(name = "period_half_year")
    private int periodHalfYear;

    @Column(name = "period_quarter")
    private int periodQuarter;

    public ActualSalesEntity() {
    }

    public ActualSalesEntity(UUID uuid, double salesVolumes, double netSales, double cm1, String productMainGroup, String region, String sbu, String salesType, String dataSource, int period, int periodYear, int periodHalfYear, int periodQuarter, int periodMonth, String currency, String userId, String entryTs) {
        super(uuid, salesVolumes, netSales, cm1, productMainGroup, region, salesType,
                period, periodYear, periodMonth, currency, userId, entryTs);
        this.sbu = sbu;
        this.dataSource = dataSource;
        this.periodHalfYear = periodHalfYear;
        this.periodQuarter = periodQuarter;
    }

    public String getDataSource() {
        return dataSource;
    }

    public String getSbu() {
        return sbu;
    }

    public int getPeriodHalfYear() {
        return periodHalfYear;
    }

    public int getPeriodQuarter() {
        return periodQuarter;
    }

}