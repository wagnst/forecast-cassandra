package fourschlag.entities.types.comparators;

import fourschlag.entities.types.OutputDataType;

import java.util.Comparator;

/**
 * Provides functionality compare to instances of OutputDataType
 */
public class OutputDataTypeComparator implements Comparator<OutputDataType> {
    @Override
    public int compare(OutputDataType o1, OutputDataType o2) {
        int returnValue;

        /* We compare each of the attributes in the order the objects are supposed to be sorted by.
        *  Each comparison gives us an integer value that indicates which of the two objects comes first.
        *  IF this value is 0 THEN compare the next attribute
        *  IF it is NOT null we don't have to compare the other attributes and may return the value of the last comparison */
        returnValue = o1.getEntryType().compareTo(o2.getEntryType());
        if (returnValue == 0) {
            returnValue = o1.getRegion().compareTo(o2.getRegion());

            if (returnValue == 0) {
                returnValue = o1.getSubregion().compareTo(o2.getSubregion());

                if (returnValue == 0) {
                    returnValue = o1.getSbu().compareTo(o2.getSbu());

                    if (returnValue == 0) {
                        returnValue = o2.getFcType().compareTo(o1.getFcType());

                        if (returnValue == 0) {
                            returnValue = o1.getProductMainGroup().compareTo(o2.getProductMainGroup());

                            if (returnValue == 0) {
                                returnValue = o1.getSalesType().compareTo(o2.getSalesType());

                                if (returnValue == 0) {
                                    return o1.getOrderNumber() - o2.getOrderNumber();
                                }
                            }
                        }
                    }
                }
            }
        }
        /* IF return value is negative THEN o1 comes first, IF return value is positive o2 comes first */
        return returnValue;
    }
}
