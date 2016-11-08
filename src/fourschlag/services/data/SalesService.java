package fourschlag.services.data;

import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import fourschlag.entities.OrgStructureEntity;
import fourschlag.entities.RegionEntity;
import fourschlag.entities.SalesEntity;
import fourschlag.entities.accessors.ActualSalesAccessor;
import fourschlag.entities.accessors.ForecastSalesAccessor;
import fourschlag.entities.accessors.OrgStructureAccessor;
import fourschlag.entities.accessors.RegionAccessor;

import java.time.LocalDateTime;
import java.util.*;

public class SalesService extends Service {
    private ActualSalesAccessor actualAccessor;
    private ForecastSalesAccessor forecastAccessor;
    private OrgStructureAccessor orgStructureAccessor;
    private RegionAccessor regionAccessor;

    private static final int MONTHS_AMOUNT = 18;

    public SalesService() {
        super("141.19.145.142", "original_version");
        MappingManager manager = new MappingManager(this.getSession());
        actualAccessor = manager.createAccessor(ActualSalesAccessor.class);
        forecastAccessor = manager.createAccessor(ForecastSalesAccessor.class);
        orgStructureAccessor = manager.createAccessor(OrgStructureAccessor.class);
        regionAccessor = manager.createAccessor(RegionAccessor.class);
    }

    public List<OutputDataType> getSalesKPIs(int year, int period, String currency) {
        List<OutputDataType> resultList = new ArrayList<>();

        Result<OrgStructureEntity> products = orgStructureAccessor.getProducts();
        Result<RegionEntity> subregions = regionAccessor.getSubregions();

        Period requestedPeriod = new Period(period);

        //Regions abfragen, Duplikate filtern
        Set<String> regions = new HashSet<>();
        for (RegionEntity region : subregions) {
            regions.add(region.getRegion());
        }

        for (OrgStructureEntity product : products) {
            for (String region : regions) {
                resultList.addAll(getSalesKPIsForProductAndRegion(product.getProduct_main_group(), product.getSbu()
                        , requestedPeriod, region, "3rd_party"));
                resultList.addAll(getSalesKPIsForProductAndRegion(product.getProduct_main_group(), product.getSbu()
                        , requestedPeriod, region, "transfer"));
            }
        }

        return resultList;
    }

    private List<OutputDataType> getSalesKPIsForProductAndRegion(String product_main_group, String sbu,
                                                                 Period period, String region, String sales_type) {

        // TODO: Es fehlen noch: Price, var_costs, cm1_specific, cm1_percent --> Berechnen sich aus den anderen KPIs
        // TODO: Währungsumrechnung

        List<OutputDataType> resultList = new ArrayList<>();

        Period requestedPeriod = period.getFirstPeriodOfYear();
        LocalDateTime now = LocalDateTime.now();
        Period currentPeriod = new Period(now.getYear(), now.getMonth().getValue());

        System.out.println(product_main_group + " " + requestedPeriod + " " + region + " " + sales_type);

        LinkedList<Double> salesVolumesMonths = new LinkedList<>();
        LinkedList<Double> netSalesMonths = new LinkedList<>();
        LinkedList<Double> cm1Months = new LinkedList<>();

        SalesEntity queryResult;
        boolean actualFlag = false;
        boolean forecastFlag = false;

        //Set the KPIs for 18 months
        for (int i = 0; i < MONTHS_AMOUNT; i++) {
            /* Compare requested period with current period
               If requested period in history (requested: 201610, current: 201611) use actual data.
               Else forecast data
             */
            if (requestedPeriod.getPeriod() < currentPeriod.getPeriod()) {
                actualFlag = true;
                queryResult = actualAccessor.getSalesKPIs(product_main_group, requestedPeriod.getPeriod(), region,
                                                            sales_type, "BW B");
                if (queryResult == null) {
                    queryResult = actualAccessor.getSalesKPIs(product_main_group, requestedPeriod.getPeriod(), region,
                                                                sales_type, "BW A");
                }
            } else {
                forecastFlag = true;
                queryResult = forecastAccessor.getSalesKPI(product_main_group, requestedPeriod.getPeriod(), region, sales_type);
            }

            //Check if queryResult has data
            if (queryResult == null) {
                salesVolumesMonths.add(new Double(0));
                netSalesMonths.add(new Double(0));
                cm1Months.add(new Double(0));
            } else {
                salesVolumesMonths.add(queryResult.getSales_volumes());
                netSalesMonths.add(queryResult.getNet_sales());
                cm1Months.add(queryResult.getCm1());
            }
            requestedPeriod.increment();
        }

        /* Check flags to decide what entry type was used */
        EntryType entryType;
        if (actualFlag && forecastFlag) {
            entryType = EntryType.ACTUAL_FORECAST;
        } else if (forecastFlag) {
            entryType = EntryType.FORECAST;
        } else {
            entryType = EntryType.ACTUAL;
        }


        //Create Objects for each KPI
        OutputDataType salesVolume = new OutputDataType(KPIs.SALES_VOLUME, sbu, product_main_group, region, region,
                sales_type, entryType, salesVolumesMonths);
        OutputDataType netSales = new OutputDataType(KPIs.NET_SALES, sbu, product_main_group, region, region,
                sales_type, entryType,  netSalesMonths);
        OutputDataType cm1 = new OutputDataType(KPIs.CM1, sbu, product_main_group, region, region, sales_type,
                entryType, cm1Months);

        resultList.add(salesVolume);
        resultList.add(netSales);
        resultList.add(cm1);

        return resultList;
    }
}

