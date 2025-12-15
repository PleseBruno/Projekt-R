package hr.fer.projekt.application;

import hr.fer.projekt.genetskiAlgoritam.GeneticAlgorithms;
import hr.fer.projekt.genetskiAlgoritam.GeneticType;
import hr.fer.projekt.neuronskaMreza.NeuralNetwork;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class IrisTraining {

    private static final int NUM_NEURALNETWORKS = 50;
    private static final int NUM_GENS = 500;
    private final static double ALPHA = 0.2;
    private final static double CROSS_CHANCE = 0.1;
    private final static double MUTATION_CHANCE = 0.05;


    private static class IrisSample {
        final double[] input;
        final double[] target;

        IrisSample(double[] input, double[] target) {
            this.input = input;
            this.target = target;
        }
    }

    public static void main(String[] args) {

        List<IrisSample> dataset;
        try {
            dataset = loadIrisDataset("Iris.csv");
        } catch (IOException e) {
            System.err.println("Ne mogu učitati Iris.csv: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        int inputNodes = 4;
        int[] hiddenLayers = {20};
        int outputNodes = 3;

        Map<NeuralNetwork, Double> generation = new HashMap<>();

        for (int i = 0; i < NUM_NEURALNETWORKS; i++) {
            generation.put(new NeuralNetwork("IRIS-1." + (i + 1),
                    inputNodes, hiddenLayers, outputNodes), null);
        }

        // globalno najbolja mreža kroz sve generacije
        Map.Entry<NeuralNetwork, Double> bestOverall = null;

        for (int gen = 0; gen < NUM_GENS; gen++) {
            System.out.println("Generation: " + gen);

            // izračunaj fitness za ovu generaciju
            for (NeuralNetwork nn : generation.keySet()) {
                double correct = 0.0;
                for (IrisSample s : dataset) {
                    double[] out = nn.generateOutput(s.input);
                    int predictedClass = argMax(out);
                    int expectedClass  = argMax(s.target);
                    if (predictedClass == expectedClass) {
                        correct += 1.0;
                    }
                }
                double accuracy = correct / dataset.size(); // [0,1]
                generation.put(nn, accuracy);
            }

            // pronađi najbolju mrežu u ovoj generaciji
            Map.Entry<NeuralNetwork, Double> best = generation.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .orElse(null);

            if (best != null) {
                System.out.printf("  Best accuracy: %.4f%n", best.getValue());

                // ažuriraj globalno najbolju ako je ova generacija bolja
                if (bestOverall == null || best.getValue() > bestOverall.getValue()) {
                    bestOverall = best;
                }
            }

            // napravi novu generaciju (s null fitnessima)
            generation = GeneticAlgorithms.makeNewGen(generation, GeneticType.DEFAULT, gen, ALPHA, CROSS_CHANCE,  MUTATION_CHANCE);
        }

        if (bestOverall != null) {
            System.out.println("=== FINAL BEST NETWORK ===");
            System.out.println("ID: " + bestOverall.getKey().getID());
            System.out.printf("Accuracy: %.4f%n", bestOverall.getValue());
        }
    }

    private static List<IrisSample> loadIrisDataset(String filename) throws IOException {
        List<IrisSample> samples = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Path.of(filename))) {
            String line = br.readLine(); // preskoči header

            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] parts = line.split(",");
                if (parts.length < 6) continue;

                double sepalLen = Double.parseDouble(parts[1]);
                double sepalWid = Double.parseDouble(parts[2]);
                double petalLen = Double.parseDouble(parts[3]);
                double petalWid = Double.parseDouble(parts[4]);
                String species  = parts[5].trim();

                double[] input = { sepalLen, sepalWid, petalLen, petalWid };

                double[] target = switch (species) {
                    case "Iris-setosa"     -> new double[]{1.0, 0.0, 0.0};
                    case "Iris-versicolor" -> new double[]{0.0, 1.0, 0.0};
                    case "Iris-virginica"  -> new double[]{0.0, 0.0, 1.0};
                    default -> throw new IllegalArgumentException("Nepoznata klasa: " + species);
                };

                samples.add(new IrisSample(input, target));
            }
        }

        return samples;
    }

    private static int argMax(double[] arr) {
        int idx = 0;
        double max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
                idx = i;
            }
        }
        return idx;
    }
}
