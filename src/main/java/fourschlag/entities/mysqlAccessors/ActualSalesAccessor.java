package fourschlag.entities.mysqlAccessors;

import fourschlag.entities.tables.kpi.sales.ActualSalesEntity;
import fourschlag.entities.types.mysql.QueryStructure;
import fourschlag.services.DataService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActualSalesAccessor implements DataService{
    private static final String TABLE_NAME = "actual_sales";

    public ActualSalesEntity getSalesKPIs(
            String productMainGroup,
            int period,
            String region,
            String salesType,
            String dataSource) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("product_main_group", productMainGroup);
        parameters.put("period", period);
        parameters.put("region", region);
        parameters.put("sales_type", salesType);
        parameters.put("data_source", dataSource);

        QueryStructure qs = new QueryStructure(TABLE_NAME, "product_main_group = :product_main_group AND period = :period AND region = :region AND sales_type = :sales_type AND data_source = :data_source");

        List<ActualSalesEntity> list = selectQuery(qs, parameters);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public static void main(String[] args) {
        ActualSalesEntity entity = new ActualSalesAccessor().getSalesKPIs(
                "Eichbaum Radler Zitrone",
                201301,
                "South America",
                "3rd_party",
                "BW B");

        System.out.println(entity);
    }
}
