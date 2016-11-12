package fourschlag.services.data.requests;

import fourschlag.entities.SalesEntity;
import fourschlag.entities.accessors.ActualSalesAccessor;
import fourschlag.entities.accessors.ForecastSalesAccessor;
import fourschlag.entities.types.*;
import fourschlag.services.db.CassandraConnection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SalesRequest extends KpiRequest {
    private String productMainGroup;
    private String sbu;
    private Period planPeriod;
    private Period currentPeriod;
    private String region;
    private SalesType salesType;

    private ActualSalesAccessor actualAccessor;
    private ForecastSalesAccessor forecastAccessor;


    public SalesRequest(CassandraConnection connection, String productMainGroup, String sbu, int planYear,
                        Period currentPeriod, String region, SalesType salesType) {
        super(connection);
        this.productMainGroup = productMainGroup;
        this.sbu = sbu;
        this.planPeriod = Period.getPeriodByYear(planYear);
        this.currentPeriod = currentPeriod;
        this.region = region;
        this.salesType = salesType;
        actualAccessor = getManager().createAccessor(ActualSalesAccessor.class);
        forecastAccessor = getManager().createAccessor(ForecastSalesAccessor.class);
    }

    public List<OutputDataType> getSalesKPIs() {
        //System.out.println(productMainGroup + " " + region + " " + salesType);
        List<OutputDataType> resultList = new ArrayList<>();

        /* Create Lists for each KPI to store the values of the 18 months */
        LinkedList<Double> salesVolumesMonths = new LinkedList<>();
        LinkedList<Double> netSalesMonths = new LinkedList<>();
        LinkedList<Double> cm1Months = new LinkedList<>();
        LinkedList<Double> priceMonths = new LinkedList<>();
        LinkedList<Double> varCostMonths = new LinkedList<>();
        LinkedList<Double> cm1SpecificMonths = new LinkedList<>();
        LinkedList<Double> cm1PercentMonths = new LinkedList<>();

        LinkedList<Double> kpisForOneMonth;

        for (int i = 0; i < getNumberOfMonths(); i++) {
            kpisForOneMonth = getSalesKPIsForSpecificMonth();
            salesVolumesMonths.add(kpisForOneMonth.poll());
            netSalesMonths.add(kpisForOneMonth.poll());
            cm1Months.add(kpisForOneMonth.poll());
            priceMonths.add(kpisForOneMonth.poll());
            varCostMonths.add(kpisForOneMonth.poll());
            cm1SpecificMonths.add(kpisForOneMonth.poll());
            cm1PercentMonths.add(kpisForOneMonth.poll());
        }

        //TODO: Set EntryType correctly
        resultList.add(createOutputDataType(KeyPerformanceIndicators.SALES_VOLUME, EntryType.ACTUAL, salesVolumesMonths));
        resultList.add(createOutputDataType(KeyPerformanceIndicators.NET_SALES, EntryType.ACTUAL, netSalesMonths));
        resultList.add(createOutputDataType(KeyPerformanceIndicators.CM1, EntryType.ACTUAL, cm1Months));
        resultList.add(createOutputDataType(KeyPerformanceIndicators.PRICE, EntryType.ACTUAL, priceMonths));
        resultList.add(createOutputDataType(KeyPerformanceIndicators.VAR_COSTS, EntryType.ACTUAL, varCostMonths));
        resultList.add(createOutputDataType(KeyPerformanceIndicators.CM1_SPECIFIC, EntryType.ACTUAL, cm1SpecificMonths));
        resultList.add(createOutputDataType(KeyPerformanceIndicators.CM1_PERCENT, EntryType.ACTUAL, cm1PercentMonths));

        return resultList;
    }

    /* TODO: Return data type maybe as HashMap? */
    private LinkedList<Double> getSalesKPIsForSpecificMonth() {
        double salesVolume;
        double netSales;
        double cm1;
        double price;
        double varCost;
        double cm1Specific;
        double cm1Percent;

        SalesEntity queryResult;

        if (planPeriod.getPeriod() < currentPeriod.getPeriod()) {
            queryResult = getActualData();
        } else {
            queryResult = getForecastData();
        }

        if (queryResult == null) {
            salesVolume = 0;
            netSales = 0;
            cm1 = 0;
        } else {
            salesVolume = queryResult.getSalesVolumes();
            netSales = queryResult.getNetSales();
            cm1 = queryResult.getCm1();
        }

        if (salesVolume == 0) {
            price = 0;
            varCost = 0;
            cm1Specific = 0;
        } else {
            price = netSales / salesVolume * 1000;
            varCost = (netSales - cm1) * 1000 / salesVolume;
            cm1Specific = cm1 / salesVolume * 1000;
        }

        if (netSales == 0) {
            cm1Percent = 0;
        } else {
            cm1Percent = cm1 / netSales;
        }

        LinkedList<Double> resultList = new LinkedList<>(Arrays.asList(salesVolume, netSales, cm1, price, varCost, cm1Specific, cm1Percent));
        return resultList;
    }

    private SalesEntity getActualData() {
        SalesEntity queryResult;
        queryResult = actualAccessor.getSalesKPIs(productMainGroup, planPeriod.getPeriod(), region,
                salesType.getType(), "BW B");
        if (queryResult == null) {
            queryResult = actualAccessor.getSalesKPIs(productMainGroup, planPeriod.getPeriod(), region,
                    salesType.getType(), "BW A");
            if (queryResult != null) {
                queryResult.setCm1(getForecastCm1());
            }
        }


        return queryResult;
    }

    private SalesEntity getForecastData() {
        return forecastAccessor.getSalesKPI(productMainGroup, currentPeriod.getPeriod(),
                planPeriod.getPeriod(), region, salesType.toString());
    }

    private double getForecastCm1() {
        SalesEntity cm1 = forecastAccessor.getCm1(productMainGroup, currentPeriod.getPeriod(),
                planPeriod.getPeriod(), region, salesType.toString());
        if (cm1 == null) {
            return 0;
        }
        return cm1.getCm1();
    }

    private OutputDataType createOutputDataType(KeyPerformanceIndicators kpi, EntryType entryType, LinkedList<Double> monthlyValues) {
        return new OutputDataType(kpi, sbu, productMainGroup,
                region, region, salesType.toString(), entryType.toString(), monthlyValues);
    }

}
