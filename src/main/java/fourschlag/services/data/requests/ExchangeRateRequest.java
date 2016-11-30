package fourschlag.services.data.requests;

import fourschlag.entities.accessors.ExchangeRateAccessor;
import fourschlag.entities.tables.ExchangeRateEntity;
import fourschlag.entities.types.Currency;
import fourschlag.entities.types.Period;
import fourschlag.services.db.CassandraConnection;

import java.util.HashMap;
import java.util.Map;

/**
 * Extends Request. Offers functionality to request the exchange rates.
 */

public class ExchangeRateRequest extends Request {

    private Currency toCurrency;
    private Map<Integer, Map<Currency, Double>> exchangeRates;

    private ExchangeRateAccessor accessor;

    /**
     * Constructor for ExchangeRateRequest
     *
     * @param connection Cassandra connection that is supposed to be used
     * @param toCurrency the desired currency
     */
    public ExchangeRateRequest(CassandraConnection connection, Currency toCurrency) {
        super(connection);
        this.toCurrency = toCurrency;
        this.exchangeRates = new HashMap<>();
        accessor = getManager().createAccessor(ExchangeRateAccessor.class);
    }

    /**
     * Getter for the currency
     *
     * @return the desired currency
     */
    public Currency getToCurrency() {
        return toCurrency;
    }


    /**
     * method to retrieve a specific exchange rate
     *
     * @param period       The period the exchange rate is supposed to be taken from
     * @param fromCurrency The currency to be converted from
     *
     * @return a Map with the needed exchange rates
     */

    public double getExchangeRate(Period period, Currency fromCurrency) {
        Double exchangeRate;
        Map<Currency, Double> map = exchangeRates.get(period.getPeriod());
        if (map != null) {
            exchangeRate = map.get(fromCurrency);
        } else {
            exchangeRate = null;
        }

        if (exchangeRate == null) {
            ExchangeRateEntity queryResult = accessor.getSpecificExchangeRate(period.getPeriod(),
                    fromCurrency.getAbbreviation(), toCurrency.getAbbreviation());

            if (queryResult == null) {
                queryResult = accessor.getSpecificExchangeRate(period.getZeroMonthPeriod(), fromCurrency.getAbbreviation(),
                        toCurrency.getAbbreviation());
            }
            if (queryResult == null) {
                /* TODO: Maybe throw exception if no exchange Rate is available */
                exchangeRate = 1.0;
            } else {
                exchangeRate = queryResult.getRate();
            }
            map = new HashMap<>();
            map.put(fromCurrency, exchangeRate);
            exchangeRates.put(period.getPeriod(), map);
        }

        return exchangeRate;
    }

    public Map<Integer, Map<Currency, Double>> getExchangeRates() {
        return exchangeRates;
    }
}
