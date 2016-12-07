package fourschlag.entities.jpalAccessors;

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
                "select e from ActualSalesEntity e " +
                        "where e.primaryKey.productMainGroup = '" + productMainGroup + "' " +
                        "and e.primaryKey.period = " + period + " " +
                        "and e.primaryKey.region = '" + region + "' " +
                        "and e.primaryKey.salesType = '" + salesType + "' " +
                        "and e.primaryKey.dataSource = '" + dataSource + "'", ActualSalesEntity.class);

        return (ActualSalesEntity) query.getSingleResult();
    }

    public List<ActualSalesEntity> getDistinctPmgAndRegions() {
        Query query = getEntityManager().createQuery(
                "select distinct NEW ActualSalesEntity(e.primaryKey.productMainGroup, e.primaryKey.region) " +
                        "from ActualSalesEntity e", ActualSalesEntity.class);

        return query.getResultList();
    }
}
