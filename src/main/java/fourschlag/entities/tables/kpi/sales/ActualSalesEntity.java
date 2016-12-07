package fourschlag.entities.tables.kpi.sales;

import java.util.UUID;

/**
 * Extends SalesEntity. Provides the data from the ActualSales table
 */
public class ActualSalesEntity extends SalesEntity {

    private String dataSource;

    private String sbu;

    private int periodHalfYear;

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

    /**
     * Getter for the DataSource
     *
     * @return DataSource that is currently used
     */
    public String getDataSource() {
        return dataSource;
    }

    /**
     * Getter for the SBU
     *
     * @return SBU that is currently used
     */
    public String getSbu() {
        return sbu;
    }

    /**
     * Getter for the PeriodHalfYear
     *
     * @return PeriodHalfYear that is currently used
     */
    public int getPeriodHalfYear() {
        return periodHalfYear;
    }

    /**
     * Getter for the PeriodQuarter
     *
     * @return PeriodQuarter that is currently used
     */
    public int getPeriodQuarter() {
        return periodQuarter;
    }

}