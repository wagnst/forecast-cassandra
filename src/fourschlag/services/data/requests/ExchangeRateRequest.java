package fourschlag.services.data.requests;

import fourschlag.entities.accessors.ExchangeRateAccessor;
import fourschlag.entities.tables.ExchangeRateEntity;
import fourschlag.entities.types.Period;
import fourschlag.services.db.CassandraConnection;

import java.util.HashMap;
import java.util.Map;

/**
 * Extends Request. Offers functionality to request exchange rates.
 */
public class ExchangeRateRequest extends Request {
    private String toCurrency;
    private Map<Integer, Map<String, Double>> exchangeRates;

    private ExchangeRateAccessor accessor;

    /**
     * Constructor for ExchangeRateRequest
     *
     * @param connection Cassandra connection that is supposed to be used
     * @param toCurrency the desired currency
     */
    public ExchangeRateRequest(CassandraConnection connection, String toCurrency) {
        super(connection);
        this.toCurrency = toCurrency;
        this.exchangeRates = new HashMap<>();
        accessor = getManager().createAccessor(ExchangeRateAccessor.class);
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public double getExchangeRate(Period period, String fromCurrency) {
        Double exchangeRate;
        Map<String, Double> map = exchangeRates.get(period.getPeriod());
        if (map != null) {
            exchangeRate = map.get(fromCurrency);
        } else {
            exchangeRate = null;
        }

        if (exchangeRate == null) {
            ExchangeRateEntity queryResult = accessor.getSpecificExchangeRate(period.getPeriod(), fromCurrency, toCurrency);

            if (queryResult == null) {
                queryResult = accessor.getSpecificExchangeRate(period.getZeroMonthPeriod(), fromCurrency, toCurrency);
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

    public Map<Integer, Map<String, Double>> getExchangeRates() {
        return exchangeRates;
    }
}
