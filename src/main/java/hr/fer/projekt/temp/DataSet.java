package hr.fer.projekt.temp;

import java.util.HashMap;
import java.util.Set;
import java.util.Map;


public class DataSet {

    /**
     * funkcija koja generira mapu sa zadanim outputima {1, 2, 3, 4} za inpute {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}
     * @return dataSetMap
     */
    public static Map<Set<Integer>, Set<Integer>> getDataSetMap() {
        Map<Set<Integer>, Set<Integer>> dataSetMap = new HashMap<Set<Integer>, Set<Integer>>();

        dataSetMap.put(Set.of(1, 2, 3), Set.of(1));
        dataSetMap.put(Set.of(4, 5, 6), Set.of(1, 2, 3));
        dataSetMap.put(Set.of(7, 8, 9), Set.of(2, 3));
        dataSetMap.put(Set.of(0), Set.of(4));
        dataSetMap.put(Set.of(1, 4, 6, 7), Set.of(4, 1));
        dataSetMap.put(Set.of(1, 5, 6), Set.of());
        dataSetMap.put(Set.of(4, 6), Set.of(2, 4));
        dataSetMap.put(Set.of(0), Set.of(1, 3));
        dataSetMap.put(Set.of(2, 7), Set.of(1, 2, 3));
        dataSetMap.put(Set.of(1), Set.of(1));
        dataSetMap.put(Set.of(2), Set.of(2));
        dataSetMap.put(Set.of(3), Set.of(3));
        dataSetMap.put(Set.of(4), Set.of(4));
        dataSetMap.put(Set.of(), Set.of(1, 2, 3, 4));
        dataSetMap.put(Set.of(5), Set.of(1, 4));
        dataSetMap.put(Set.of(6), Set.of(1, 2, 3));
        dataSetMap.put(Set.of(7), Set.of(2, 3, 4));
        dataSetMap.put(Set.of(8), Set.of(1));
        dataSetMap.put(Set.of(9), Set.of(4));
        dataSetMap.put(Set.of(1, 2), Set.of(1, 2));
        dataSetMap.put(Set.of(1, 3), Set.of());
        dataSetMap.put(Set.of(1, 4), Set.of(1));
        dataSetMap.put(Set.of(1, 5), Set.of());
        dataSetMap.put(Set.of(2, 6), Set.of(1, 4));
        dataSetMap.put(Set.of(2, 4, 5), Set.of(1, 4));
        dataSetMap.put(Set.of(4, 5), Set.of());
        dataSetMap.put(Set.of(8, 9), Set.of(1, 2, 3));
        dataSetMap.put(Set.of(2, 3), Set.of(1));
        dataSetMap.put(Set.of(6, 9), Set.of(1, 2, 3));
        dataSetMap.put(Set.of(7, 9), Set.of(1, 2, 3));
        return dataSetMap;
    }
}
