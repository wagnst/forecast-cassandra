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
    /**
     * Selects all rows but only the columns product_main_group and sbu
     *
     * @return Iterable of entities mapped as OrgStructureEntity
     */
    @Query("SELECT product_main_group, sbu FROM org_structure;")
    Result<OrgStructureEntity> getProductsAndSbus();

    /**
     * Queries the table for all rows restricted by a specific sbu
     *
     * @param sbu primary key field sbu for where clause
     *
     * @return Iterable of entities mapped as OrgStructureEntity
     */
    @Query("SELECT * FROM org_structure WHERE sbu = :sbu ALLOW FILTERING")
    Result<OrgStructureEntity> getEntitiesBySbu(
            @Param("sbu") String sbu);

    /**
     * Queries the table for all rows restricted by a specific
     * product_main_group
     *
     * @param productMainGroup primary key field product_main_group for where
     *                         clause
     *
     * @return Iterable of entities mapped as OrgStructureEntity
     */
    @Query("SELECT * FROM org_structure WHERE product_main_group = :productMainGroup ALLOW FILTERING")
    Result<OrgStructureEntity> getEntitiesByPmg(
            @Param("productMainGroup") String productMainGroup);

    @Query("SELECT DISTINCT product_main_group FROM org_structure")
    Result<OrgStructureEntity> getDistinctPmg();
}
