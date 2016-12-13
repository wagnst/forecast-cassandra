package fourschlag.services.web.ws;

import fourschlag.entities.types.*;

/**
 * ParameterUtil tries to validate different HTTP arguments
 * and depending on success returns boolean.
 * It can be called from a static context.
 */
public class ParameterUtil {
    public static boolean validatePeriod(int period) {
        try {
            new Period(period);
        } catch (IllegalArgumentException ex) {
            return false;
        }
        return true;
    }

    public static boolean validatePlanYear(int year) {
        try {
            Period.getPeriodByYear(year);
        } catch (IllegalArgumentException ex) {
            return false;
        }
        return true;
    }

    public static boolean validateSalesType(String salesType) {
        if (SalesType.getSalesTypeByString(salesType) == null) {
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

    public static Period calculateToPeriod(int planYear) {
        return Period.getPeriodByYear(planYear).incrementMultipleTimes(OutputDataType.getNumberOfMonths());
    }

    public static Period calculateToPeriod(Period fromPeriod) {
        return new Period(fromPeriod).incrementMultipleTimes(OutputDataType.getNumberOfMonths());
    }
}
