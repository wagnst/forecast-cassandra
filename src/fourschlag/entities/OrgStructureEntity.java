package fourschlag.entities;

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
}
