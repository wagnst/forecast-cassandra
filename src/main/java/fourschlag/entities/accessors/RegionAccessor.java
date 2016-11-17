package fourschlag.entities.accessors;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;
import fourschlag.entities.tables.RegionEntity;

@Accessor
public interface RegionAccessor {
    @Query("SELECT subregion, region FROM regions;")
    Result<RegionEntity> getSubregions();
}
