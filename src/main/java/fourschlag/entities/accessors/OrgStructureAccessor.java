package fourschlag.entities.accessors;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import fourschlag.entities.tables.OrgStructureEntity;

/**
 * Provides functionality to query the database for the Product Main Groups and
 * SBUs
 */

@Accessor
public interface OrgStructureAccessor {
    /*CQL-Query to get the OrgStructure data */
    @Query("SELECT product_main_group, sbu FROM org_structure;")
    Result<OrgStructureEntity> getProductsAndSbus();

    @Query("SELECT * FROM org_structure WHERE sbu = :sbu ALLOW FILTERING")
    Result<OrgStructureEntity> getEntitiesBySbu(
            @Param("sbu") String sbu);

    @Query("SELECT * FROM org_structure WHERE product_main_group = :productMainGroup ALLOW FILTERING")
    Result<OrgStructureEntity> getEntitiesByPmg(
            @Param("productMainGroup") String productMainGroup);
}
