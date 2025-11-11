package hr.fer.projekt.temp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DataSet {

    public static Map<List<Integer>, List<Integer>> getDataSetMap() {
        Map<List<Integer>, List<Integer>> dataSetMap = new HashMap<List<Integer>, List<Integer>>();

        dataSetMap.put(List.of(1, 2, 3), List.of(1));
        dataSetMap.put(List.of(4, 5, 6), List.of(1, 2, 3));
        dataSetMap.put(List.of(7, 8, 9), List.of(2, 3));
        dataSetMap.put(List.of(0), List.of(4));
        dataSetMap.put(List.of(1, 4, 6, 7), List.of(4, 1));
        dataSetMap.put(List.of(1, 5, 6), List.of());
        dataSetMap.put(List.of(4, 6), List.of(2, 4));
        dataSetMap.put(List.of(0), List.of(1, 3));
        dataSetMap.put(List.of(2, 7), List.of(1, 2, 3));
        dataSetMap.put(List.of(1), List.of(1));
        dataSetMap.put(List.of(2), List.of(2));
        dataSetMap.put(List.of(3), List.of(3));
        dataSetMap.put(List.of(4), List.of(4));
        dataSetMap.put(List.of(), List.of(1, 2, 3, 4));
        dataSetMap.put(List.of(5), List.of(1, 4));
        dataSetMap.put(List.of(6), List.of(1, 2, 3));
        dataSetMap.put(List.of(7), List.of(2, 3, 4));
        dataSetMap.put(List.of(8), List.of(1));
        dataSetMap.put(List.of(9), List.of(4));
        dataSetMap.put(List.of(1, 2), List.of(1, 2));
        dataSetMap.put(List.of(1, 3), List.of());
        dataSetMap.put(List.of(1, 4), List.of(1));
        dataSetMap.put(List.of(1, 5), List.of());
        dataSetMap.put(List.of(2, 6), List.of(1, 4));
        dataSetMap.put(List.of(2, 4, 5), List.of(1, 4));
        dataSetMap.put(List.of(4, 5), List.of());
        dataSetMap.put(List.of(8, 9), List.of(1, 2, 3));
        dataSetMap.put(List.of(2, 3), List.of(1));
        dataSetMap.put(List.of(6, 9), List.of(1, 2, 3));
        dataSetMap.put(List.of(7, 9), List.of(1, 2, 3));
        return dataSetMap;
    }
}
