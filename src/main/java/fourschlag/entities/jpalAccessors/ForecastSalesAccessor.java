package fourschlag.entities.jpalAccessors;

import fourschlag.entities.jpaTables.ForecastSalesEntity;

import javax.persistence.Query;
import java.util.List;

public class ForecastSalesAccessor extends Accessor {
    public ForecastSalesEntity getSalesKpis(
            String productMainGroup,
            int period,
            int planPeriod,
            String region,
            String salesType,
            String entryType) {

        Query query = getEntityManager().createQuery(
                "select e.salesVolumes, e.netSales, e.cm1, e.topdownAdjustSalesVolumes, e.topdownAdjustNetSales, e.topdownAdjustCm1, e.currency " +
                        "from ForecastSalesEntity e " +
                        "where e.primaryKey.productMainGroup = '" + productMainGroup + "' " +
                        "and e.primaryKey.period = " + period + " " +
                        "and e.primaryKey.planPeriod = " + planPeriod + " " +
                        "and e.primaryKey.region = '" + region + "' " +
                        "and e.primaryKey.salesType = '" + salesType + "' " +
                        "and e.primaryKey.entryType = '" + entryType + "'");

        return (ForecastSalesEntity) query.getSingleResult();
    }

    public ForecastSalesEntity getCm1(
            String productMainGroup,
            int period,
            int planPeriod,
            String region,
            String salesType) {

        Query query = getEntityManager().createQuery(
                "select e.cm1, e.topdownAdjustCm1, e.currency " +
                        "from ForecastSalesEntity e " +
                        "where e.primaryKey.productMainGroup = '" + productMainGroup + "' " +
                        "and e.primaryKey.period = " + period + " " +
                        "and e.primaryKey.planPeriod = " + planPeriod + " " +
                        "and e.primaryKey.region = '" + region + "' " +
                        "and e.primaryKey.salesType = '" + salesType + "' " +
                        "and e.primaryKey.entryType = 'forecast'");

        return (ForecastSalesEntity) query.getSingleResult();
    }

    public List<ForecastSalesEntity> getDistinctPmgAndRegions() {
        Query query = getEntityManager().createQuery("select distinct e.primaryKey.productMainGroup, e.primaryKey.region from ForecastSalesEntity e");

        return (List<ForecastSalesEntity>) query.getResultList();
    }
}
