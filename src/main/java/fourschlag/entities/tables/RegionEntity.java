package fourschlag.entities.tables;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * Provides the data from the Region table
 */

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

    /**
     * Getter for the Subregion
     *
     * @return Subregion that is currently used
     */
    public String getSubregion() {
        return subregion;
    }

    /**
     * Getter for the Region
     *
     * @return Region that is currently used
     */
    public String getRegion() {
        return region;
    }
}
