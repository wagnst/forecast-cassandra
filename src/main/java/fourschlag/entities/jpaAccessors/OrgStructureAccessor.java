package fourschlag.entities.jpaAccessors;

import fourschlag.entities.jpaTables.OrgStructureEntity;
import fourschlag.services.db.JpaConnection;

import javax.persistence.Query;
import java.util.List;

public class OrgStructureAccessor extends Accessor {

    public OrgStructureAccessor(JpaConnection connection) {
        super(connection);
    }

    public List<OrgStructureEntity> getProductsAndSbus() {
        Query query = getEntityManagerFactory().createEntityManager().createQuery(
                "select e from OrgStructureEntity e", OrgStructureEntity.class);

        return query.getResultList();
    }
}
