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

    private static final boolean HEADLESS = true;
    private final static int NUM_NEURALNETWORKS = 50;
    private final static int NUM_GENS = 10000;
    private final static int TESTS_PER_NETWORK = 30;

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
            int inputNodes = 9;
            int[] hiddenLayers = {10,15, 10};
            int outputNodes = 4;

            ParallelGameRunner runner = new ParallelGameRunner(
                    Runtime.getRuntime().availableProcessors()
            );


            try {

                Map<NeuralNetwork, Double> Generacija = new HashMap<>();

                for (int i = 0; i < NUM_NEURALNETWORKS; i++) {
                    Generacija.put(new NeuralNetwork("NN-1." + i + 1, inputNodes, hiddenLayers, outputNodes), null);
                }

                for (int j = 0; j < NUM_GENS; j++) {
                    System.out.println("Generation: " + j);
                    List<NeuralNetwork>  nns = new ArrayList<>(Generacija.keySet());

                        // Use a temporary accumulator so we don't modify the population while testing
                        Map<NeuralNetwork, Double> accumulator = new HashMap<>();
                        for (NeuralNetwork nn : nns) accumulator.put(nn, 0.0);

                        for (int k = 0; k < TESTS_PER_NETWORK; k++) {
                            var results = runner.runGamesInParallel(nns);
                            for (var entry : results.entrySet()) {
                                accumulator.put(entry.getKey(), accumulator.getOrDefault(entry.getKey(), 0.0) + entry.getValue());
                            }
                        }

                        // Compute averages into a new map (population remains the same during tests)
                        Map<NeuralNetwork, Double> averaged = new HashMap<>();
                        for (NeuralNetwork nn : nns) {
                            double sum = accumulator.getOrDefault(nn, 0.0);
                            averaged.put(nn, sum / TESTS_PER_NETWORK);
                        }

                        Generacija = GeneticAlgorithms.makeNewGen(averaged, GeneticType.DEFAULT, j);
                }


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                runner.shutdown();
            }
            
        }else launch();
    }
}
