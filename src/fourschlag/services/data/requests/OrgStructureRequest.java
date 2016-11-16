package fourschlag.services.data.requests;

import com.datastax.driver.mapping.Result;
import fourschlag.entities.accessors.OrgStructureAccessor;
import fourschlag.entities.tables.OrgStructureEntity;
import fourschlag.services.db.CassandraConnection;

/**
 * Extends Request. Offers functionality to request product main groups and sbus.
 */
public class OrgStructureRequest extends Request {
    private OrgStructureAccessor orgStructureAccessor;

    /**
     * Constructor for OrgStructureRequest.
     *
     * @param connection Cassandra connection that is supposed to be used
     */
    public OrgStructureRequest(CassandraConnection connection) {
        super(connection);
        orgStructureAccessor = getManager().createAccessor(OrgStructureAccessor.class);
    }

    /**
     * Queries the database for all Product Main Groups
     *
     * @return Result Iterable with multiple OrgStructure entities
     */
    public Result<OrgStructureEntity> getProductMainGroups() {
        return orgStructureAccessor.getProducts();
    }
}
