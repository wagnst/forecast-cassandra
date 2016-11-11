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

    public List<OutputDataType> getSalesKPIs(int planYear, int currentPeriodInt, String currency) {
        List<OutputDataType> resultList = new ArrayList<>();

        Result<OrgStructureEntity> products = orgStructureAccessor.getProducts();
        Result<RegionEntity> subregions = regionAccessor.getSubregions();

        Period currentPeriod = new Period(currentPeriodInt);

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
                            product.getProductMainGroup(), product.getSbu(), planYear, currentPeriod, region, salesType
                    ));
            }
        }

        return resultList;
    }

    private List<OutputDataType> getSalesKPIsForProductAndRegion(String productMainGroup, String sbu, int planYear,
                                                                 Period currentPeriod, String region, SalesType salesType) {
        // TODO: Es fehlen noch: Price, var_costs, cm1_specific, cm1_percent --> Berechnen sich aus den anderen KeyPerformanceIndicators
        // TODO: Währungsumrechnung

        List<OutputDataType> resultList = new ArrayList<>();

        /* Get the Plan Period by taking the given plan year and jump to its first month. The Plan Period will be
         * incremented, while the current Period stays the same, because it represents the point of view/time from which
          * we are accessing the data */
        Period planPeriod = Period.getPeriodByYear(planYear);

        //System.out.println(productMainGroup + " " + planPeriod + " " + region + " " + salesType);

        /* Create Lists for each KPI to store the values of the 18 months */
        LinkedList<Double> salesVolumesMonths = new LinkedList<>();
        LinkedList<Double> netSalesMonths = new LinkedList<>();
        LinkedList<Double> cm1Months = new LinkedList<>();
        LinkedList<Double> priceMonths = new LinkedList<>();
        LinkedList<Double> varCostMonths = new LinkedList<>();
        LinkedList<Double> cm1SpecificMonths = new LinkedList<>();
        LinkedList<Double> cm1PercentMonths = new LinkedList<>();

        SalesEntity queryResult;
        boolean actualFlag = false;
        boolean forecastFlag = false;

        double currentSalesVolume;
        double currentNetSales;
        double currentCm1;

        for (int i = 0; i < MONTHS_AMOUNT; i++) {
            /* Compare plan period with current period
               If plan period in past (plan: 201610, current: 201611) use actual data.
               Else forecast data
             */
            /* TODO: IF planPeriod is currentPeriod - 1, THEN try to get actual data. IF actual data is not available, THEN use forecast data */

            if (planPeriod.getPeriod() < currentPeriod.getPeriod()) {
                actualFlag = true;
                queryResult = actualAccessor.getSalesKPIs(productMainGroup, planPeriod.getPeriod(), region,
                        salesType.getType(), "BW B");
                if (queryResult == null) {
                    queryResult = actualAccessor.getSalesKPIs(productMainGroup, planPeriod.getPeriod(), region,
                            salesType.getType(), "BW A");
                }
            } else {
                forecastFlag = true;
                queryResult = forecastAccessor.getSalesKPI(productMainGroup, currentPeriod.getPeriod(),
                        planPeriod.getPeriod(), region, salesType.toString());
            }

            /* Check if queryResult has data and save the data temporarily because we need the values again for calculations */
            if (queryResult == null) {
                currentSalesVolume = 0;
                currentNetSales = 0;
                currentCm1 = 0;
            } else {
                currentSalesVolume = queryResult.getSalesVolumes();
                currentNetSales = queryResult.getNetSales();
                currentCm1 = queryResult.getCm1();
            }

            /* IF sales volume is 0 THEN set these KPIs also to 0. ELSE calculate the other KPIs*/
            if(currentSalesVolume == 0) {
                priceMonths.add(new Double(0));
                varCostMonths.add(new Double(0));
                cm1SpecificMonths.add(new Double(0));
                cm1PercentMonths.add(new Double(0));
            } else {
                priceMonths.add(currentNetSales/currentSalesVolume*1000);
                varCostMonths.add((currentNetSales-currentCm1)*1000/currentSalesVolume);
                cm1SpecificMonths.add(currentCm1/currentSalesVolume*1000);
                cm1PercentMonths.add(currentCm1/currentNetSales*100);
            }

            /* Put the temporarily saved KPIs into the List*/
            salesVolumesMonths.add(currentSalesVolume);
            netSalesMonths.add(currentNetSales);
            cm1Months.add(currentCm1);

            /* Increment the planPeriod to jump to the next month */
            planPeriod.increment();
        }

        /* Check flags to set what entry type was used */
        EntryType entryType;
        if (actualFlag && forecastFlag) {
            entryType = EntryType.ACTUAL_FORECAST;
        } else if (forecastFlag) {
            entryType = EntryType.FORECAST;
        } else {
            entryType = EntryType.ACTUAL;
        }

        // TODO: Think about putting all these objects into Lists and then be able to iterate over them. Less performant but nicer to look at.
        /* Create Objects for each KPI */
        OutputDataType salesVolumes = new OutputDataType(KeyPerformanceIndicators.SALES_VOLUME, sbu, productMainGroup,
                region, region, salesType.toString(), entryType.toString(), salesVolumesMonths);

        OutputDataType netSales = new OutputDataType(KeyPerformanceIndicators.NET_SALES, sbu, productMainGroup, region,
                region, salesType.toString(), entryType.toString(), netSalesMonths);

        OutputDataType cm1 = new OutputDataType(KeyPerformanceIndicators.CM1, sbu, productMainGroup, region, region,
                salesType.toString(), entryType.toString(), cm1Months);

        OutputDataType price = new OutputDataType(KeyPerformanceIndicators.PRICE, sbu, productMainGroup, region, region,
                salesType.toString(), entryType.toString(), priceMonths);

        OutputDataType varCost = new OutputDataType(KeyPerformanceIndicators.VAR_COSTS, sbu, productMainGroup, region,
                region, salesType.toString(), entryType.toString(), varCostMonths);

        OutputDataType cm1Specific = new OutputDataType(KeyPerformanceIndicators.CM1_SPECIFIC, sbu, productMainGroup,
                region, region, salesType.toString(), entryType.toString(), cm1SpecificMonths);

        OutputDataType cm1Percent = new OutputDataType(KeyPerformanceIndicators.CM1_PERCENT, sbu, productMainGroup,
                region, region, salesType.toString(), entryType.toString(), cm1PercentMonths);

        /* Add all KPI Objects to the list that is returned by this method */
        resultList.add(salesVolumes);
        resultList.add(netSales);
        resultList.add(cm1);
        resultList.add(price);
        resultList.add(varCost);
        resultList.add(cm1Specific);
        resultList.add(cm1Percent);

        return resultList;
    }
}