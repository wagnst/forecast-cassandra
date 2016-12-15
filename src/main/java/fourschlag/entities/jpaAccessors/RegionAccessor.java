package fourschlag.entities.jpaAccessors;

import fourschlag.entities.jpaTables.RegionEntity;
import fourschlag.services.db.JpaConnection;

import javax.persistence.Query;
import java.util.List;

public class RegionAccessor extends Accessor {

    public RegionAccessor(JpaConnection connection) {
        super(connection);
    }

    public List<RegionEntity> getSubregions() {
        Query query = getEntityManagerFactory().createEntityManager().createQuery(
                "select e from RegionEntity e", RegionEntity.class);

        return query.getResultList();
    }
}
