package fourschlag.services.data.requests;

import com.datastax.driver.mapping.Result;
import fourschlag.entities.RegionEntity;
import fourschlag.entities.accessors.RegionAccessor;

import java.util.HashSet;
import java.util.Set;

public class RegionRequest extends Request{

    private RegionAccessor regionAccessor;
    private static RegionRequest instance = null;

    private RegionRequest() {
        super();
        regionAccessor = getManager().createAccessor(RegionAccessor.class);
    }

    private static RegionRequest getInstance() {
        if (instance == null) {
            instance = new RegionRequest();
        }
        return instance;
    }

    public static Result<RegionEntity> getSubregions() {
        return getInstance().regionAccessor.getSubregions();
    }

    public static Set<String> getRegions() {
        /* query regions, filter duplicates */
        Set<String> regions = new HashSet<>();
        for (RegionEntity region : getSubregions()) {
            regions.add(region.getRegion());
        }
        return regions;
    }
}
