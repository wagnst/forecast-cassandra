package fourschlag.entities.mysqlTables;

import fourschlag.entities.mysqlTables.keys.OrgStructureKey;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "org_structure")
public class OrgStructureEntity {

    @EmbeddedId
    private OrgStructureKey primaryKey;

    @Column(name = "bu")
    private String bu;

    public OrgStructureKey getPrimaryKey() {
        return primaryKey;
    }

    public String getBu() {
        return bu;
    }
}