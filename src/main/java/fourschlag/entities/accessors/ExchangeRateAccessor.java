package fourschlag.entities.accessors;

import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import fourschlag.entities.tables.ExchangeRateEntity;

/**
 * Provides functionality to query the database for the ExchangeRate
 */

@Accessor
public interface ExchangeRateAccessor {
    /**
     * Queries the table for a specific exchange rate. Selects only the field
     * 'rate'.
     *
     * @param period       primary key field period for where clause
     * @param fromCurrency primary key field from_currency for where clause
     * @param toCurrency   primary key field to_currency for where clause
     *
     * @return One database row mapped as ExchangeRateEntity
     */
    @Query("SELECT rate FROM exchange_rate WHERE period = :period AND from_currency = :from_currency AND to_currency = :to_currency ALLOW FILTERING")
    ExchangeRateEntity getSpecificExchangeRate(@Param("period") int period,
                                               @Param("from_currency") String fromCurrency,
                                               @Param("to_currency") String toCurrency);
}
