package fourschlag.entities.mysqlTables.keys;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class OrgStructureKey implements Serializable{
    @Column(name = "product_main_group")
    private String productMainGroup;

    @Column(name = "sbu")
    private String sbu;

    public String getProductMainGroup() {
        return productMainGroup;
    }

    public String getSbu() {
        return sbu;
    }
}
