package fourschlag.services.data;

import fourschlag.entities.OrgStructureEntity;
import fourschlag.entities.types.OutputDataType;
import fourschlag.entities.types.Period;
import fourschlag.entities.types.SalesType;
import fourschlag.services.data.requests.OrgStructureRequest;
import fourschlag.services.data.requests.RegionRequest;
import fourschlag.services.data.requests.SalesRequest;

import java.util.ArrayList;
import java.util.List;

public class SalesService extends Service {

    public SalesService() {
        super();
    }

    public List<OutputDataType> getSalesKPIs(int planYear, int currentPeriodInt, String currency) {
        List<OutputDataType> resultList = new ArrayList<>();

        Period currentPeriod = new Period(currentPeriodInt);

        /* fill result list and calculate KPI's */
        for (OrgStructureEntity product : OrgStructureRequest.getProductMainGroups()) {
            for (String region : RegionRequest.getRegions()) {
                /* use sales_types from enum, instead of mapped ones */
                for (SalesType salesType : SalesType.values())
                    resultList.addAll(new SalesRequest(
                            product.getProductMainGroup(), product.getSbu(), planYear, currentPeriod, region, salesType
                    ).getSalesKPIs());
            }
        }

        return resultList;
    }
}