package fourschlag.entities.tables;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(name = "org_structure")
public class OrgStructureEntity {

    @PartitionKey
    @Column(name = "product_main_group")
    private String productMainGroup;

    @Column(name = "sbu")
    private String sbu;

    @Column(name = "bu")
    private String bu;

    public OrgStructureEntity() {
    }

    public OrgStructureEntity(String productMainGroup, String sbu, String bu) {
        this.productMainGroup = productMainGroup;
        this.sbu = sbu;
        this.bu = bu;
    }

    public String getProductMainGroup() {
        return productMainGroup;
    }

    public String getSbu() {
        return sbu;
    }

    public String getBu() {
        return bu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrgStructureEntity that = (OrgStructureEntity) o;

        if (productMainGroup != null ? !productMainGroup.equals(that.productMainGroup) : that.productMainGroup != null)
            return false;
        if (sbu != null ? !sbu.equals(that.sbu) : that.sbu != null) return false;
        return bu != null ? bu.equals(that.bu) : that.bu == null;

    }

    @Override
    public int hashCode() {
        int result = productMainGroup != null ? productMainGroup.hashCode() : 0;
        result = 31 * result + (sbu != null ? sbu.hashCode() : 0);
        result = 31 * result + (bu != null ? bu.hashCode() : 0);
        return result;
    }
}
