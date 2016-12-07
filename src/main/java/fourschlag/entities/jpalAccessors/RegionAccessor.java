package fourschlag.entities.jpalAccessors;

import fourschlag.entities.jpaTables.RegionEntity;

import javax.persistence.Query;
import java.util.List;

public class RegionAccessor extends Accessor {
    public List<RegionEntity> getSubregions() {
        Query query = getEntityManager().createQuery(
                "select e.primaryKey.subregion, e.primaryKey.region from RegionEntity e");

        return (List<RegionEntity>) query.getResultList();
    }
}
