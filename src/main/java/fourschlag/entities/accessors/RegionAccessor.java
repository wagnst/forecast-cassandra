package fourschlag.entities.accessors;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
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

    @Query("SELECT * FROM regions WHERE region = :region ALLOW FILTERING")
    Result<RegionEntity> getEntitiesByRegion(
            @Param("region") String region);

    @Query("SELECT * FROM regions WHERE subregion = :subregion ALLOW FILTERING")
    Result<RegionEntity> getEntitiesBySubregion(
            @Param("subregion") String subregion);
}
