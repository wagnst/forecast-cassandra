package fourschlag.entities.jpaAccessors;

import fourschlag.entities.jpaTables.OrgStructureEntity;

import javax.persistence.Query;
import java.util.List;

public class OrgStructureAccessor extends Accessor {
    public List<OrgStructureEntity> getProductsAndSbus() {
        Query query = getEntityManagerFactory().createEntityManager().createQuery(
                "select e from OrgStructureEntity e", OrgStructureEntity.class);

        return query.getResultList();
    }
}
