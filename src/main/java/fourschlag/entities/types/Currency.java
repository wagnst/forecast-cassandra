package fourschlag.entities.types;

public enum Currency {
    EURO("eur",'€'),
    DOLLAR("usd",'$');

    private final String abbreviation;
    private final char symbol;

    Currency(String abbreviation, char symbol){
        this.abbreviation = abbreviation;
        this.symbol = symbol;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public char getSymbol() {
        return symbol;
    }

    public String getSymbolAsString() { return symbol + "";}

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

    public static Currency getCurrencyBySymbol(char symbol) {
        for (Currency currency: Currency.values()) {
            if (currency.getSymbol() == symbol) {
                return currency;
            }
        }
        return null;
    }
}
