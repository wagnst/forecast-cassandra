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
     * @param period       The period the exchange rate is supposed to be taken
     *                     from
     * @param fromCurrency The currency to be converted from
     *
     * @return a Map with the needed exchange rates
     */
    public double getExchangeRate(Period period, Currency fromCurrency) {
        Double exchangeRate;

        /* check if the exchangeRates map already has the desired exchange rate */
        Map<Currency, Double> map = exchangeRates.get(period.getPeriod());
        /* IF the map has data for the desired period */
        if (map != null) {
            /* THEN check for the desired currency */
            exchangeRate = map.get(fromCurrency);
        } else {
            /* ELSE create a new Map */
            map = new HashMap<>();
            /* the exchange rate is null for now */
            exchangeRate = null;
        }

        /* IF the exchange rate is null */
        if (exchangeRate == null) {
            /* THEN query the database for the needed rate */
            ExchangeRateEntity queryResult = accessor.getSpecificExchangeRate(period.getPeriod(),
                    fromCurrency.getAbbreviation(), toCurrency.getAbbreviation());

            /* IF the result is empty THEN query again with zero month period */
            if (queryResult == null) {
                queryResult = accessor.getSpecificExchangeRate(period.getZeroMonthPeriod(), fromCurrency.getAbbreviation(),
                        toCurrency.getAbbreviation());
            }
            /* IF result still empty THEN set exchange rate to 1.0 */
            if (queryResult == null) {
                exchangeRate = 1.0;
            } else {
                /* ELSE use the rate from the query result */
                exchangeRate = queryResult.getRate();
            }
            /* put the rate at its right place into the map */
            map.put(fromCurrency, exchangeRate);
            /* put the inner map into the outer map */
            exchangeRates.put(period.getPeriod(), map);
        }

        /* finally return the exchange rate */
        return exchangeRate;
    }

    public Map<Integer, Map<Currency, Double>> getExchangeRates() {
        return exchangeRates;
    }
}
