package fourschlag.entities.types;

/**
 * Enum that provides the common currencies
 */


public enum Currency {
    EURO("eur",'€'),
    DOLLAR("usd",'$');

    private final String abbreviation;
    private final char symbol;

    /**
     * Constructor for Currency
     *
     * @param abbreviation
     * @param symbol
     */
    Currency(String abbreviation, char symbol){
        this.abbreviation = abbreviation;
        this.symbol = symbol;
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
     * Getter for the SymbolAsString
     *
     * @return SymbolAsString that is currently used
     */
    public String getSymbolAsString() { return symbol + "";}

    /**
     * toString-method to print out the Abbreviation
     *
     * @return Abbreviation that is currently used
     */
    @Override
    public String toString() {
        return getAbbreviation();
    }

    /**
     * method to get the Currency by the current Abbreviation
     *
     * @param abbreviation Actual Abbreviation
     *
     * @return Currency object
     */
    public static Currency getCurrencyByAbbreviation(String abbreviation) {
        for (Currency currency : Currency.values()) {
            if (currency.getAbbreviation().equals(abbreviation)) {
                return currency;
            }
        }
        return null;
    }

    /**
     * method ..
     *
     * @param symbol Currencysymbol (€ or $)
     *
     * @return Currency object
     */
    public static Currency getCurrencyBySymbol(char symbol) {
        for (Currency currency: Currency.values()) {
            if (currency.getSymbol() == symbol) {
                return currency;
            }
        }
        return null;
    }
}
