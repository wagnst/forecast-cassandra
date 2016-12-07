package fourschlag.entities.jpaAccessors;

import fourschlag.entities.jpaTables.ActualSalesEntity;

import javax.persistence.Query;
import java.util.List;

public class ActualSalesAccessor extends Accessor {

    public ActualSalesEntity getSalesKPIs(
            String productMainGroup,
            int period,
            String region,
            String salesType,
            String dataSource) {

        Query query = getEntityManager().createQuery(
                "select new ActualSalesEntity(e.salesVolumes, e.netSales, e.cm1, e.currency) from ActualSalesEntity e " +
                        "where e.primaryKey.productMainGroup = :productMainGroup " +
                        "and e.primaryKey.period = :period " +
                        "and e.primaryKey.region = :region " +
                        "and e.primaryKey.salesType = :salesType " +
                        "and e.primaryKey.dataSource = :dataSource", ActualSalesEntity.class);

        query.setParameter("productMainGroup", productMainGroup);
        query.setParameter("period", period);
        query.setParameter("region", region);
        query.setParameter("salesType", salesType);
        query.setParameter("dataSource", salesType);

        return (ActualSalesEntity) query.getSingleResult();
    }

    public List<ActualSalesEntity> getDistinctPmgAndRegions() {
        Query query = getEntityManager().createQuery(
                "select distinct new ActualSalesEntity(e.primaryKey.productMainGroup, e.primaryKey.region) " +
                        "from ActualSalesEntity e", ActualSalesEntity.class);

        return query.getResultList();
    }
}
