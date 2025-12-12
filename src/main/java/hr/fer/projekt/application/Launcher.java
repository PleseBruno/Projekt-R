package hr.fer.projekt.application;

import hr.fer.projekt.controllers.HeadlessGameInstance;
import hr.fer.projekt.controllers.ParallelGameRunner;
import hr.fer.projekt.neuronskaMreza.NeuralNetwork;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Launcher extends Application {

    private static final boolean HEADLESS = true;

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

    public static void main(String[] args) {

        if (HEADLESS) {

            ParallelGameRunner runner = new ParallelGameRunner(
                    Runtime.getRuntime().availableProcessors()
            );

            List<NeuralNetwork>  nns = new ArrayList<>();
            try {
                for (int i = 0; i < 50; i++) {
                    NeuralNetwork nn = new NeuralNetwork(
                            "NN_" + i,
                            4,
                            new int[]{10, 10},
                            4
                    );
                    nns.add(nn);
                }



                var results = runner.runGamesInParallel(nns);
                for (var entry : results.entrySet()) {
                    System.out.println("Neural Network: " + entry.getKey().getID() +
                            ", Fitness: " + entry.getValue());
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                runner.shutdown();
            }
            
        }else launch();
    }
}
