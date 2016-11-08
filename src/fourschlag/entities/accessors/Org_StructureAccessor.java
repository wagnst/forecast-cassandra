package fourschlag.entities.accessors;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;
import fourschlag.entities.Org_StructureEntity;

/**
 * Created by Henrik on 07.11.2016.
 */
@Accessor
public interface Org_StructureAccessor {
    @Query("SELECT product_main_group, sbu FROM org_structure;")
    Result<Org_StructureEntity> getProducts();
}
