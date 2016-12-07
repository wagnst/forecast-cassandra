package fourschlag.entities.jpaTables;

import fourschlag.entities.jpaTables.keys.RegionKey;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "regions")
public class RegionEntity {
    @EmbeddedId
    private RegionKey primaryKey;

    public RegionKey getPrimaryKey() {
        return primaryKey;
    }
}
