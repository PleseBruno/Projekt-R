package hr.fer.projekt.controllers;

import hr.fer.projekt.application.World;
import hr.fer.projekt.entities.Obstacle;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class mainController implements Initializable {

    private final int FPS = 120;
    private final int GAME_SPEED_STEPS = 1;

    public volatile Boolean
            aPressed = false, dPressed = false,
            sPressed = false, wPressed = false, newObstacle = false;

    AnimationTimer gameLoop;

    @FXML
    private Rectangle player;

    @FXML
    private Rectangle more;

    @FXML
    private Rectangle nebo;

    private Map<String, Rectangle> obstacles;

    @FXML
    private AnchorPane stage;
    
    private volatile World world;
    
    double yDelta = 0.02 ;
    double time = 0;
    int jumpHeight = 100;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        stage.requestFocus();

        world = new World();

        obstacles = new HashMap<>();
        obstacles.put(world.getObstacles().getLast().getID(), world.getObstacles().getLast().toRectangle());
        stage.getChildren().add(obstacles.get(world.getObstacles().getLast().getID()));


        player.setLayoutX(world.getPlayer().getX());
        player.setLayoutY(world.getPlayer().getY());

        load();

        Thread physicsThread = new Thread(() -> {
            try {
                // physics loop: wake every 1 ms and perform GAME_SPEED_STEPS steps to speed simulation
                while (!world.getPlayer().isDead()) {
                    int steps = Math.max(1, GAME_SPEED_STEPS);
                    for (int i = 0; i < steps; i++) {
                        step();

                    }
                    Thread.sleep(5); // sleep 1ms as requested (physics tick)
                }
            } catch (InterruptedException ex) {
                // thread interrupted -> exit
            }
        }, "PhysicsLoop");
        physicsThread.setDaemon(true);
        physicsThread.start();

        // Rendering: AnimationTimer that repaints at configured FPS (does not affect physics)
        final long[] prev = {System.nanoTime()};
        final long intervalNanos = (long) (1e9 / Math.max(1, FPS));
        AnimationTimer painter = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - prev[0] >= intervalNanos) {
                    update();
                    prev[0] = now;
                }
            }
        };
        painter.start();
    };
//
//        gameLoop = new AnimationTimer() {
//            @Override
//            public void handle(long l) {
//
//                update();
//                step();
//
//                System.out.println(world.getObstacles().getFirst().getHeight() + " "  + world.getObstacles().getFirst().getWidth()
//                        + " " + world.getObstacles().getFirst().getX() + " " + world.getObstacles().getFirst().getY()
//                        + " " + player.getX() + " " + player.getY() + " " + player.getWidth() + " " + player.getHeight());
//            }
//            gameLoop.start

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
        if (world.getObstacles().getLast().getX() < 250) {

            world.generateObstacle();

            newObstacle = true;

        }

        world.getObstacles().removeIf(obstacle -> obstacle.getX() + obstacle.getWidth() <= 0);

        Obstacle.moveObstacles(0.5, world.getObstacles());

        world.getPlayer().moveVertical(world.getObstacles());
    }

    //Called every game frame
    private void update() {
        time ++;

        player.setLayoutX(world.getPlayer().getX());
        player.setLayoutY(world.getPlayer().getY());

        if (newObstacle) {

            obstacles.put(world.getObstacles().getLast().getID(), world.getObstacles().getLast().toRectangle());
            stage.getChildren().add(obstacles.get(world.getObstacles().getLast().getID()));

            newObstacle = false;
        }


        for(Obstacle obstacle : world.getObstacles()) {
            obstacles.get(obstacle.getID()).setLayoutX(obstacle.getX());
            obstacles.get(obstacle.getID()).setLayoutY(obstacle.getY());
        }

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

    @FXML
    void keyReleased(KeyEvent event) {
        if(event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
            aPressed = false;
        }
        if(event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
            dPressed = false;
        }
        if(event.getCode() == KeyCode.SPACE || event.getCode() == KeyCode.W || event.getCode() == KeyCode.UP) {
            wPressed = false;
        } else if(event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S) {
            sPressed = false;
        }
    }

    private boolean isBirdDead(){
        return (player.getLayoutX() + player.getWidth() < 0);
    }

    private void resetBird(){
        world.getPlayer().setX(73);
        world.getPlayer().setY(178);
        time = 0;
    }

}
