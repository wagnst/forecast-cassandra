package fourschlag.services.data;

import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import fourschlag.entities.ActualSalesEntity;
import fourschlag.entities.Org_StructureEntity;
import fourschlag.entities.RegionEntity;
import fourschlag.entities.accessors.ActualSalesAccessor;
import fourschlag.entities.accessors.Org_StructureAccessor;
import fourschlag.entities.accessors.RegionAccessor;

import java.util.*;

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
        //TODO currency, data_source
        List<OutputDataType> resultList = new ArrayList<>();

        Result<Org_StructureEntity> products = org_structure_accessor.getProducts();
        Result<RegionEntity> subregions = region_accessor.getSubregions();
        Set<String> regions = new HashSet<>();
        for (RegionEntity region : subregions) {
            regions.add(region.getRegion());
        }

        for (Org_StructureEntity product : products) {
            for (String region : regions) {
                resultList.addAll(getSalesKPIsForProductAndRegion(product.getProduct_main_group(), product.getSbu(), year, region, "3rd_party"));
                resultList.addAll(getSalesKPIsForProductAndRegion(product.getProduct_main_group(), product.getSbu(), year, region, "transfer"));
            }
        }

        return resultList;
    }

    private List<OutputDataType> getSalesKPIsForProductAndRegion(String product_main_group, String sbu, int year, String region, String sales_type) {
        List<OutputDataType> resultList = new ArrayList<>();

        //Calculate first period of given year
        int period = year * 100 + 1;

        //Get KPIs for first month
        System.out.println(product_main_group + period + region + sales_type);

        LinkedList<Double> salesVolumesMonths = new LinkedList<>();
        LinkedList<Double> netSalesMonths = new LinkedList<>();
        LinkedList<Double> cm1Months = new LinkedList<>();

        ActualSalesEntity queryResult;
        //Set the KPIs for the first month
        for (int i = 0; i < 18; i++) {
            queryResult = actual_accessor.getSalesKPIs(product_main_group, period++, region, sales_type);
            if (queryResult != null) {
                salesVolumesMonths.add(queryResult.getSales_volumes());
                netSalesMonths.add(queryResult.getNet_sales());
                cm1Months.add(queryResult.getCm1());
            } else {
                salesVolumesMonths.add(new Double(0));
                netSalesMonths.add(new Double(0));
                cm1Months.add(new Double(0));
            }
        }

        //Create Objects for each KPI
        OutputDataType salesVolume = new OutputDataType(KPIs.SALES_VOLUME, sbu, product_main_group, region, sales_type,
                salesVolumesMonths);
        OutputDataType net_sales = new OutputDataType(KPIs.NET_SALES, sbu, product_main_group, region, sales_type,
                netSalesMonths);
        OutputDataType cm1 = new OutputDataType(KPIs.CM1, sbu, product_main_group, region, sales_type, cm1Months);

        resultList.add(salesVolume);
        resultList.add(net_sales);
        resultList.add(cm1);

        return resultList;
    }
}

