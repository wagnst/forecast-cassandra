package fourschlag.entities.jpaAccessors;

import fourschlag.entities.jpaTables.OrgStructureEntity;

import javax.persistence.Query;
import java.util.List;

public class OrgStructureAccessor extends Accessor {
    public List<OrgStructureEntity> getProductsAndSbus() {
        Query query = getEntityManager().createQuery(
                "select e from OrgStructureEntity e", OrgStructureEntity.class);

        return (List<OrgStructureEntity>) query.getResultList();
    }
}
