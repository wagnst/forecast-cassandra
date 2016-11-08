package fourschlag.entities.accessors;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;
import fourschlag.entities.Org_StructureEntity;
import fourschlag.entities.RegionEntity;


/**
 * Created by Henrik on 07.11.2016.
 */
@Accessor
public interface RegionAccessor {
    @Query("SELECT subregion, region FROM regions;")
    Result<RegionEntity> getSubregions();
}
