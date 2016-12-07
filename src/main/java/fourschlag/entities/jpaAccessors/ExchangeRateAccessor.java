package fourschlag.entities.jpaAccessors;

import fourschlag.entities.jpaTables.ExchangeRateEntity;

import javax.persistence.Query;

public class ExchangeRateAccessor extends Accessor{
    public ExchangeRateEntity getSpecificExchangeRate(
            int period,
            String fromCurrency,
            String toCurrency) {

        Query query = getEntityManager().createQuery(
                "select e from ExchangeRateEntity e" +
                        "where e.period = " + period + " " +
                        "and e.primaryKey.fromCurrency = '" + fromCurrency + "' " +
                        "and e.primaryKey.toCurrency = '" + toCurrency + "'", ExchangeRateEntity.class);

        return (ExchangeRateEntity) query.getSingleResult();
    }
}