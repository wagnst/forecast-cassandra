package fourschlag.entities.jpaAccessors;

import fourschlag.entities.jpaTables.OrgStructureEntity;
import fourschlag.services.db.JpaConnection;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class OrgStructureAccessor extends Accessor {

    public OrgStructureAccessor(JpaConnection connection) {
        super(connection);
    }

    public List<OrgStructureEntity> getProductsAndSbus() {
        EntityManager manager = createEntityManager();

        Query query = manager.createQuery(
                "select e from OrgStructureEntity e", OrgStructureEntity.class);

        try {
            return query.getResultList();
        } finally {
            manager.close();
        }
    }
}
