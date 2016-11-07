package fourschlag.services.data;

import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import fourschlag.entities.ActualSalesEntity;
import fourschlag.entities.Org_StructureEntity;
import fourschlag.entities.RegionEntity;
import fourschlag.entities.accessors.ActualSalesAccessor;
import fourschlag.entities.accessors.Org_StructureAccessor;
import fourschlag.entities.accessors.RegionAccessor;

import java.util.ArrayList;
import java.util.List;

public class SalesService extends Service {
    private ActualSalesAccessor actual_accessor;
    private Org_StructureAccessor org_structure_accessor;
    private RegionAccessor region_accessor;

    public SalesService() {
        super("141.19.145.142", "original_version");
        MappingManager manager = new MappingManager(this.getSession());
        actual_accessor = manager.createAccessor(ActualSalesAccessor.class);
        org_structure_accessor = manager.createAccessor(Org_StructureAccessor.class);
        region_accessor = manager.createAccessor(RegionAccessor.class);
    }

    public List<String> getSomething() {
        List<String> resultList = new ArrayList<>();
        Result<ActualSalesEntity> queryResult = actual_accessor.getSomething();

        for (ActualSalesEntity e : queryResult) {
            resultList.add(e.getSbu());
        }

        return resultList;
    }

    public List<ActualSalesEntity> getKPIs(String product_main_group, int period) {
        List<ActualSalesEntity> resultList = new ArrayList<>();
        for (ActualSalesEntity e : actual_accessor.getKPIs(product_main_group, period)) {
            resultList.add(e);
        }
        return resultList;
    }

    //Test Method
    public List<OutputDataType> getSalesVolumes(String product_main_group, int year, String region) {
        List<OutputDataType> resultList = new ArrayList<>();
        OutputDataType data = new OutputDataType(KPIs.SALES_VOLUME);

        int period = year * 100 + 1;

        ActualSalesEntity queryResult = actual_accessor.getSalesVolumes(product_main_group, period++, region);
        data.setProductMainAndSBU(queryResult.getSbu(), product_main_group);
        data.setRegion(region);

        data.setM01(queryResult.getSales_volumes());
        data.setM03(actual_accessor.getSalesVolumes(product_main_group, period++, region).getSales_volumes());
        data.setM04(actual_accessor.getSalesVolumes(product_main_group, period++, region).getSales_volumes());
        data.setM05(actual_accessor.getSalesVolumes(product_main_group, period++, region).getSales_volumes());
        data.setM06(actual_accessor.getSalesVolumes(product_main_group, period++, region).getSales_volumes());
        data.setM07(actual_accessor.getSalesVolumes(product_main_group, period++, region).getSales_volumes());
        data.setM08(actual_accessor.getSalesVolumes(product_main_group, period++, region).getSales_volumes());
        data.setM09(actual_accessor.getSalesVolumes(product_main_group, period++, region).getSales_volumes());
        data.setM10(actual_accessor.getSalesVolumes(product_main_group, period++, region).getSales_volumes());
        data.setM11(actual_accessor.getSalesVolumes(product_main_group, period++, region).getSales_volumes());
        data.setM12(actual_accessor.getSalesVolumes(product_main_group, period++, region).getSales_volumes());

        //+89 to jump to the next year
        period += 89;
        System.out.println(period);
        data.setM13(actual_accessor.getSalesVolumes(product_main_group, period++, region).getSales_volumes());
        data.setM14(actual_accessor.getSalesVolumes(product_main_group, period++, region).getSales_volumes());
        data.setM15(actual_accessor.getSalesVolumes(product_main_group, period++, region).getSales_volumes());
        data.setM16(actual_accessor.getSalesVolumes(product_main_group, period++, region).getSales_volumes());
        data.setM17(actual_accessor.getSalesVolumes(product_main_group, period++, region).getSales_volumes());
        data.setM18(actual_accessor.getSalesVolumes(product_main_group, period++, region).getSales_volumes());

        resultList.add(data);
        return resultList;
    }



    public List<OutputDataType> getSalesKPIs(int year, int period, String currency) {
        List<OutputDataType> resultList = new ArrayList<>();

        Result<Org_StructureEntity> products = org_structure_accessor.getProducts();
        Result<RegionEntity> subregions = region_accessor.getSubregions();

        for (Org_StructureEntity product: products) {
            for (RegionEntity region: subregions) {
                getSalesKPIsForProductAndRegion(product.getProduct_main_group(), year, region.getSubregion(), "3rd_party");
                getSalesKPIsForProductAndRegion(product.getProduct_main_group(), year, region.getSubregion(), "transfer");
            }
        }


        return resultList;
    }

    private List<OutputDataType> getSalesKPIsForProductAndRegion(String product_main_group, int year, String region, String data_source) {
        List<OutputDataType> resultList = new ArrayList<>();

        //Create Objects for each KPI
        OutputDataType sales_volume = new OutputDataType(KPIs.SALES_VOLUME);
        OutputDataType net_sales = new OutputDataType(KPIs.NET_SALES);
        OutputDataType cm1 = new OutputDataType(KPIs.CM1);

        //Calculate first period of given year
        int period = year * 100 + 1;

        //Get KPIs for first month
        ActualSalesEntity m01 = actual_accessor.getSalesKPIs(product_main_group, period++, region, data_source);

        //Set all required attributes
        sales_volume.setProductMainAndSBU(m01.getSbu(), product_main_group);
        sales_volume.setRegion(region);

        net_sales.setProductMainAndSBU(m01.getSbu(), product_main_group);
        net_sales.setRegion(region);

        cm1.setProductMainAndSBU(m01.getSbu(), product_main_group);
        cm1.setRegion(region);

        //Set the KPIs for the first month
        sales_volume.setM01(m01.getSales_volumes());
        net_sales.setM01(m01.getNet_sales());
        cm1.setM01(m01.getCm1());

        //Do that for 18 month in total
        ActualSalesEntity m02 = actual_accessor.getSalesKPIs(product_main_group, period++, region, data_source);
        sales_volume.setM02(m02.getSales_volumes());
        net_sales.setM02(m02.getNet_sales());
        cm1.setM02(m02.getCm1());

        //to be continued

        return resultList;
    }
}

