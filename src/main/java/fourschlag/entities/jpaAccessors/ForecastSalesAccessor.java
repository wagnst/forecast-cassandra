package fourschlag.entities.jpaAccessors;

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
                "select new ForecastSalesEntity(e.salesVolumes, e.netSales, e.cm1, e.topdownAdjustSalesVolumes," +
                        " e.topdownAdjustNetSales, e.topdownAdjustCm1, e.currency) " +
                        "from ForecastSalesEntity e " +
                        "where e.primaryKey.productMainGroup = :productMainGroup " +
                        "and e.primaryKey.period = :period " +
                        "and e.primaryKey.planPeriod = :planPeriod " +
                        "and e.primaryKey.region = :region " +
                        "and e.primaryKey.salesType = :salesType " +
                        "and e.primaryKey.entryType = :entryType", ForecastSalesEntity.class);

        query.setParameter("productMainGroup", productMainGroup);
        query.setParameter("period", period);
        query.setParameter("planPeriod", planPeriod);
        query.setParameter("region", region);
        query.setParameter("salesType", salesType);
        query.setParameter("entryType", entryType);

        return (ForecastSalesEntity) query.getSingleResult();
    }

    public ForecastSalesEntity getCm1(
            String productMainGroup,
            int period,
            int planPeriod,
            String region,
            String salesType) {

        Query query = getEntityManager().createQuery(
                "select new ForecastSalesEntity(e.cm1, e.topdownAdjustCm1, e.currency) " +
                        "from ForecastSalesEntity e " +
                        "where e.primaryKey.productMainGroup = :productMainGroup " +
                        "and e.primaryKey.period = :period " +
                        "and e.primaryKey.planPeriod = :planPeriod " +
                        "and e.primaryKey.region = :region " +
                        "and e.primaryKey.salesType = :salesType " +
                        "and e.primaryKey.entryType = 'forecast'", ForecastSalesEntity.class);

        query.setParameter("productMainGroup", productMainGroup);
        query.setParameter("period", period);
        query.setParameter("planPeriod", planPeriod);
        query.setParameter("region", region);
        query.setParameter("salesType", salesType);

        return (ForecastSalesEntity) query.getSingleResult();
    }

    public List<ForecastSalesEntity> getDistinctPmgAndRegions() {
        Query query = getEntityManager().createQuery(
                "select distinct new ForecastSalesEntity(e.primaryKey.productMainGroup, e.primaryKey.region) " +
                        "from ForecastSalesEntity e", ForecastSalesEntity.class);

        return query.getResultList();
    }
}
