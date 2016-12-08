package fourschlag.entities.accessors;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;
import fourschlag.entities.tables.RegionEntity;

/**
 * Provides functionality to query the database for the Regions
 */

@Accessor
public interface RegionAccessor {
    /*CQL_Query to get the region data */
    @Query("SELECT subregion, region FROM regions;")
    Result<RegionEntity> getSubregions();
}
