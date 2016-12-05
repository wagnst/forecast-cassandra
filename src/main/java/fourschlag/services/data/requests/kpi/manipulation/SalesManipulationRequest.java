package fourschlag.services.data.requests.kpi.manipulation;

import fourschlag.entities.accessors.sales.ForecastSalesAccessor;
import fourschlag.services.data.requests.Request;
import fourschlag.services.db.CassandraConnection;

public class SalesManipulationRequest extends Request {

    private final ForecastSalesAccessor forecastAccessor;

    public SalesManipulationRequest(CassandraConnection connection) {
        super(connection);
        forecastAccessor = getManager().createAccessor(ForecastSalesAccessor.class);
    }

    public boolean setForecastSales(double topdownAdjustSalesVolumes, double topdownAdjustNetSales, double topdownAdjustCm1, int planPeriod, int planYear, int planHalfYear,
                                    int planQuarter, int planMonth, String entryType, String status, String usercomment, String productMainGroup, String salesType,
                                    double salesVolumes, double netSales, double cm1, int period, String region,
                                    int periodYear, int periodMonth, String currency, String userId, String entryTs) {

        try {
            if (forecastAccessor.getForecastSales(productMainGroup, region, period, salesType, planPeriod, entryType) != null) {
                // update an existing record
                forecastAccessor.updateForecastSales(topdownAdjustSalesVolumes, topdownAdjustNetSales, topdownAdjustCm1, planPeriod, planYear, planHalfYear, planQuarter,
                        planMonth, entryType, status, usercomment, productMainGroup, salesType, salesVolumes, netSales, cm1, period, region, periodYear, periodMonth, currency, userId, entryTs);
            } else {
                forecastAccessor.setForecastSales(topdownAdjustSalesVolumes, topdownAdjustNetSales, topdownAdjustCm1, planPeriod, planYear, planHalfYear, planQuarter,
                        planMonth, entryType, status, usercomment, productMainGroup, salesType, salesVolumes, netSales, cm1, period, region, periodYear, periodMonth, currency, userId, entryTs);
            }
        } catch (Exception e) {
            //TODO: implement better exception to be catched
            return false;
        }

        return true;

    }

    //TODO: implement method for non-forecast related tables
}
