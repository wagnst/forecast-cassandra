package fourschlag.services.data.requests;

import fourschlag.entities.tables.SalesEntity;
import fourschlag.entities.accessors.ActualSalesAccessor;
import fourschlag.entities.accessors.ForecastSalesAccessor;
import fourschlag.entities.types.*;
import fourschlag.services.data.Service;
import fourschlag.services.db.CassandraConnection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static fourschlag.entities.types.KeyPerformanceIndicators.*;

public class SalesRequest extends KpiRequest {
    private static boolean actualFlag = false;
    private static boolean forecastFlag = false;
    private String productMainGroup;
    private String sbu;
    private Period planPeriod;
    private Period currentPeriod;
    private String region;
    private SalesType salesType;
    private ExchangeRateRequest exchangeRates;

    private EntryType entryType;
    private ActualSalesAccessor actualAccessor;
    private ForecastSalesAccessor forecastAccessor;


    public SalesRequest(CassandraConnection connection, String productMainGroup, String sbu, int planYear,
                        Period currentPeriod, String region, SalesType salesType, ExchangeRateRequest exchangeRates) {
        super(connection);
        this.productMainGroup = productMainGroup;
        this.sbu = sbu;
        this.planPeriod = Period.getPeriodByYear(planYear);
        this.currentPeriod = currentPeriod;
        this.region = region;
        this.salesType = salesType;
        this.exchangeRates = exchangeRates;
        actualAccessor = getManager().createAccessor(ActualSalesAccessor.class);
        forecastAccessor = getManager().createAccessor(ForecastSalesAccessor.class);
    }

    public List<OutputDataType> getSalesKpis() {
        //System.out.println(productMainGroup + " " + region + " " + salesType);
        List<OutputDataType> resultList = new ArrayList<>();

        /* TODO: Prepare Map with months to iterate over and fill with values */
        for (int i = 0; i < Service.getNumberOfMonths(); i++) {
            getSalesKpisForSpecificMonth();
            planPeriod.increment();
        }

        setEntryType();

        resultList.add(createOutputDataType(SALES_VOLUME, entryType, monthlyKpiValues.get(SALES_VOLUME)));
        resultList.add(createOutputDataType(NET_SALES, entryType, monthlyKpiValues.get(NET_SALES)));
        resultList.add(createOutputDataType(CM1, entryType, monthlyKpiValues.get(CM1)));
        resultList.add(createOutputDataType(PRICE, entryType, monthlyKpiValues.get(PRICE)));
        resultList.add(createOutputDataType(VAR_COSTS, entryType, monthlyKpiValues.get(VAR_COSTS)));
        resultList.add(createOutputDataType(CM1_SPECIFIC, entryType, monthlyKpiValues.get(CM1_SPECIFIC)));
        resultList.add(createOutputDataType(CM1_PERCENT, entryType, monthlyKpiValues.get(CM1_PERCENT)));

        return resultList;
    }

    /* TODO: Return data type maybe as HashMap? Could be bad for performance */
    private void getSalesKpisForSpecificMonth() {
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

            if (queryResult.getCurrency().equals(exchangeRates.getToCurrency()) == false) {
                double exchangeRate = exchangeRates.getExchangeRate(planPeriod, queryResult.getCurrency());
                salesVolume *= exchangeRate;
                netSales *= exchangeRate;
                cm1 *= exchangeRate;
            }
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

        monthlyKpiValues.get(SALES_VOLUME).add(salesVolume);
        monthlyKpiValues.get(NET_SALES).add(netSales);
        monthlyKpiValues.get(CM1).add(cm1);
        monthlyKpiValues.get(PRICE).add(price);
        monthlyKpiValues.get(VAR_COSTS).add(varCost);
        monthlyKpiValues.get(CM1_SPECIFIC).add(cm1Specific);
        monthlyKpiValues.get(CM1_PERCENT).add(cm1Percent);
    }

    private SalesEntity getActualData() {
        actualFlag = true;
        SalesEntity queryResult = actualAccessor.getSalesKPIs(productMainGroup, planPeriod.getPeriod(), region,
                salesType.getType(), DataSource.BW_B.toString());
        if (queryResult == null) {
            queryResult = actualAccessor.getSalesKPIs(productMainGroup, planPeriod.getPeriod(), region,
                    salesType.getType(), DataSource.BW_A.toString());
            if (queryResult != null) {
                //TODO: Cover case in entryType, when all the KPIs are actual data except CM1
                queryResult.setCm1(getForecastCm1());
            }
        }

        return queryResult;
    }

    private SalesEntity getForecastData() {
        forecastFlag = true;
        return forecastAccessor.getSalesKPI(productMainGroup, currentPeriod.getPeriod(),
                planPeriod.getPeriod(), region, salesType.toString());
    }

    private double getForecastCm1() {
        forecastFlag = true;
        SalesEntity cm1 = forecastAccessor.getCm1(productMainGroup, currentPeriod.getPeriod(),
                planPeriod.getPeriod(), region, salesType.toString());
        if (cm1 == null) {
            return 0;
        }
        return cm1.getCm1();
    }

    private OutputDataType createOutputDataType(KeyPerformanceIndicators kpi, EntryType entryType, LinkedList<Double> monthlyValues) {
        return new OutputDataType(kpi, sbu, productMainGroup,
                region, region, salesType.toString(), entryType.toString(), exchangeRates.getToCurrency(), monthlyValues);
    }

    private void setEntryType() {
        if (actualFlag && forecastFlag) {
            entryType = EntryType.ACTUAL_FORECAST;
        } else if (forecastFlag) {
            entryType = EntryType.FORECAST;
        } else {
            entryType = EntryType.ACTUAL;
        }
    }

}
