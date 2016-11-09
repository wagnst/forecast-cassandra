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
import fourschlag.entities.types.*;

import java.time.LocalDateTime;
import java.util.*;

public class SalesService extends Service {
    private ActualSalesAccessor actualAccessor;
    private ForecastSalesAccessor forecastAccessor;
    private OrgStructureAccessor orgStructureAccessor;
    private RegionAccessor regionAccessor;

    private static final int MONTHS_AMOUNT = 18;

    public SalesService() {
        super();
        MappingManager manager = new MappingManager(this.getSession());
        actualAccessor = manager.createAccessor(ActualSalesAccessor.class);
        forecastAccessor = manager.createAccessor(ForecastSalesAccessor.class);
        orgStructureAccessor = manager.createAccessor(OrgStructureAccessor.class);
        regionAccessor = manager.createAccessor(RegionAccessor.class);
    }

    public List<OutputDataType> getSalesKPIs(int planYear, int period, String currency) {
        List<OutputDataType> resultList = new ArrayList<>();

        Result<OrgStructureEntity> products = orgStructureAccessor.getProducts();
        Result<RegionEntity> subregions = regionAccessor.getSubregions();

        Period requestedPeriod = new Period(period);

        /* query regions, filter duplicates */
        Set<String> regions = new HashSet<>();
        for (RegionEntity region : subregions) {
            regions.add(region.getRegion());
        }

        /* fill result list and calculate KPI's */
        for (OrgStructureEntity product : products) {
            for (String region : regions) {
                /* use sales_types from enum, instead of mapped ones */
                for (SalesType salesType : SalesType.values())
                    resultList.addAll(getSalesKPIsForProductAndRegion(
                            product.getProductMainGroup(), product.getSbu(), planYear, requestedPeriod, region, salesType
                    ));
            }
        }

        return resultList;
    }

    private List<OutputDataType> getSalesKPIsForProductAndRegion(String productMainGroup, String sbu, int planYear,
                                                                 Period period, String region, SalesType salesType) {
        // TODO: Es fehlen noch: Price, var_costs, cm1_specific, cm1_percent --> Berechnen sich aus den anderen KeyPerformanceIndicators
        // TODO: Währungsumrechnung

        List<OutputDataType> resultList = new ArrayList<>();

        Period requestedPeriod = period.getFirstPeriodOfYear();
        LocalDateTime now = LocalDateTime.now();
        Period currentPeriod = new Period(now.getYear(), now.getMonth().getValue());

        //System.out.println(productMainGroup + " " + requestedPeriod + " " + region + " " + salesType);

        LinkedList<Double> salesVolumesMonths = new LinkedList<>();
        LinkedList<Double> netSalesMonths = new LinkedList<>();
        LinkedList<Double> cm1Months = new LinkedList<>();

        SalesEntity queryResult;
        boolean actualFlag = false;
        boolean forecastFlag = false;

        for (int i = 0; i < MONTHS_AMOUNT; i++) {
            /* Compare requested period with current period
               If requested period in history (requested: 201610, current: 201611) use actual data.
               Else forecast data
             */
            if (requestedPeriod.getPeriod() < currentPeriod.getPeriod()) {
                actualFlag = true;
                queryResult = actualAccessor.getSalesKPIs(productMainGroup, requestedPeriod.getPeriod(), region,
                        salesType.getType(), "BW B");
                if (queryResult == null) {
                    queryResult = actualAccessor.getSalesKPIs(productMainGroup, requestedPeriod.getPeriod(), region,
                            salesType.getType(), "BW A");
                }
            } else {
                forecastFlag = true;
                //TODO: Add case for entry_type = budget (--> kpi description in GDrive)
                queryResult = forecastAccessor.getSalesKPI(productMainGroup, requestedPeriod.getPeriod(), planYear,
                                                            region, salesType.toString(), "forecast");
            }

            /* Check if queryResult has data */
            if (queryResult == null) {
                salesVolumesMonths.add(new Double(0));
                netSalesMonths.add(new Double(0));
                cm1Months.add(new Double(0));
            } else {
                salesVolumesMonths.add(queryResult.getSalesVolumes());
                netSalesMonths.add(queryResult.getNetSales());
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

        /* Create Objects for each KPI */
        OutputDataType salesVolume = new OutputDataType(KeyPerformanceIndicators.SALES_VOLUME, sbu, productMainGroup, region, region,
                salesType.toString(), entryType.toString(), salesVolumesMonths);
        OutputDataType netSales = new OutputDataType(KeyPerformanceIndicators.NET_SALES, sbu, productMainGroup, region, region,
                salesType.toString(), entryType.toString(), netSalesMonths);
        OutputDataType cm1 = new OutputDataType(KeyPerformanceIndicators.CM1, sbu, productMainGroup, region, region, salesType.toString(),
                entryType.toString(), cm1Months);

        resultList.add(salesVolume);
        resultList.add(netSales);
        resultList.add(cm1);

        return resultList;
    }
}