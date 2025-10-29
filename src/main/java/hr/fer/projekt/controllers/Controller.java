package hr.fer.projekt.controllers;

import javafx.beans.binding.Bindings;
import javafx.fxml.Initializable;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private BooleanProperty aPressed = new SimpleBooleanProperty();
    private BooleanProperty dPressed = new SimpleBooleanProperty();
    private BooleanProperty sPressed = new SimpleBooleanProperty();
    private BooleanProperty wPressed = new SimpleBooleanProperty();

    private BooleanBinding keyPressed = wPressed.or(aPressed).or(sPressed).or(dPressed);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        movementSetup();

        keyPressed.addListener((observableValue, aBoolean, t1) -> {
            movementSetup();
            scene.requestFocus();
        });
    }

    @FXML
    private AnchorPane scene;

    public void movementSetup() {
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case KeyCode.W:
                    wPressed.set(true);
                    break;
                case KeyCode.A:
                    aPressed.set(true);
                    break;
                case KeyCode.S:
                    sPressed.set(true);
                    break;
                case KeyCode.D:
                    dPressed.set(true);
                    break;
            }
        });
        scene.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case KeyCode.W:
                    wPressed.set(false);
                    break;
                case KeyCode.A:
                    aPressed.set(false);
                    break;
                case KeyCode.S:
                    sPressed.set(false);
                    break;
                case KeyCode.D:
                    dPressed.set(false);
                    break;
            }
        });
    }

    public boolean isUp() {
        return wPressed.get() && !sPressed.get();
    }

    public boolean isDown() {
        return sPressed.get() && !wPressed.get();
    }

    public boolean isLeft() {
        return aPressed.get();
    }

    public boolean isRight() {
        return dPressed.get();
    }

}
