package fourschlag.jsoncompare;

import com.fasterxml.jackson.databind.ObjectMapper;
import fourschlag.entities.types.Currency;
import fourschlag.entities.types.OutputDataType;
import fourschlag.entities.types.comparators.OutputDataTypeComparator;
import fourschlag.services.data.services.FixedCostsService;
import fourschlag.services.data.services.SalesService;
import fourschlag.services.db.CassandraConnection;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonDiff {
    private static CassandraConnection connection = CassandraConnection.getInstance();
    private static SalesService salesService = new SalesService(connection);
    private static FixedCostsService fixedCostsService = new FixedCostsService(connection);

    public static void main(String[] args) {
        Set<CompareObject> ourSet = getTheirJson(new File("ourJson.json"));
        Set<CompareObject> theirSet = getTheirJson(new File("spJson.json"));

        List<CompareObject> ourList = getSortedList(ourSet);
        List<CompareObject> theirList = getSortedList(theirSet);

        List<CompareObject> noDuplicates = new ArrayList<>();
        System.out.println("Checking if ourList contains every object from theirList");
        for (CompareObject param : theirList) {
            if (!ourList.contains(param)) {
                if (!noDuplicates.contains(param)) {
                    noDuplicates.add(param);
                }
            }
        }

        System.out.println("Checking if theirList contains every object from ourList");
        for (CompareObject param : ourList) {
            if (!theirList.contains(param)) {
                if (!noDuplicates.contains(param)) {
                    noDuplicates.add(param);
                }
            }
        }


        for (CompareObject object : noDuplicates) {
            System.out.println(object);
        }

        System.out.println("Finished");

        /*
        for (CompareObject param : ourList) {
            System.out.println(param.toString());
        }
         */


        System.out.println(ourList.size());
        System.out.println(theirList.size());

    }

    static List<CompareObject> getSortedList(Set<CompareObject> set) {
        return set.stream()
                .sorted(new DiffComparator())
                .collect(Collectors.toList());
    }

    static Set<CompareObject> getTheirJson(File json) {
        ObjectMapper om = new ObjectMapper();

        File spJsonFile = json;

        OutputDataType[] spEntries = null;
        try {
            spEntries = om.readValue(spJsonFile, OutputDataType[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Arrays.stream(spEntries)
                .map(entry -> new CompareObject(entry.getSbu(), entry.getProductMainGroup(), entry.getRegion(),entry.getSubregion(),
                        entry.getSalesType(), entry.getEntryType()))
                .collect(Collectors.toSet());
    }

    /* Only for Team FourSchlag. If you are not Team FourSchlag, then use getTheirJson two times */
    static Set<CompareObject> getOurJson() {
        int planYear = 2016;
        int period = 201609;
        Currency curr = Currency.getCurrencyByAbbreviation("usd");

        /* Get all Sales KPIs and save them to a stream */
        Stream<OutputDataType> salesKpis = salesService.getSalesKPIs(planYear, period, curr);
        /* Also save the Fixed Costs KPIs to a stream */
        Stream<OutputDataType> fixedCostsKpis = fixedCostsService.getFixedCostsKpis(planYear, period, curr);

        /* Combine both streams to one */
        Set<CompareObject> resultSet = Stream.concat(salesKpis, fixedCostsKpis)
                /* Sort the whole stream */
                .sorted(new OutputDataTypeComparator())
                /* Convert the stream to a List */
                .map(entry -> new CompareObject(entry.getSbu(), entry.getProductMainGroup(), entry.getRegion(),entry.getSubregion(),
                entry.getSalesType(), entry.getEntryType()))
                .collect(Collectors.toSet());

        /* Close both streams */
        salesKpis.close();
        fixedCostsKpis.close();

        return resultSet;
    }
}
