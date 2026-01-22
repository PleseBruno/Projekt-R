package hr.fer.projekt.application;

import hr.fer.projekt.controllers.ParallelGameRunner;
import hr.fer.projekt.genetskiAlgoritam.GeneticAlgorithms;
import hr.fer.projekt.genetskiAlgoritam.GeneticType;
import hr.fer.projekt.matematika.Statistics;
import hr.fer.projekt.neuronskaMreza.NeuralNetwork;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class Launcher extends Application {

    private final static boolean HEADLESS = true;
    private final static boolean CONTINUE_LEARNING = false;
    private final static int NUM_GENS = 4000;
    private final static int NUM_AVG_FITNESS = 100;

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("startScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        scene.getStylesheets().add(getClass().getResource("button.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Patkica");
        stage.setResizable(false);
        stage.show();

    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        if (HEADLESS) {
            int numNeuralNetworks = 50;
            int testsPerNetwork = 40;
            double alpha = 0.2;
            double crossChance = 0.08;
            double mutationChance = 0.02;

            LinkedList<Double> lastAvgFitnessesList = new LinkedList<>();
            double oldMean = 0.0, newMean;
            double stdDeviation;

            boolean growth_era = true;

            int inputNodes = 12;
            int[] hiddenLayers = {100, 200};
            int outputNodes = 4;

            ParallelGameRunner runner = new ParallelGameRunner(
                    Runtime.getRuntime().availableProcessors()
            );

            try {

                Map<NeuralNetwork, Double> Generacija = new HashMap<>();

                if (CONTINUE_LEARNING) {
                    NeuralNetwork n = NeuralNetwork.loadNeuralNetworkFromFile("best_network.txt");
                    inputNodes = n.getInputNodes();
                    hiddenLayers = n.getHiddenLayers();
                    outputNodes = n.getOutputNodes();
                    Generacija.put(n, null);
                    for (int i = 0; i < numNeuralNetworks - 1; i++) {
                        Generacija.put(new NeuralNetwork("NN-1." + i + 1, inputNodes, hiddenLayers, outputNodes), null);
                    }
                } else {
                    for (int i = 0; i < numNeuralNetworks; i++) {
                        Generacija.put(new NeuralNetwork("NN-1." + i + 1, inputNodes, hiddenLayers, outputNodes), null);
                    }
                }


                // Track best network found across all generations
                NeuralNetwork bestNetwork = null;
                double bestFitness = Double.NEGATIVE_INFINITY;

                for (int j = 0; j < NUM_GENS; j++) {

                    System.out.println("Generation: " + j);
                    List<NeuralNetwork>  nns = new ArrayList<>(Generacija.keySet());

                    Random random = new Random(System.nanoTime());
                    List<Long> seeds = new ArrayList<>();

                    for (int k = 0; k < testsPerNetwork; k++) {
                        seeds.add(random.nextLong());
                    }

                    // Use a temporary accumulator so we don't modify the population while testing
                    Map<NeuralNetwork, Double> accumulator = new HashMap<>();
                    for (NeuralNetwork nn : nns) accumulator.put(nn, 0.0);

                    for (int l = 0; l < testsPerNetwork; l++) {
                        Long seed = seeds.get(l);
                        var results = runner.runGamesInParallel(nns, seed);
                        for (var entry : results.entrySet()) {
                            accumulator.put(entry.getKey(), accumulator.getOrDefault(entry.getKey(), 0.0) + entry.getValue());
                        }
                    }

                    // Compute averages into a new map (population remains the same during tests)
                    Map<NeuralNetwork, Double> averaged = new HashMap<>();
                    for (NeuralNetwork nn : nns) {
                        double sum = accumulator.getOrDefault(nn, 0.0);
                        double avg = sum / testsPerNetwork;
                        averaged.put(nn, avg);
                        if (avg > bestFitness) {
                            bestFitness = avg;
                            bestNetwork = nn;
                        }
                    }
                    double totalFitnessThisGenAverage = averaged.values().stream().mapToDouble(Double::doubleValue).sum() / averaged.size();
                    System.out.printf("  Average fitness this generation: %.4f%n", totalFitnessThisGenAverage);

                    if (lastAvgFitnessesList.size() < NUM_AVG_FITNESS) {
                        lastAvgFitnessesList.add(totalFitnessThisGenAverage);
                    } else {
                        lastAvgFitnessesList.add(totalFitnessThisGenAverage);
                        lastAvgFitnessesList.removeLast();

                        newMean = lastAvgFitnessesList
                                            .stream()
                                            .mapToDouble(Double::doubleValue)
                                            .average()
                                            .orElseThrow(() -> new IllegalStateException("No average fitness found"));

//                        if (newMean > oldMean) {
//                            oldMean = newMean;
//
//                            growth_era = true;
//                        } else {
//                            stdDeviation = Statistics.getStdDev(lastAvgFitnessesList);
//
//                            if (stdDeviation > 1200) {
//                                growth_era = true;
//                            } else  {
//                                growth_era = false;
//                            }
//                        }
                    }

                    if (growth_era) {
                        alpha = 0.2;
                        crossChance = 0.08;
                        mutationChance = 0.02;
                    } else {
                        alpha = 0.25;
                        crossChance = 0.1;
                        mutationChance = 0.07;
                    }

                    if (j == 1000) {
                        numNeuralNetworks = 30;
                        testsPerNetwork = 20;
                    }

                    Generacija = GeneticAlgorithms.makeNewGen(averaged, GeneticType.DEFAULT, j, numNeuralNetworks ,alpha, crossChance, mutationChance);

                    if (j % 500 == 0 && j > 1000) {
                        Path fileName = Path.of("lastSavedNetwok.txt");

                        try {
                            Files.writeString(fileName, bestNetwork.toString());
                            System.out.println("Saved network: " + bestNetwork.toString());
                        }
                        catch (IOException e) {
                            System.err.println("Neuronska mreža neuspješno spremljena.");
                        }
                    }
                }

                // Print best network to stdout at the end
                if (bestNetwork != null) {
                    System.out.println("\n=== BEST NETWORK ===");
                    System.out.println("ID: " + bestNetwork.getID() + " fitness=" + bestFitness);
                    System.out.println(bestNetwork.toString());

                    Path fileName = Path.of("best_network.txt");

                    try {
                        Files.writeString(fileName, bestNetwork.toString());
                    }
                    catch (IOException e) {
                        System.err.println("Neuronska mreža neuspješno učitana.");
                    }


                } else {
                    System.out.println("No best network found.");
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                runner.shutdown();
            }

        } else launch();
    }
}