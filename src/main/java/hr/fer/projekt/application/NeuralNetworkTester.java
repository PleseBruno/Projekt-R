package hr.fer.projekt.application;

import hr.fer.projekt.genetskiAlgoritam.FitnessChecker;
import hr.fer.projekt.matematika.Matrix;
import hr.fer.projekt.neuronskaMreza.NeuralNetwork;
import hr.fer.projekt.temp.DataSet;

import java.util.*;

public class NeuralNetworkTester {

    public static void main(String[] args) {
        String id;
        int inputNodes;
        int outputNodes;
        int numHiddenLayers;
        int[] hiddenLayers;
        List<Matrix> weights;
        List<Matrix> biases;

        NeuralNetwork neuralNetwork;

        try (Scanner input = new Scanner(System.in)) {
            input.useLocale(Locale.US);

            id = input.nextLine();
            numHiddenLayers = input.nextInt();
            inputNodes = input.nextInt();
            outputNodes = input.nextInt();
            hiddenLayers = new int[numHiddenLayers];
            for (int i = 0; i < numHiddenLayers; i++) {
                hiddenLayers[i] = input.nextInt();
            }
            weights = new ArrayList<Matrix>();
            biases = new ArrayList<Matrix>();

            // Broj slojeva = input + hidden + output
            int prevNodes = inputNodes;
            for (int hiddenNodes : hiddenLayers) {
                weights.add(new Matrix(hiddenNodes, prevNodes));
                biases.add(new Matrix(hiddenNodes, 1));
                prevNodes = hiddenNodes;
            }

            // Zadnji sloj (output)
            weights.add(new Matrix(outputNodes, prevNodes));
            biases.add(new Matrix(outputNodes, 1));

            for (Matrix w : weights) {
                for (int i = 0; i < w.cols * w.rows; i++) {
                    w.data[i/w.cols][i%w.cols] = input.nextDouble();
                }
            };
            for (Matrix b : biases) {
                for (int i = 0; i < b.cols * b.rows; i++) {
                    b.data[i/b.cols][i%b.cols] = input.nextDouble();
                }
            };

            neuralNetwork = new NeuralNetwork(id, inputNodes, hiddenLayers, outputNodes, weights, biases);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Greška prilikom ucitavanja matrice" + e.getMessage());
        }

        NeuralNetworkTester.test(neuralNetwork);
    }

    private static void test(NeuralNetwork neuralNetwork) {
        Map<Set<Integer>, Set<Integer>> dataSetMap = DataSet.getDataSetMap();

        List<Set<Integer>> inputsSet = dataSetMap.keySet().stream().toList();
        List<double[]> inputs = new ArrayList<double[]>();
        List<double[]> outputs = new ArrayList<double[]>();

        Boolean hasMistakes = false;

        for (int i = 0; i < inputsSet.size(); i++) {
            Set<Integer> set = inputsSet.get(i);

            double[] arr = new double[10];

            for (int v : set) {
                arr[v] = 1.0;
            }

            double[] output = neuralNetwork.generateOutput(arr);
            Set<Integer> outputSet = FitnessChecker.outputToSet(output);

            if (!dataSetMap.get(set).equals(outputSet)) {
                System.out.println("Neural network did not generate correct output!");
                System.out.println("Wanted Output: " + dataSetMap.get(set));
                System.out.println("Generated Output: " + outputSet);
                System.out.println();

                hasMistakes = true;
            }

        }

        if (!hasMistakes) {
            System.out.println("Neural network generated correct outputs!");
        }


    }
}
