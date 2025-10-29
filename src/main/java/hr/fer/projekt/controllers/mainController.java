package hr.fer.projekt.controllers;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class mainController implements Initializable {

    AnimationTimer gameLoop;

    @FXML
    private Rectangle player;

    @FXML
    private AnchorPane world;

    double yDelta = 0.02 ;
    double time = 0;
    int jumpHeight = 100;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        load();

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long l) {
                update();
            }
        };
        gameLoop.start();
    }

    @FXML
    void keyPressed(KeyEvent event) {
        if(event.getCode() == KeyCode.SPACE){
            fly();
        }
    }

    private void fly(){
        if (player.getLayoutY() + player.getY() <= jumpHeight) {
            moveBirdY(-(player.getLayoutY() + player.getY()));
            time = 0;
            return;
        }

        moveBirdY(-jumpHeight);
        time = 0;
    }

    //Called every game frame
    private void update() {
        time ++;
        moveBirdY(yDelta * time);

        if(isBirdDead()){
            resetBird();
        }
    }

    //Everything called once, at the game start
    private void load(){
        System.out.println("Game starting");

    }

    private void moveBirdY(double positionChange){
        player.setY(player.getY() + positionChange);
    }

    private boolean isBirdDead(){
        double playerY = player.getLayoutY() + player.getY();
        return playerY >= world.getHeight();
    }

    private void resetBird(){
        player.setY(0);
        time = 0;
    }
}
