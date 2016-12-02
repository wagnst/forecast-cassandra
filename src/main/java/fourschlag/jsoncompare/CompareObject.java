package fourschlag.jsoncompare;

public class CompareObject {
    protected String sbu;
    protected String productMainGroup;
    protected String region;
    protected String subregion;
    protected String salesType;
    protected String entryType;


    public CompareObject(String sbu, String productMainGroup, String region, String subregion, String salesType, String entryType) {
        this.sbu = sbu;
        this.productMainGroup = productMainGroup;
        this.region = region;
        this.subregion = subregion;
        this.salesType = salesType;
        this.entryType = entryType;
    }

    public String getSbu() {
        return sbu;
    }

    public String getProductMainGroup() {
        return productMainGroup;
    }

    public String getRegion() {
        return region;
    }

    public String getSubregion() {
        return subregion;
    }

    public String getSalesType() {
        return salesType;
    }

    public String getEntryType() {
        return entryType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompareObject that = (CompareObject) o;

        if (sbu != null ? !sbu.equals(that.sbu) : that.sbu != null) return false;
        if (productMainGroup != null ? !productMainGroup.equals(that.productMainGroup) : that.productMainGroup != null)
            return false;
        if (region != null ? !region.equals(that.region) : that.region != null) return false;
        if (subregion != null ? !subregion.equals(that.subregion) : that.subregion != null) return false;
        if (salesType != null ? !salesType.equals(that.salesType) : that.salesType != null) return false;
        return entryType != null ? entryType.equals(that.entryType) : that.entryType == null;
    }

    @Override
    public int hashCode() {
        int result = sbu != null ? sbu.hashCode() : 0;
        result = 31 * result + (productMainGroup != null ? productMainGroup.hashCode() : 0);
        result = 31 * result + (region != null ? region.hashCode() : 0);
        result = 31 * result + (subregion != null ? subregion.hashCode() : 0);
        result = 31 * result + (salesType != null ? salesType.hashCode() : 0);
        result = 31 * result + (entryType != null ? entryType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return  "sbu='" + sbu + '\'' +
                ", productMainGroup='" + productMainGroup + '\'' +
                ", region='" + region + '\'' +
                ", subregion='" + subregion + '\'' +
                ", salesType='" + salesType + '\'' +
                ", entryType='" + entryType;
    }
}
