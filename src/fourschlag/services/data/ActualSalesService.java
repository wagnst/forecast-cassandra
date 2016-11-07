package fourschlag.services.data;

import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import fourschlag.entities.ActualSalesEntity;
import fourschlag.entities.accessors.ActualSalesAccessor;
import jnr.ffi.annotations.Out;

import java.util.ArrayList;
import java.util.List;

public class ActualSalesService extends Service {
    private ActualSalesAccessor accessor;

    public ActualSalesService() {
        super("141.19.145.142", "original_version");
        MappingManager manager = new MappingManager(this.getSession());
        accessor = manager.createAccessor(ActualSalesAccessor.class);
    }

    public List<String> getSomething() {
        List<String> resultList = new ArrayList<>();
        Result<ActualSalesEntity> queryResult = accessor.getSomething();

        for (ActualSalesEntity e : queryResult) {
            resultList.add(e.getSbu());
        }

        return resultList;
    }

    public List<ActualSalesEntity> getKPIs(String product_main_group, int period) {
        List<ActualSalesEntity> resultList = new ArrayList<>();
        for (ActualSalesEntity e : accessor.getKPIs(product_main_group, period)) {
            resultList.add(e);
        }
        return resultList;
    }

    //Test Method
    public List<OutputDataType> getSalesVolumes(String product_main_group, int year, String region) {
        List<OutputDataType> resultList = new ArrayList<>();
        OutputDataType data = new OutputDataType(KPIs.SALES_VOLUME);

        int period = year * 100 + 1;

        ActualSalesEntity queryResult = accessor.getSalesVolumes(product_main_group, period++, region);
        data.setProductMainAndSBU(queryResult.getSbu(), product_main_group);
        data.setRegion(region);

        data.setM01(queryResult.getSales_volumes());
        data.setM03(accessor.getSalesVolumes(product_main_group, period++, region).getSales_volumes());
        data.setM04(accessor.getSalesVolumes(product_main_group, period++, region).getSales_volumes());
        data.setM05(accessor.getSalesVolumes(product_main_group, period++, region).getSales_volumes());
        data.setM06(accessor.getSalesVolumes(product_main_group, period++, region).getSales_volumes());
        data.setM07(accessor.getSalesVolumes(product_main_group, period++, region).getSales_volumes());
        data.setM08(accessor.getSalesVolumes(product_main_group, period++, region).getSales_volumes());
        data.setM09(accessor.getSalesVolumes(product_main_group, period++, region).getSales_volumes());
        data.setM10(accessor.getSalesVolumes(product_main_group, period++, region).getSales_volumes());
        data.setM11(accessor.getSalesVolumes(product_main_group, period++, region).getSales_volumes());
        data.setM12(accessor.getSalesVolumes(product_main_group, period++, region).getSales_volumes());

        //+89 to jump to the next year

        period += 89;
        System.out.println(period);
        data.setM13(accessor.getSalesVolumes(product_main_group, period++, region).getSales_volumes());
        data.setM14(accessor.getSalesVolumes(product_main_group, period++, region).getSales_volumes());
        data.setM15(accessor.getSalesVolumes(product_main_group, period++, region).getSales_volumes());
        data.setM16(accessor.getSalesVolumes(product_main_group, period++, region).getSales_volumes());
        data.setM17(accessor.getSalesVolumes(product_main_group, period++, region).getSales_volumes());
        data.setM18(accessor.getSalesVolumes(product_main_group, period++, region).getSales_volumes());

        resultList.add(data);
        return resultList;
    }
}

