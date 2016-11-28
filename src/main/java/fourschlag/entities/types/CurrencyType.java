package fourschlag.entities.types;

import java.util.Currency;

public enum CurrencyType {
    EURO("eur","€"),
    DOLLAR("usd","$");

    private final String Abbreviations;
    private final String symbol;

    CurrencyType(String Abbreviations, String symbol){
        this.Abbreviations = Abbreviations;
        this.symbol = symbol;
    }

    public String getAbbreviations() {
        return Abbreviations;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return getAbbreviations();
    }
}
