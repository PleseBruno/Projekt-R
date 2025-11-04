package hr.fer.projekt.controllers;

import hr.fer.projekt.application.World;
import hr.fer.projekt.entities.Obstacle;
import hr.fer.projekt.temp.KeyPress;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class mainController implements Initializable {

    public Boolean
            aPressed = false, dPressed = false,
            sPressed = false, wPressed = false;

    AnimationTimer gameLoop;

    @FXML
    private Rectangle player;

    @FXML
    private AnchorPane stage;
    
    private World world;
    
    double yDelta = 0.02 ;
    double time = 0;
    int jumpHeight = 100;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        stage.requestFocus();

        world = new World();

        player.setLayoutX(world.getPlayer().getX());
        player.setLayoutY(world.getPlayer().getY());

        for (Obstacle obstacle : world.getObstacles()) {
            stage.getChildren().add(obstacle.getShape());
        }

        load();

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long l) {

                update();
                step();

//                System.out.println(world.getObstacles().getFirst().getHeight() + " "  + world.getObstacles().getFirst().getWidth()
//                        + " " + world.getObstacles().getFirst().getX() + " " + world.getObstacles().getFirst().getY()
//                        + " " + player.getX() + " " + player.getY() + " " + player.getWidth() + " " + player.getHeight());
            }
        };

        gameLoop.start();
    }

    //Everything called once, at the game start
    private void load(){
        System.out.println("Game starting");

    }

    public void step() {

        if (sPressed && !world.getPlayer().isDived() && !world.getPlayer().isJumped()) {
            world.getPlayer().dive();
        }
        if (wPressed && !world.getPlayer().isDived() && !world.getPlayer().isJumped()) {
            world.getPlayer().jump();
        }

        if (aPressed && world.getPlayer().getX() > world.getBorderLeft() + 70) {
            world.getPlayer().moveLeft();
        }
        if (dPressed && world.getPlayer().getX() < world.getBorderRight() - 50) {
            world.getPlayer().moveRight();
        }

        if ((world.getPlayer().getX() + world.getPlayer().getMoveX() < world.getBorderLeft() + 70) && world.getPlayer().getMoveX() != 0) {
            world.getPlayer().setMoveX(0);
            world.getPlayer().setX(world.getBorderLeft() + 70);
        }

        if ((world.getPlayer().getX() + world.getPlayer().getMoveX() > world.getBorderRight() - 50) && world.getPlayer().getMoveX() != 0) {
            world.getPlayer().setMoveX(0);
            world.getPlayer().setX(world.getBorderRight() - 50);
        }

        world.generateObstacle();

        world.getObstacles().removeIf(obstacle -> obstacle.getX() + obstacle.getWidth() <= 0);

        Obstacle.moveObstacles(0, world.getObstacles());
//ne 0 !!!!!!!!!!!!
        world.getPlayer().moveVertical(world.getObstacles());

        aPressed = false;
        dPressed = false;
        wPressed = false;
        sPressed = false;
    }

    //Called every game frame
    private void update() {
        time ++;
        moveObstacles(0.5);
//ne 0 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!11
        player.setLayoutX(world.getPlayer().getX());
        player.setLayoutY(world.getPlayer().getY());

        if(isBirdDead()){
            resetBird();
        }
    }

    @FXML
    void keyPressed(KeyEvent event) {

        if(event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
            aPressed = true;
        }
        if(event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
            dPressed = true;
        }
        if(event.getCode() == KeyCode.SPACE || event.getCode() == KeyCode.W || event.getCode() == KeyCode.UP) {
            wPressed = true;
        } else if(event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S) {
            sPressed = true;
        }
    }
//
//    private void fly(){
//        if (player.getLayoutY() + player.getY() <= jumpHeight) {
//            movePlayerY(-(player.getLayoutY() + player.getY()));
//            time = 0;
//        }
//
//        movePlayerY(-jumpHeight);
//        time = 0;
//    }

    private Obstacle createObstacle() {
        return null;
    }

    private void moveObstacles(double positionChange){

        for (Obstacle obstacle : world.getObstacles()) {
            obstacle.setX(obstacle.getX() - positionChange);
        }

    }
//
//    private void movePlayerY(double positionChange){
//        player.setY(world.getPlayer().getY() + positionChange);
//    }

    private boolean isBirdDead(){
        return (player.getLayoutX() + player.getWidth() < 0);
    }

    private void resetBird(){
        world.getPlayer().setX(73);
        world.getPlayer().setY(178);
        time = 0;
    }
}
