package hr.fer.projekt.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Launcher extends Application {

    private final boolean graphicsOn = true;

    @Override
    public void start(Stage stage) throws IOException {

        if (graphicsOn) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            scene.getRoot().requestFocus();
            stage.setTitle("Patkica");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }else {
            FXMLLoader fxmlLoaderNoGraph = new FXMLLoader(getClass().getResource("noGraphics.fxml"));
            Scene scene = new Scene(fxmlLoaderNoGraph.load());
            scene.getRoot().requestFocus();
            stage.setTitle("Patkica");
            stage.setScene(scene);
            stage.setResizable(true);
            stage.show();
        }

    }

    public static void main(String[] args) {
            launch();
    }
}
