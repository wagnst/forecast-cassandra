package fourschlag.services.data.requests;

import com.datastax.driver.mapping.Result;
import fourschlag.entities.accessors.ExchangeRateAccessor;
import fourschlag.entities.tables.ExchangeRateEntity;
import fourschlag.entities.types.Period;
import fourschlag.services.data.Service;
import fourschlag.services.db.CassandraConnection;

import java.util.HashMap;
import java.util.Map;

public class ExchangeRateRequest extends Request {
    private String toCurrency;
    private Period planPeriod;
    private Map<Period, Map<String, Double>> exchangeRates;

    private ExchangeRateAccessor accessor;

    public ExchangeRateRequest(CassandraConnection connection, String toCurrency, int planYear) {
        super(connection);
        this.toCurrency = toCurrency;
        this.planPeriod = Period.getPeriodByYear(planYear);
        this.exchangeRates = new HashMap<>();
        accessor = getManager().createAccessor(ExchangeRateAccessor.class);
        fillExchangeRateMap();
    }

    private void fillExchangeRateMap() {
        Result<ExchangeRateEntity> exchangeRateResult;

        for (int i = 0; i < Service.getNumberOfMonths(); i++) {
            exchangeRateResult = accessor.getExchangeRate(planPeriod.getPeriod(), toCurrency);
            exchangeRates.put(planPeriod, new HashMap<>());

            for (ExchangeRateEntity entity : exchangeRateResult) {
                exchangeRates.get(planPeriod).put(entity.getFromCurrency(), entity.getRate());
            }

            planPeriod.increment();
        }

    }

    public String getToCurrency() {
        return toCurrency;
    }

    public double getExchangeRate(Period period, String fromCurrency) {
        return exchangeRates.get(period).get(fromCurrency);
    }

    public Map<Period, Map<String, Double>> getExchangeRates() {
        return exchangeRates;
    }
}
