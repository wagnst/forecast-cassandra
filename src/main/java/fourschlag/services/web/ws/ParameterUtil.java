package fourschlag.services.web.ws;

import fourschlag.entities.types.*;

/**
 * ParameterUtil tries to validate different HTTP arguments
 * and depending on success returns boolean.
 * It can be called from a static context.
 */
public class ParameterUtil {
    static boolean validatePeriod(int period) {
        try {
            /* TODO: method need to be fixed */
            Period currentPeriod = new Period(period);
        } catch (IllegalArgumentException ex) {
            return false;
        }
        return true;
    }


    static boolean validatePlanYear(int year) {
        try {
            /* TODO: method need to be fixed */
            Period currentPlanYear = Period.getPeriodByYear(year);
        } catch (IllegalArgumentException ex) {
            return false;
        }
        return true;
    }

    static boolean validateSalesType(String salesType) {
        if (SalesType.getSalesTypeByString(salesType) == null) {
            return false;
        }
        return true;
    }

    static boolean validateEntryType(String entryType) {
        if (EntryType.getEntryTypeByString(entryType) == null) {
            return false;
        }
        return true;
    }

    static boolean validateCurrency(String currency) {
        Currency curr = Currency.getCurrencyByAbbreviation(currency);

        if (curr == null) {
            return false;
        }
        return true;
    }

    /* TODO: method need to be fixed */
    public static Period calculateToPeriod(int planYear) {
        return Period.getPeriodByYear(planYear).incrementMultipleTimes(OutputDataType.getNumberOfMonths());
    }

    static Period calculateToPeriod(Period fromPeriod) {
        return new Period(fromPeriod).incrementMultipleTimes(OutputDataType.getNumberOfMonths());
    }
}
