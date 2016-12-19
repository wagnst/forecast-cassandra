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
    /**
     * Queries the table for all rows
     *
     * @return Iterable of entities mapped as RegionEntity
     */
    @Query("SELECT subregion, region FROM regions;")
    Result<RegionEntity> getAll();

    /**
     * Queries the table for all rows with a specific region
     *
     * @param region primary key field region for where clause
     * @return Iterable of entities mapped as RegionEntity
     */
    @Query("SELECT * FROM regions WHERE region = :region ALLOW FILTERING")
    Result<RegionEntity> getEntitiesByRegion(
            @Param("region") String region);

    /**
     * Queries the table for all rows with a specific subregion
     *
     * @param subregion primary key field subregion for where clause
     * @return Iterable of entities mapped as RegionEntity
     */
    @Query("SELECT * FROM regions WHERE subregion = :subregion ALLOW FILTERING")
    Result<RegionEntity> getEntitiesBySubregion(
            @Param("subregion") String subregion);

    @Query("SELECT * FROM regions WHERE subregion = :subregion AND region = :region ALLOW FILTERING;")
    RegionEntity getSpecificEntity(
            @Param("subregion") String subregion,
            @Param("region") String region);

}
