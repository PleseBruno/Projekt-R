package hr.fer.projekt.neuronskaMreza;

import hr.fer.projekt.temp.dataSet;
import java.lang.Integer;
import java.util.*;

public class Single {
    Map<List<Integer>,List<Integer>> dataSetMap;

    List<double[][]> neuronskaMreza;

    List<Integer> input;
    double[] result;

    public Single() {
        dataSetMap = dataSet.getDataSetMap();
        //TODO: implementirati generiranje random matrica

        //privremeno generiramo random input
        input = generateRandomInput();

        //privremeno generiramo random rezultat
        Random rand = new Random();
        result = new double[] {
                rand.nextDouble(-1.0,1.0),
                rand.nextDouble(-1.0,1.0),
                rand.nextDouble(-1.0,1.0),
                rand.nextDouble(-1.0,1.0)};
    }

    public double[] getResult() {
        return result;
    }

    public List<Integer> getInput() {
        return input;
    }

    public double[][] computeResult() {
        //TODO: racunanje rezultata matricnim mnozenjem
        return null;
    }

    public List<Integer> generateRandomInput() {
        Random rand = new Random();
        int slucajni = rand.nextInt(0,30);

        return dataSetMap.keySet().stream().toList().get(slucajni);
    }

    public Single makeBaby(Single mama, Single tata) {
        //TODO: implementirati pravljenje beba izmedu dvije neuronske mreze
        return null;
    }
}