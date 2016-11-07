package fourschlag.entities;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * Created by Henrik on 07.11.2016.
 */

@Table(keyspace = "original_version", name = "org_structure")
public class Org_StructureEntity {
    @PartitionKey
    private String product_main_group;
    private String sbu;
    private String bu;

    public Org_StructureEntity(){}

    public Org_StructureEntity(String product_main_group, String sbu, String bu) {
        this.product_main_group = product_main_group;
        this.sbu = sbu;
        this.bu = bu;
    }

    public String getProduct_main_group() {
        return product_main_group;
    }

    public String getSbu() {
        return sbu;
    }

    public String getBu() {
        return bu;
    }
}
