package fourschlag.entities.jpaAccessors;

import fourschlag.entities.jpaTables.ActualFixedCostsEntity;
import fourschlag.services.db.JpaConnection;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

public class ActualFixedCostsAccessor extends Accessor {

    public ActualFixedCostsAccessor(JpaConnection connection) {
        super(connection);
    }

    public ActualFixedCostsEntity getFixedCostsKpis(
            String sbu,
            String subregion,
            int period) {

        Query query = getEntityManagerFactory().createEntityManager().createQuery(
                "select new ActualFixedCostsEntity(e.fixPreManCost, e.shipCost, e.sellCost, e.diffActPreManCost, " +
                        "e.idleEquipCost, e.rdCost, e.adminCostBu, e.adminCostOd, e.adminCostCompany, e.otherOpCostBu," +
                        " e.otherOpCostOd, e.otherOpCostCompany, e.specItems, e.provisions, e.currencyGains," +
                        " e.valAdjustInventories, e.otherFixCost, e.depreciation, e.capCost, e.equityIncome, e.currency) " +
                        "from ActualFixedCostsEntity e " +
                        "where e.primaryKey.sbu = :sbu " +
                        "and e.primaryKey.subregion = :subregion " +
                        "and e.primaryKey.period = :period", ActualFixedCostsEntity.class);

        query.setParameter("sbu", sbu);
        query.setParameter("subregion", subregion);
        query.setParameter("period", period);

        try {
            return (ActualFixedCostsEntity) query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }

    }

    public List<ActualFixedCostsEntity> getDistinctSbuAndSubregions() {
        Query query = getEntityManagerFactory().createEntityManager().createQuery(
                "select distinct new ActualFixedCostsEntity(e.primaryKey.sbu, e.primaryKey.subregion) " +
                        "from ActualFixedCostsEntity e", ActualFixedCostsEntity.class);

        return query.getResultList();
    }
}
