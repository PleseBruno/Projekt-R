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
    private final static int NUM_GENS = 100;

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
            int inputNodes = 7;
            int[] hiddenLayers = {20};
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
                    var results = runner.runGamesInParallel(nns);
                    for (var entry : results.entrySet()) {
                        Generacija.put(entry.getKey(), entry.getValue());
                    }

                    Generacija = GeneticAlgorithms.makeNewGen(Generacija, GeneticType.DEFAULT, j);
                }


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                runner.shutdown();
            }
            
        }else launch();
    }
}
