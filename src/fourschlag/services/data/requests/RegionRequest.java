package fourschlag.services.data.requests;

import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import fourschlag.entities.tables.RegionEntity;
import fourschlag.entities.accessors.RegionAccessor;
import fourschlag.services.db.CassandraConnection;

import java.util.HashSet;
import java.util.Set;

public class RegionRequest extends Request {

    private RegionAccessor regionAccessor;

    public RegionRequest(CassandraConnection connection) {
        super(connection);
        MappingManager manager = new MappingManager(getSession());
        regionAccessor = manager.createAccessor(RegionAccessor.class);
    }

    public Result<RegionEntity> getSubregions() {
        return regionAccessor.getSubregions();
    }

    public Set<String> getRegions() {
        /* query regions, filter duplicates */
        Set<String> regions = new HashSet<>();
        for (RegionEntity region : getSubregions()) {
            regions.add(region.getRegion());
        }
        return regions;
    }
}
