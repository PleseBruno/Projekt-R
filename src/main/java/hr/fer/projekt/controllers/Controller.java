package hr.fer.projekt.controllers;

import hr.fer.projekt.application.World;
import hr.fer.projekt.entities.Obstacle;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private final boolean graphicsOn = true;
    private final int FPS = 60;
    private final int GAME_SPEED_STEPS = 1;

    public volatile Boolean
            aPressed = false, dPressed = false,
            sPressed = false, wPressed = false, newObstacle = false;

    @FXML
    private Rectangle player;

    private Map<String, Rectangle> obstacles;

    @FXML
    private AnchorPane stage;
    
    private volatile World world;
    
    double yDelta = 0.02 ;
    volatile double time = 0;

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

        Thread physicsThread = getPhysicsThread();
        physicsThread.start();

        // Rendering
        final long[] prev = {System.nanoTime()};
        final long intervalNanos = (long) (1e9 / Math.max(1, FPS));
        AnimationTimer painter = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - prev[0] >= intervalNanos) {
                    if (graphicsOn){
                        update();
                    }else {
                        cosoleprint();
                    }

                    prev[0] = now;
                }
                if(world.getPlayer().isDead()){
                    System.out.println("Game over! Your score: " + (time));
                    this.stop();
                }
            }
        };
        painter.start();

    }

    private Thread getPhysicsThread() {
        Thread physicsThread = new Thread(() -> {
            try {
                // physics loop
                while (!world.getPlayer().isDead()) {
                    int steps = Math.max(1, GAME_SPEED_STEPS);
                    for (int i = 0; i < steps; i++) {
                        step();

                    }
                    Thread.sleep(3);
                }
            } catch (InterruptedException ignored) {
            }
        }, "PhysicsLoop");
        physicsThread.setDaemon(true);
        return physicsThread;
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
        if (dPressed && world.getPlayer().getX() < world.getBorderRight() - 57) {
            world.getPlayer().moveRight();
        }

        if (world.getObstacles().getLast().getX() + world.getObstacles().getLast().getWidth() < 250) {

            world.generateObstacle();

            newObstacle = true;
        }

        Obstacle.moveObstacles(0.5 + time / 1000.0, world.getObstacles());

        world.getObstacles().removeIf(obstacle -> obstacle.getX() + obstacle.getWidth() <= -50);

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

        stage.getChildren().removeIf(node -> {
            if (node instanceof Rectangle) {
                Rectangle rect = (Rectangle) node;
                return rect.getLayoutX() <= -rect.getWidth() && obstacles.containsKey(rect.getId());
            }
            return false;
        });

    }

    private void cosoleprint() {
        time ++;

        System.out.println("Player position: X=" + world.getPlayer().getX() + " Y=" + world.getPlayer().getY());

        for(Obstacle obstacle : world.getObstacles()) {
            System.out.println("Obstacle ID=" + obstacle.getID() + " position: X=" + obstacle.getX() + " Y=" + obstacle.getY());
        }

        System.out.println("-----");
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

}