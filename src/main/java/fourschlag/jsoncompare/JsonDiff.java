package fourschlag.jsoncompare;

import com.fasterxml.jackson.databind.ObjectMapper;
import fourschlag.entities.types.OutputDataType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonDiff {
    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException();
        }
        /* Get both JSON Files and put all entries into a Set to remove all duplicates */
        Stream<OutputDataType> ourJson;
        Stream<OutputDataType> spJson;
        try {
            ourJson = getJsonFromFile(new File("resultJson.json"));
            spJson = getJsonFromFile(new File("spJson.json"));
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("\nError when trying to map the JSON");
            return;
        }

        Set<CompareObject> ourSet;
        Set<CompareObject> spSet;
        if (args[0].equals("true")) {
            ourSet = asSetWithKpi(ourJson);
            spSet = asSetWithKpi(spJson);
        } else {
            ourSet = asSetWithoutKpi(ourJson);
            spSet = asSetWithoutKpi(spJson);
        }

        /* Sort the collection to make it easier to read later */
        List<CompareObject> ourList = getSortedList(ourSet);
        List<CompareObject> spList = getSortedList(spSet);

        List<CompareObject> noDuplicates = new ArrayList<>();

        if (args[1].equals("true")) {
            /* For each object in their List check
             * IF our List does not contain it
             * THEN add it to our result list
             */
            for (CompareObject param : spList) {
                if (!ourList.contains(param)) {
                    noDuplicates.add(param);
                }
            }
        } else {
            /* For each object in our List check
             * IF their List does not contain it
             * THEN add it to our result list
             */
            for (CompareObject param : ourList) {
                if (!spList.contains(param)) {
                    noDuplicates.add(param);
                }
            }
        }


        if (noDuplicates.isEmpty()) {
            System.out.println("Your JSON contains all the combinations that are part of the SP JSON!");
        } else {
            /* Print out the result list */
            System.out.println("The following entries are part of the SP Json, but are missing in your Json:\n");
            for (CompareObject object : noDuplicates) {
                System.out.println(object);
            }
        }
        /* --SIMPLE OUTPUT--
        if (noDuplicates.isEmpty()) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
        */

        System.out.println("\n\nFinished\n");


        System.out.println("Number of entries/combinations in your Json (without duplicates): " + ourList.size());
        System.out.println("Number of entries/combinations in SP Json (without duplicates): " + spList.size());

    }

    private static List<CompareObject> getSortedList(Set<CompareObject> set) {
        return set.stream()
                .sorted(new DiffComparator())
                .collect(Collectors.toList());
    }

    private static Stream<OutputDataType> getJsonFromFile(File json) throws IOException {
        ObjectMapper om = new ObjectMapper();
        OutputDataType[] spEntries = om.readValue(json, OutputDataType[].class);


        return Arrays.stream(spEntries);
    }

    private static Set<CompareObject> asSetWithoutKpi(Stream<OutputDataType> stream) {
        return stream
                .map(entry -> new CompareObject(entry.getSbu(), entry.getProductMainGroup(), entry.getRegion(), entry.getSubregion(),
                        entry.getSalesType(), entry.getEntryType()))
                .collect(Collectors.toSet());
    }

    private static Set<CompareObject> asSetWithKpi(Stream<OutputDataType> stream) {
        return stream
                .map(entry -> new CompareObjectWithKpi(entry.getSbu(), entry.getProductMainGroup(), entry.getRegion(), entry.getSubregion(),
                        entry.getSalesType(), entry.getEntryType(), entry.getKpi()))
                .collect(Collectors.toSet());
    }

}
