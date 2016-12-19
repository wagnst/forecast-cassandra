package fourschlag.entities.jpaAccessors;

import fourschlag.entities.jpaTables.ForecastFixedCostsEntity;
import fourschlag.services.db.JpaConnection;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

public class ForecastFixedCostsAccessor extends Accessor {

    public ForecastFixedCostsAccessor(JpaConnection connection) {
        super(connection);
    }

    public ForecastFixedCostsEntity getFixedCostsKpis(
            String sbu,
            String subregion,
            int period,
            int planPeriod,
            String entryType) {

        EntityManager manager = createEntityManager();

        Query query = manager.createQuery(
                "select new ForecastFixedCostsEntity(e.fixPreManCost, e.shipCost, e.sellCost, e.diffActPreManCost, " +
                        "e.idleEquipCost, e.rdCost, e.adminCostBu, e.adminCostOd, e.adminCostCompany, e.otherOpCostBu," +
                        " e.otherOpCostOd, e.otherOpCostCompany, e.specItems, e.provisions, e.currencyGains," +
                        " e.valAdjustInventories, e.otherFixCost, e.depreciation, e.capCost, e.equityIncome," +
                        " e.topdownAdjustFixCosts, e.currency) " +
                        "from ForecastFixedCostsEntity e " +
                        "where e.primaryKey.sbu = :sbu " +
                        "and e.primaryKey.subregion = :subregion " +
                        "and e.primaryKey.period = :period " +
                        "and e.primaryKey.planPeriod = :planPeriod " +
                        "and e.primaryKey.entryType = :entryType", ForecastFixedCostsEntity.class);

        query.setParameter("sbu", sbu);
        query.setParameter("subregion", subregion);
        query.setParameter("period", period);
        query.setParameter("planPeriod", planPeriod);
        query.setParameter("entryType", entryType);

        try {
            return (ForecastFixedCostsEntity) query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } finally {
            manager.close();
        }
    }

    public List<ForecastFixedCostsEntity> getDistinctSbuAndSubregions() {
        EntityManager manager = createEntityManager();

        Query query = manager.createQuery(
                "select distinct new ForecastFixedCostsEntity(e.primaryKey.sbu, e.primaryKey.subregion) " +
                        "from ForecastFixedCostsEntity e", ForecastFixedCostsEntity.class);

        try {
            return query.getResultList();
        } finally {
            manager.close();
        }

    }
}
