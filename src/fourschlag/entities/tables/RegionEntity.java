package fourschlag.entities.tables;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(name = "regions")
public class RegionEntity {

    @PartitionKey
    private String subregion;
    private String region;

    public RegionEntity() {
    }

    public RegionEntity(String subregion, String region) {
        this.subregion = subregion;
        this.region = region;
    }

    public String getSubregion() {
        return subregion;
    }

    public String getRegion() {
        return region;
    }
}
