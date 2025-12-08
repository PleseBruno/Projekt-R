package hr.fer.projekt.genetskiAlgoritam;

import hr.fer.projekt.temp.DataSet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FitnessChecker {

    public static double rateDataSet(double[] input, double[] output) {
        Map<Set<Integer>, Set<Integer>> dataSetMap = DataSet.getDataSetMap();

        Set<Integer> inputSet = inputToSet(input);
        Set<Integer> outputSet = outputToSet(output);

        Set<Integer> wantedOutputSet = dataSetMap.get(inputSet);

        double score = 0.0;

        if (outputSet.size() == wantedOutputSet.size()) {
            score += 1000;
        }

        if (wantedOutputSet.isEmpty() && outputSet.isEmpty() || wantedOutputSet.equals(outputSet)) {
            score += 500;
        } else {
            for (Integer realResultInteger : wantedOutputSet) {
                if (outputSet.contains(realResultInteger)) {
                    score += 100;
                }
            }
        }


        return score;
    }

    public static Set<Integer> inputToSet(double[] input) {
        Set<Integer> set = new HashSet<>();

        for (int i = 0; i < input.length; i++) {
            if (input[i] == 1.0) {
                set.add(i);
            }
        }

        return set;
    }

    public static Set<Integer> outputToSet(double[] output) {
        Set<Integer> set = new HashSet<>();

        for (int i = 0; i < output.length; i++) {
            if (output[i] >= 0.5) {
                set.add(i+1);
            }
        }

        return set;
    }
}
