package fourschlag.jsoncompare;

import java.util.Comparator;

/**
 * Created by thor on 29.11.2016.
 */
public class DiffComparator implements Comparator<CompareObject> {
    @Override
    public int compare(CompareObject o1, CompareObject o2) {
        int returnValue;
        returnValue = o1.getSbu().compareTo(o2.getSbu());
        if (returnValue == 0) {
            returnValue = o1.getProductMainGroup().compareTo(o2.getProductMainGroup());

            if (returnValue == 0) {
                returnValue = o1.getRegion().compareTo(o2.getRegion());

                if (returnValue == 0) {
                    returnValue = o1.getSubregion().compareTo(o2.getSubregion());

                    if (returnValue == 0) {
                        returnValue = o1.getSalesType().compareTo(o2.getSalesType());

                        if (returnValue == 0) {
                            returnValue = o1.getEntryType().compareTo(o2.getEntryType());
                        }
                    }
                }
            }
        }
        return returnValue;
    }
}
