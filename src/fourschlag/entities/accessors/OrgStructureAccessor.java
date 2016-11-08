package fourschlag.entities.accessors;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;
import fourschlag.entities.OrgStructureEntity;

/**
 * Created by Henrik on 07.11.2016.
 */
@Accessor
public interface OrgStructureAccessor {
    @Query("SELECT product_main_group, sbu FROM org_structure;")
    Result<OrgStructureEntity> getProducts();
}
