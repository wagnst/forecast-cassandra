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

public class JsonDiff {
    public static void main(String[] args) {
        Set<CompareObject> ourSet = getJsonFromFile(new File("resultGorilla.json"));
        Set<CompareObject> theirSet = getJsonFromFile(new File("spJson.json"));

        List<CompareObject> ourList = getSortedList(ourSet);
        List<CompareObject> theirList = getSortedList(theirSet);

        List<CompareObject> noDuplicates = new ArrayList<>();
        System.out.println("Checking if our list contains every object from their list");
        for (CompareObject param : theirList) {
            if (!ourList.contains(param)) {
                if (!noDuplicates.contains(param)) {
                    noDuplicates.add(param);
                }
            }
        }

        /*
        System.out.println("Checking if their list contains every object from our list");
        for (CompareObject param : ourList) {
            if (!theirList.contains(param)) {
                if (!noDuplicates.contains(param)) {
                    noDuplicates.add(param);
                }
            }
        } */


        for (CompareObject object : noDuplicates) {
            System.out.println(object);
        }

        System.out.println("Finished");

        /*
        for (CompareObject param : ourList) {
            System.out.println(param.toString());
        }
         */


        System.out.println("Size of our list: "  + ourList.size());
        System.out.println("Size of their list: "  + theirList.size());

    }

    private static List<CompareObject> getSortedList(Set<CompareObject> set) {
        return set.stream()
                .sorted(new DiffComparator())
                .collect(Collectors.toList());
    }

    private static Set<CompareObject> getJsonFromFile(File json) {
        ObjectMapper om = new ObjectMapper();

        OutputDataType[] spEntries = new OutputDataType[]{};
        try {
            spEntries = om.readValue(json, OutputDataType[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Arrays.stream(spEntries)
                .map(entry -> new CompareObject(entry.getSbu(), entry.getProductMainGroup(), entry.getRegion(),entry.getSubregion(),
                        entry.getSalesType(), entry.getEntryType()))
                .collect(Collectors.toSet());
    }
}
