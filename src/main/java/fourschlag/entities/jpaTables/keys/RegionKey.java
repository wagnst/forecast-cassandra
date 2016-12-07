package fourschlag.entities.jpaTables.keys;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class RegionKey implements Serializable {
    @Column(name = "region")
    private String region;

    @Column(name = "subregion")
    private String subregion;

    public String getRegion() {
        return region;
    }

    public String getSubregion() {
        return subregion;
    }
}
