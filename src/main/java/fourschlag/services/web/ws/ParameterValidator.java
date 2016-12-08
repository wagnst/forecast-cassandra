package fourschlag.services.web.ws;

import fourschlag.entities.types.Currency;
import fourschlag.entities.types.EntryType;
import fourschlag.entities.types.Period;
import fourschlag.entities.types.SalesType;

/**
 * ParameterValidator tries to validate different HTTP arguments
 * and depending on success returns boolean.
 * It can be called from a static context.
 */
public class ParameterValidator {
    public static boolean validatePeriod(int period) {
        try {
            Period currentPeriod = new Period(period);
        } catch (IllegalArgumentException ex) {
            return false;
        }
        return true;
    }

    public static boolean validatePlanYear(int year) {
        try {
            Period currentPlanYear = Period.getPeriodByYear(year);
        } catch (IllegalArgumentException ex) {
            return false;
        }
        return true;
    }

    public static boolean validateSalesType(String salesType) {
        if (SalesType.getSalesTypeByString(salesType).equals(null)) {
            return false;
        }
        return true;
    }

    public static boolean validateEntryType(String entryType) {
        if (EntryType.getEntryTypeByString(entryType) == null) {
            return false;
        }
        return true;
    }

    public static boolean validateCurrency(String currency) {
        Currency curr = Currency.getCurrencyByAbbreviation(currency);

        if (curr == null) {
            return false;
        }
        return true;
    }
}
