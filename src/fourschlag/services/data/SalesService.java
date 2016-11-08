package fourschlag.services.data;

import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import fourschlag.entities.ActualSalesEntity;
import fourschlag.entities.OrgStructureEntity;
import fourschlag.entities.RegionEntity;
import fourschlag.entities.accessors.ActualSalesAccessor;
import fourschlag.entities.accessors.OrgStructureAccessor;
import fourschlag.entities.accessors.RegionAccessor;

import java.util.*;

public class SalesService extends Service {
    private ActualSalesAccessor actualAccessor;
    private OrgStructureAccessor orgStructureAccessor;
    private RegionAccessor regionAccessor;

    private static final int MONTHS_AMOUNT = 18;

    public SalesService() {
        super("141.19.145.142", "original_version");
        MappingManager manager = new MappingManager(this.getSession());
        actualAccessor = manager.createAccessor(ActualSalesAccessor.class);
        orgStructureAccessor = manager.createAccessor(OrgStructureAccessor.class);
        regionAccessor = manager.createAccessor(RegionAccessor.class);
    }

    public List<OutputDataType> getSalesKPIs(int year, int intPeriod, String currency) {
        //TODO currency, data_source
        List<OutputDataType> resultList = new ArrayList<>();

        Period period = new Period(intPeriod);

        Result<OrgStructureEntity> products = orgStructureAccessor.getProducts();
        Result<RegionEntity> subregions = regionAccessor.getSubregions();

        //Regions abfragen, Duplikate filtern
        Set<String> regions = new HashSet<>();
        for (RegionEntity region : subregions) {
            regions.add(region.getRegion());
        }

        for (OrgStructureEntity product : products) {
            for (String region : regions) {
                resultList.addAll(getSalesKPIsForProductAndRegion(product.getProduct_main_group(), product.getSbu()
                                    , period, region, "3rd_party"));
                resultList.addAll(getSalesKPIsForProductAndRegion(product.getProduct_main_group(), product.getSbu()
                                    , period, region, "transfer"));
            }
        }

        return resultList;
    }

    private List<OutputDataType> getSalesKPIsForProductAndRegion(String product_main_group, String sbu,
                                                                 Period parameterPeriod, String region, String sales_type) {
        List<OutputDataType> resultList = new ArrayList<>();

        Period period = parameterPeriod;

        //Get KPIs for first month
        System.out.println(product_main_group + " " + period + " " + region + " " + sales_type);

        LinkedList<Double> salesVolumesMonths = new LinkedList<>();
        LinkedList<Double> netSalesMonths = new LinkedList<>();
        LinkedList<Double> cm1Months = new LinkedList<>();
        ActualSalesEntity queryResult;

        //Set the KPIs for 18 months
        for (int i = 0; i < MONTHS_AMOUNT; i++) {
            queryResult = actualAccessor.getSalesKPIs(product_main_group, period.getPeriod(), region, sales_type);
            if (queryResult != null) {
                salesVolumesMonths.add(queryResult.getSales_volumes());
                netSalesMonths.add(queryResult.getNet_sales());
                cm1Months.add(queryResult.getCm1());
            } else {
                salesVolumesMonths.add(new Double(0));
                netSalesMonths.add(new Double(0));
                cm1Months.add(new Double(0));
            }

            period.increment();
        }

        //Create Objects for each KPI
        OutputDataType salesVolume = new OutputDataType(KPIs.SALES_VOLUME, sbu, product_main_group, region, region,
                sales_type, salesVolumesMonths);
        OutputDataType net_sales = new OutputDataType(KPIs.NET_SALES, sbu, product_main_group, region, region,
                sales_type, netSalesMonths);
        OutputDataType cm1 = new OutputDataType(KPIs.CM1, sbu, product_main_group, region, region, sales_type,
                cm1Months);

        resultList.add(salesVolume);
        resultList.add(net_sales);
        resultList.add(cm1);

        return resultList;
    }
}

