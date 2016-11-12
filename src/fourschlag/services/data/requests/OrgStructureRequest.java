package fourschlag.services.data.requests;

import com.datastax.driver.mapping.Result;
import fourschlag.entities.OrgStructureEntity;
import fourschlag.entities.accessors.OrgStructureAccessor;


public class OrgStructureRequest extends Request {
    private OrgStructureAccessor orgStructureAccessor;
    private static OrgStructureRequest instance = null;

    private OrgStructureRequest() {
        super();
        orgStructureAccessor = getManager().createAccessor(OrgStructureAccessor.class);
    }

    private static OrgStructureRequest getInstance() {
        if (instance == null) {
            instance = new OrgStructureRequest();
        }
        return instance;
    }

    public static Result<OrgStructureEntity> getProductMainGroups() {
        return getInstance().orgStructureAccessor.getProducts();
    }
}
