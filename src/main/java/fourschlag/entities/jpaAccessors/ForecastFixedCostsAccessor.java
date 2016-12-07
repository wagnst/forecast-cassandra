package fourschlag.entities.jpaAccessors;

import fourschlag.entities.jpaTables.ForecastFixedCostsEntity;

import javax.persistence.Query;
import java.util.List;

public class ForecastFixedCostsAccessor extends Accessor {

    public ForecastFixedCostsEntity getFixedCostsKpis(
            String sbu,
            String subregion,
            int period,
            int planPeriod,
            String entryType) {

        Query query = getEntityManager().createQuery(
                "select new ForecastFixedCostsEntity(e.fixPreManCost, e.shipCost, e.sellCost, e.diffActPreManCost, " +
                        "e.idleEquipCost, e.rdCost, e.adminCostBu, e.adminCostOd, e.adminCostCompany, e.otherOpCostBu," +
                        " e.otherOpCostOd, e.otherOpCostCompany, e.specItems, e.provisions, e.currencyGains," +
                        " e.valAdjustInventories, e.otherFixCost, e.depreciation, e.capCost, e.equityIncome," +
                        " e.topdownAdjustFixCosts, e.currency) " +
                        "from ForecastFixedCostsEntity e " +
                        "where e.primaryKey.sbu = '" + sbu + "' " +
                        "and e.primaryKey.subregion = '" + subregion + "' " +
                        "and e.primaryKey.period = " + period + " " +
                        "and e.primaryKey.planPeriod = " + planPeriod + " " +
                        "and e.primaryKey.entryType = '" + entryType + "'", ForecastFixedCostsEntity.class);

        return (ForecastFixedCostsEntity) query.getSingleResult();
    }

    public List<ForecastFixedCostsEntity> getDistinctSbuAndSubregions() {
        Query query = getEntityManager().createQuery(
                "select distinct new ForecastFixedCostsEntity(e.primaryKey.sbu, e.primaryKey.subregion) " +
                        "from ForecastFixedCostsEntity e", ForecastFixedCostsEntity.class);

        return (List<ForecastFixedCostsEntity>) query.getSingleResult();
    }
}
