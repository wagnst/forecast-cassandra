package fourschlag.entities.jpaAccessors;

import fourschlag.entities.jpaTables.RegionEntity;

import javax.persistence.Query;
import java.util.List;

public class RegionAccessor extends Accessor {
    public List<RegionEntity> getSubregions() {
        Query query = getEntityManager().createQuery(
                "select e from RegionEntity e", RegionEntity.class);

        return query.getResultList();
    }
}
