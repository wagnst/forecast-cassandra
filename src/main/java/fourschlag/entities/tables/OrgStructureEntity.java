package fourschlag.entities.tables;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * Provides the data from the OrgStructure table
 */

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

    /**
     * Getter for the ProductMainGroup
     *
     * @return ProductMainGroup that is currently used
     */
    public String getProductMainGroup() {
        return productMainGroup;
    }

    /**
     * Getter for the SBU
     *
     * @return SBU that is currently used
     */
    public String getSbu() {
        return sbu;
    }

    /**
     * Getter for the Bu
     *
     * @return Bu that is currenty used
     */
    public String getBu() {
        return bu;
    }

    /**
     * equals-method to compare OrgStructureEntities
     *
     * @param o generic Object to compare with
     * @return true, if input object equals currently used object
     * false, if  input object does not equal currently used object
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrgStructureEntity that = (OrgStructureEntity) o;

        if (productMainGroup != null ? !productMainGroup.equals(that.productMainGroup) : that.productMainGroup != null)
            return false;
        if (sbu != null ? !sbu.equals(that.sbu) : that.sbu != null)
            return false;
        return bu != null ? bu.equals(that.bu) : that.bu == null;

    }

    /**
     * method to generate hashcode out of the ProductMainGroup
     *
     * @return integer value of the hashcode
     */
    @Override
    public int hashCode() {
        int result = productMainGroup != null ? productMainGroup.hashCode() : 0;
        result = 31 * result + (sbu != null ? sbu.hashCode() : 0);
        result = 31 * result + (bu != null ? bu.hashCode() : 0);
        return result;
    }
}
