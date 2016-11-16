package fourschlag.services.data.requests;

import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import fourschlag.entities.accessors.RegionAccessor;
import fourschlag.entities.tables.RegionEntity;
import fourschlag.services.db.CassandraConnection;

import java.util.HashSet;
import java.util.Set;

/**
 * Extends Request. Offers functionality to request regions and subregions from the database.
 */
public class RegionRequest extends Request {

    private RegionAccessor regionAccessor;

    /**
     * Constructor for RegionRequest
     *
     * @param connection Cassandra connection that is supposed to be used
     */
    public RegionRequest(CassandraConnection connection) {
        super(connection);
        MappingManager manager = new MappingManager(getSession());
        regionAccessor = manager.createAccessor(RegionAccessor.class);
    }

    /**
     * Queries the database for all subregions.
     *
     * @return Result Iterable with multiple Region entities
     */
    public Result<RegionEntity> getSubregions() {
        return regionAccessor.getSubregions();
    }

    /**
     * Queries the database for all regions
     *
     * @return Set with all regions as String
     */
    public Set<String> getRegions() {
        /* query regions, filter duplicates */
        Set<String> regions = new HashSet<>();
        Result<RegionEntity> regionEntities = getSubregions();
        for (RegionEntity region : regionEntities) {
            regions.add(region.getRegion());
        }
        return regions;
    }
}
