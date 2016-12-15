package fourschlag.entities.jpaAccessors;

import fourschlag.entities.jpaTables.ExchangeRateEntity;
import fourschlag.services.db.JpaConnection;

import javax.persistence.NoResultException;
import javax.persistence.Query;

public class ExchangeRateAccessor extends Accessor{

    public ExchangeRateAccessor(JpaConnection connection) {
        super(connection);
    }

    public ExchangeRateEntity getSpecificExchangeRate(
            int period,
            String fromCurrency,
            String toCurrency) {

        Query query = getEntityManagerFactory().createEntityManager().createQuery(
                "select e from ExchangeRateEntity e " +
                        "where e.primaryKey.period = :period " +
                        "and e.primaryKey.fromCurrency = :fromCurrency " +
                        "and e.primaryKey.toCurrency = :toCurrency", ExchangeRateEntity.class);

        query.setParameter("period", period);
        query.setParameter("fromCurrency", fromCurrency);
        query.setParameter("toCurrency", toCurrency);

        try {
            return (ExchangeRateEntity) query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
}
