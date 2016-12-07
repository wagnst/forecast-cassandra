package fourschlag.entities.jpalAccessors;

import fourschlag.entities.jpaTables.ExchangeRateEntity;

import javax.persistence.Query;

public class ExchangeRateAccessor extends Accessor{
    public ExchangeRateEntity getSpecificExchangeRate(
            int period,
            String fromCurrency,
            String toCurrency) {

        Query query = getEntityManager().createQuery(
                "select e.rate from ExchangeRateEntity e" +
                        "where e.period = " + period + " " +
                        "and e.primaryKey.fromCurrency = '" + fromCurrency + "' " +
                        "and e.primaryKey.toCurrency = '" + toCurrency + "'");

        return (ExchangeRateEntity) query.getSingleResult();
    }
}
