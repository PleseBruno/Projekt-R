package hr.fer.projekt.controllers;

import hr.fer.projekt.application.World;
import hr.fer.projekt.entities.Obstacle;
import hr.fer.projekt.temp.KeyPress;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

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

        world = new World();

        load();

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long l) {
                update();
            }
        };

        gameLoop.start();
    }

    //Everything called once, at the game start
    private void load(){
        System.out.println("Game starting");

    }

    public void step(KeyPress keyPress) {

        if (keyPress.isDown() && !world.getPlayer().isDived() && !world.getPlayer().isJumped()) {
            world.getPlayer().dive();
        }
        if (keyPress.isUp() && !world.getPlayer().isDived() && !world.getPlayer().isJumped()) {
            world.getPlayer().jump();
        }
        if (keyPress.isLeft() && world.getPlayer().getX() > world.getBorderLeft() + 30) {
            world.getPlayer().moveLeft();
        }
        if (keyPress.isRight() && world.getPlayer().getX() < world.getBorderRight() - 20) {
            world.getPlayer().moveRight();
        }

        if ((world.getPlayer().getX() + world.getPlayer().getMoveX() < world.getBorderLeft() + 30) && world.getPlayer().getMoveX() != 0) {
            world.getPlayer().setMoveX(0);
            world.getPlayer().setX(world.getBorderLeft() + 30);
        }

        if ((world.getPlayer().getX() + world.getPlayer().getMoveX() > world.getBorderRight() - 20) && world.getPlayer().getMoveX() != 0) {
            world.getPlayer().setMoveX(0);
            world.getPlayer().setX(world.getBorderRight() - 20);
        }

        world.generateObstacle();

        world.getObstacles().removeIf(obstacle -> obstacle.getX() + obstacle.getWidth() <= -128);

        Obstacle.moveObstacles(1 , world.getObstacles());

        world.getPlayer().moveVertical(world.getObstacles());

        keyPress.reset();
    }

    //Called every game frame
    private void update() {
        time ++;
        moveObstacles(yDelta * time);

        if(isBirdDead()){
            resetBird();
        }
    }

    @FXML
    void keyPressed(KeyEvent event) {

        if(event.getCode() == KeyCode.SPACE || event.getCode() == KeyCode.W || event.getCode() == KeyCode.UP) {
            wPressed = true;
        } else if(event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S) {
            sPressed = true;
        } else if(event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
            aPressed = true;
        } else if(event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
            dPressed = true;
        }
    }

    private void fly(){
        if (player.getLayoutY() + player.getY() <= jumpHeight) {
            movePlayerY(-(player.getLayoutY() + player.getY()));
            time = 0;
            return;
        }

        movePlayerY(-jumpHeight);
        time = 0;
    }

    private Obstacle createObstacle() {
        return null;
    }

    private void moveObstacles(double positionChange){
    }


    private void movePlayerY(double positionChange){
        player.setY(player.getY() + positionChange);
    }

    private boolean isBirdDead(){
        double playerY = player.getLayoutY() + player.getY();
        return playerY >= stage.getHeight();
    }

    private void resetBird(){
        player.setY(0);
        time = 0;
    }
}
