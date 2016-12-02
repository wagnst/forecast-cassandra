package fourschlag.jsoncompare;

/**
 * Created by thor on 02.12.2016.
 */
public class CompareObjectWithKpi extends CompareObject {
    private String kpi;

    public CompareObjectWithKpi(String sbu, String productMainGroup, String region, String subregion, String salesType, String entryType, String kpi) {
        super(sbu, productMainGroup, region, subregion, salesType, entryType);
        this.kpi = kpi;
    }

    public String getKpi() {
        return kpi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CompareObjectWithKpi that = (CompareObjectWithKpi) o;

        if (sbu != null ? !sbu.equals(that.sbu) : that.sbu != null) return false;
        if (productMainGroup != null ? !productMainGroup.equals(that.productMainGroup) : that.productMainGroup != null)
            return false;
        if (region != null ? !region.equals(that.region) : that.region != null) return false;
        if (subregion != null ? !subregion.equals(that.subregion) : that.subregion != null) return false;
        if (salesType != null ? !salesType.equals(that.salesType) : that.salesType != null) return false;
        if (entryType != null ? !entryType.equals(that.entryType) : that.entryType != null) return false;
        return kpi != null ? kpi.equals(that.kpi) : that.kpi == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (kpi != null ? kpi.hashCode() : 0);
        return result;
    }
}
