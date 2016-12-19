package fourschlag.entities.jpaAccessors;

import fourschlag.entities.jpaTables.RegionEntity;
import fourschlag.services.db.JpaConnection;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class RegionAccessor extends Accessor {

    public RegionAccessor(JpaConnection connection) {
        super(connection);
    }

    public List<RegionEntity> getSubregions() {
        EntityManager manager = createEntityManager();

        Query query = manager.createQuery(
                "select e from RegionEntity e", RegionEntity.class);

        try {
            return query.getResultList();
        } finally {
            manager.close();
        }

    }
}
