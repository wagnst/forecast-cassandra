package fourschlag.entities.mysqlAccessors;

import fourschlag.entities.mysqlTables.OrgStructureEntity;

import javax.persistence.Query;
import java.util.List;

public class OrgStructureAccessor extends Accessor {
    public List<OrgStructureEntity> getProductsAndSbus() {
        Query query = getEntityManager().createQuery(
                "select e.primaryKey.productMainGroup, e.primaryKey.sbu from OrgStructureEntity e");

        return (List<OrgStructureEntity>) query.getResultList();
    }
}
