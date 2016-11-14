package fourschlag.services.data.requests;

import com.datastax.driver.mapping.Result;
import fourschlag.entities.tables.OrgStructureEntity;
import fourschlag.entities.accessors.OrgStructureAccessor;
import fourschlag.services.db.CassandraConnection;


public class OrgStructureRequest extends Request {
    private OrgStructureAccessor orgStructureAccessor;

    public OrgStructureRequest(CassandraConnection connection) {
        super(connection);
        orgStructureAccessor = getManager().createAccessor(OrgStructureAccessor.class);
    }

    public Result<OrgStructureEntity> getProductMainGroups() {
        return orgStructureAccessor.getProducts();
    }
}
