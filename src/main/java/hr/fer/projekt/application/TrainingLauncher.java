package hr.fer.projekt.application;

import hr.fer.projekt.genetskiAlgoritam.FitnessChecker;
import hr.fer.projekt.genetskiAlgoritam.GeneticAlgorithms;
import hr.fer.projekt.genetskiAlgoritam.GeneticType;
import hr.fer.projekt.neuronskaMreza.NeuralNetwork;
import hr.fer.projekt.temp.DataSet;

import java.util.*;

public class TrainingLauncher {
    private final static int NUM_TESTS = 30;
    private final static int NUM_NEURALNETWORKS = 50;
    private final static int NUM_GENS = 500;
    private final static double ALPHA = 0.2;
    private final static double CROSS_CHANCE = 0.1;
    private final static double MUTATION_CHANCE = 0.05;

    public static void main(String[] args) {
        int inputNodes = 10;
        int[] hiddenLayers = {20};
        int outputNodes = 4;
        Map<Set<Integer>, Set<Integer>> dataSetMap = DataSet.getDataSetMap();

        List<Set<Integer>> inputsSet = dataSetMap.keySet().stream().toList();
        List<double[]> inputs = new ArrayList<>();
        List<double[]> outputs = new ArrayList<>();

        Map<NeuralNetwork, Double> Generacija = new HashMap<>();

        for (int i = 0; i < NUM_NEURALNETWORKS; i++) {
            Generacija.put(new NeuralNetwork("NN-1." + i+1, inputNodes, hiddenLayers, outputNodes), null);
        }

        for (int j = 0; j < NUM_GENS; j++) {
            System.out.println("Generation: " + j);

            for (NeuralNetwork n : Generacija.keySet()) {

                for (Set<Integer> set : inputsSet) {
                    double[] arr = new double[10];

                    for (int v : set) {
                        arr[v] = 1.0;
                    }

                    inputs.add(arr);
                }

                //System.out.println("Matrix: " + n.getID());
                double totalFitness = 0;

                for (double[] arr : inputs) {
                    double[] output = n.generateOutput(arr);
                    outputs.add(output);

                    //racunanje fitnesa pomocu fitnessCheckera
                    double fitness = FitnessChecker.rateDataSet(arr, output);
                    totalFitness += fitness;

                }
                inputs.clear();
                totalFitness /= NUM_TESTS;
                Generacija.merge(n, totalFitness, (ov, nv) -> nv);
            }
            Generacija = GeneticAlgorithms.makeNewGen(Generacija, GeneticType.DEFAULT, j, ALPHA,  CROSS_CHANCE, MUTATION_CHANCE);
        }
    }
}
