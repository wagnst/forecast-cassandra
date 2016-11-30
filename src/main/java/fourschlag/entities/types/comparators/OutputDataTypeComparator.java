package fourschlag.entities.types.comparators;

import fourschlag.entities.types.OutputDataType;

import java.util.Comparator;

/**
 * Provides functionality to sort the final ResultList
 */

public class OutputDataTypeComparator implements Comparator<OutputDataType> {
    /* IF return value is negative THEN o1 comes first, IF return value is positive o2 comes first */
    @Override
    public int compare(OutputDataType o1, OutputDataType o2) {
        int returnValue;
        returnValue = o1.getEntryType().compareTo(o2.getEntryType());
        if (returnValue == 0) {
            returnValue = o1.getRegion().compareTo(o2.getRegion());

            if (returnValue == 0) {
                returnValue = o1.getSubregion().compareTo(o2.getSubregion());

                if (returnValue == 0) {
                    returnValue = o1.getSbu().compareTo(o2.getSbu());

                    if(returnValue == 0){
                        returnValue = o2.getFcType().compareTo(o1.getFcType());

                        if(returnValue == 0){
                            returnValue = o1.getProductMainGroup().compareTo(o2.getProductMainGroup());

                            if(returnValue == 0){
                                returnValue = o1.getSalesType().compareTo(o2.getSalesType());

                                if(returnValue == 0){
                                    return o1.getOrderNumber() - o2.getOrderNumber();
                                }
                            }
                        }
                    }
                }
            }
        }
        return returnValue;
    }
}
