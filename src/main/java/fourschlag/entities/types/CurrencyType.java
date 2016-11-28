package fourschlag.entities.types;

import java.util.Currency;

public enum CurrencyType {
    EURO("EUR","€"),
    DOLLAR("USD","$");

    private final String kuerzel;
    private final String symbol;

    CurrencyType(String kuerzel, String symbol){
        this.kuerzel = kuerzel;
        this.symbol = symbol;
    }

    public String getKuerzel() {
        return kuerzel;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return getKuerzel();
    }
}
