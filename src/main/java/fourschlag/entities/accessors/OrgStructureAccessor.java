package fourschlag.entities.accessors;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import fourschlag.entities.tables.OrgStructureEntity;

@Accessor
public interface OrgStructureAccessor {
    @Query("SELECT product_main_group, sbu FROM org_structure;")
    Result<OrgStructureEntity> getProductsAndSbus();

    @Query("SELECT sbu FROM org_structure WHERE product_main_group = :product_main_group;")
    OrgStructureEntity getSbuForProductMainGroup(@Param("product_main_group") String productMainGroup);
}
