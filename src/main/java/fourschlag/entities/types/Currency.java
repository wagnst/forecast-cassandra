package fourschlag.entities.types;

public enum Currency {
    EURO("eur","€"),
    DOLLAR("usd","$");

    private final String abbreviation;
    private final String symbol;

    Currency(String abbreviation, String symbol){
        this.abbreviation = abbreviation;
        this.symbol = symbol;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return getAbbreviation();
    }

    public static Currency getCurrencyByAbbreviation(String abbreviation) {
        for (Currency currency : Currency.values()) {
            if (currency.getAbbreviation().equals(abbreviation)) {
                return currency;
            }
        }
        return null;
    }

    public static Currency getCurrencyBySymbol(String symbol) {
        for (Currency currency: Currency.values()) {
            if (currency.getSymbol().equals(symbol)) {
                return currency;
            }
        }
        return null;
    }
}
