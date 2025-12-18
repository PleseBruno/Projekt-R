package hr.fer.projekt.application;

import hr.fer.projekt.controllers.HeadlessGameInstance;
import hr.fer.projekt.controllers.ParallelGameRunner;
import hr.fer.projekt.genetskiAlgoritam.FitnessChecker;
import hr.fer.projekt.genetskiAlgoritam.GeneticAlgorithms;
import hr.fer.projekt.genetskiAlgoritam.GeneticType;
import hr.fer.projekt.neuronskaMreza.NeuralNetwork;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class Launcher extends Application {

    private static final boolean HEADLESS = false;
    private final static int NUM_NEURALNETWORKS = 50;
    private final static int NUM_GENS = 1000;
    private final static int TESTS_PER_NETWORK = 50;
    private final static double ALPHA = 0.2;
    private final static double CROSS_CHANCE = 0.08;
    private final static double MUTATION_CHANCE = 0.02;

    @Override
    public void start(Stage stage) throws IOException {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            scene.getRoot().requestFocus();
            stage.setTitle("Patkica");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        if (HEADLESS) {
            int inputNodes = 12;
            int[] hiddenLayers = {10, 20};
            int outputNodes = 4;

            ParallelGameRunner runner = new ParallelGameRunner(
                    Runtime.getRuntime().availableProcessors()
            );

            try {

                Map<NeuralNetwork, Double> Generacija = new HashMap<>();

                for (int i = 0; i < NUM_NEURALNETWORKS; i++) {
                    Generacija.put(new NeuralNetwork("NN-1." + i + 1, inputNodes, hiddenLayers, outputNodes), null);
                }

                // Track best network found across all generations
                NeuralNetwork bestNetwork = null;
                double bestFitness = Double.NEGATIVE_INFINITY;

                for (int j = 0; j < NUM_GENS; j++) {
                    System.out.println("Generation: " + j);
                    List<NeuralNetwork>  nns = new ArrayList<>(Generacija.keySet());

                    Random random = new Random(System.nanoTime());
                    List<Long> seeds = new ArrayList<>();

                    for (int i = 0; i < TESTS_PER_NETWORK; i++) {
                        seeds.add(random.nextLong());
                    }

                    // Use a temporary accumulator so we don't modify the population while testing
                    Map<NeuralNetwork, Double> accumulator = new HashMap<>();
                    for (NeuralNetwork nn : nns) accumulator.put(nn, 0.0);

                    for (int k = 0; k < TESTS_PER_NETWORK; k++) {
                        Long seed = seeds.get(k);
                        var results = runner.runGamesInParallel(nns, seed);
                        for (var entry : results.entrySet()) {
                            accumulator.put(entry.getKey(), accumulator.getOrDefault(entry.getKey(), 0.0) + entry.getValue());
                        }
                    }

                    // Compute averages into a new map (population remains the same during tests)
                    Map<NeuralNetwork, Double> averaged = new HashMap<>();
                    for (NeuralNetwork nn : nns) {
                        double sum = accumulator.getOrDefault(nn, 0.0);
                        double avg = sum / TESTS_PER_NETWORK;
                        averaged.put(nn, avg);
                        if (avg > bestFitness) {
                            bestFitness = avg;
                            bestNetwork = nn;
                        }
                    }
                    double totalFitnessThisGenAverage = averaged.values().stream().mapToDouble(Double::doubleValue).sum() / averaged.size();
                    Generacija = GeneticAlgorithms.makeNewGen(averaged, GeneticType.DEFAULT, j, ALPHA, CROSS_CHANCE, MUTATION_CHANCE);
                    System.out.printf("  Average fitness this generation: %.4f%n", totalFitnessThisGenAverage);
                }

                // Print best network to stdout at the end
                if (bestNetwork != null) {
                    System.out.println("\n=== BEST NETWORK ===");
                    System.out.println("ID: " + bestNetwork.getID() + " fitness=" + bestFitness);
                    System.out.println(bestNetwork.toString());
                } else {
                    System.out.println("No best network found.");
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                runner.shutdown();
            }
            
        }else launch();
    }
}
