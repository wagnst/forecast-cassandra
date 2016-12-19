package fourschlag.entities.types;

/**
 * Enum that with all known currencies
 */
public enum Currency {
    EURO("eur", '€'),
    DOLLAR("usd", '$');

    private final String abbreviation;
    private final char symbol;

    Currency(String abbreviation, char symbol) {
        this.abbreviation = abbreviation;
        this.symbol = symbol;
    }

    /**
     * This method searches the enum for a specific currency by a given
     * abbreviation
     *
     * @param abbreviation Actual Abbreviation
     *
     * @return Currency or null if nothing was found
     */
    public static Currency getCurrencyByAbbreviation(String abbreviation) {
        /* FOR EACH currency */
        for (Currency currency : Currency.values()) {
            /* IF the abbreviation of the current currency equals the desired abbreviation THEN return the current currency */
            if (currency.getAbbreviation().equals(abbreviation)) {
                return currency;
            }
        }
        /* If no currency was found return null */
        return null;
    }

    /**
     * Getter for the Abbreviation
     *
     * @return Abbreviation that is currently used
     */
    public String getAbbreviation() {
        return abbreviation;
    }

    /**
     * Getter for the Symbol
     *
     * @return Symbol of the currently used Currensy
     */
    public char getSymbol() {
        return symbol;
    }

    /**
     * Converts the attribute symbol from char to String
     *
     * @return Symbol of the current currency as String
     */
    public String getSymbolAsString() {
        return symbol + "";
    }

    /**
     * toString-method that prints out the abbreviation
     *
     * @return Abbreviation that is currently used
     */
    @Override
    public String toString() {
        return getAbbreviation();
    }
}