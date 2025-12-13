// language: java
package hr.fer.projekt.controllers;

import hr.fer.projekt.neuronskaMreza.NeuralNetwork;
import hr.fer.projekt.application.World;
import hr.fer.projekt.entities.Obstacle;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private NeuralNetwork neuralNetwork = new NeuralNetwork("NN_1",4, new int[]{10,10}, 4);
    private  boolean neuralNetworkPlaying = false;
    private final int FPS = 60;
    private final int NETWORK_REACTION_TIME_MS = 60;
    private final int TICK_TIME_MS= 3 ;
    private final double STARTING_GAME_SPEED = 0.5;
    private volatile double time = 0;

    public volatile Boolean
            aPressed = false, dPressed = false,
            sPressed = false, wPressed = false, newObstacle = false;

    @FXML
    private Rectangle player;
    @FXML
    private Rectangle more;
    @FXML
    private Rectangle nebo;
    @FXML
    private TextField scoreCounter;

    private Map<String, Rectangle> obstacles;

    @FXML
    private AnchorPane stage;

    private volatile World world;

    private AnimationTimer painter;
    private Thread physicsThread;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        markGameStart();
        stage.requestFocus();
        obstacles = new HashMap<>();
        startGame();

    }

    private void startPhysicsThread() {
        physicsThread = getPhysicsThread();
        physicsThread.start();
    }

    private void startPainter() {
        final long[] prev = {System.nanoTime()};
        final long intervalNanos = (long) (1e9 / Math.max(1, FPS));
        painter = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - prev[0] >= intervalNanos) {
                        update();
                    prev[0] = now;
                }
                if(world.getPlayer().isDead()){
                    System.out.println("Game over! Your score: " + (time));
                    saveScore(time);
                    this.stop();
                }
            }
        };
        painter.start();
    }

    private Thread getPhysicsThread() {
        Thread physicsThread = new Thread(() -> {
            try {
                int timeCounter = 0;
                while (!world.getPlayer().isDead()) {
                    step();
                    timeCounter += TICK_TIME_MS;
                    if (timeCounter >= NETWORK_REACTION_TIME_MS && neuralNetworkPlaying) {
                        processInputs();
                        timeCounter = 0;
                    }
                    Thread.sleep(TICK_TIME_MS);
                }
            } catch (InterruptedException ignored) {
            }
        }, "PhysicsLoop");
        physicsThread.setDaemon(true);
        return physicsThread;
    }

    void processInputs() {
        // Get sensor inputs from world
        double[] sensors = getInputs();

        // Get neural network output
        double[] output = neuralNetwork.generateOutput(sensors);

        if(output.length != 4) {
            throw new IllegalArgumentException("Neural network output size must be 4.");
        }

        if (output[0] >= 0.5) {
            aPressed = true;
        } else {
            aPressed = false;
        }
        if (output[1] >= 0.5) {
            dPressed = true;
        } else {
            dPressed = false;
        }
        if (output[2] >= 0.5) {
            wPressed = true;
        } else {
            wPressed = false;
        }
        if (output[3] >= 0.5) {
            sPressed = true;
        } else {
            sPressed = false;
        }

    }

    public void step() {

        time++;
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

        Obstacle.moveObstacles(STARTING_GAME_SPEED + time / 5000.0, world.getObstacles());

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

        scoreCounter.setText("SCORE: " + String.valueOf((int) time));

    }

    @FXML
    void keyPressed(KeyEvent event) {

        if (event.getCode() == KeyCode.R) {
            if (world != null && world.getPlayer().isDead()) {
                startGame();
            }
        }

        if (neuralNetworkPlaying) {
            return;
        }

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

        if (event.getCode() == KeyCode.R) {
            if (world != null && world.getPlayer().isDead()) {
                startGame();
            }
        }
    }

    @FXML
    void keyReleased(KeyEvent event) {

        if (neuralNetworkPlaying) {
            return;
        }

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

    private void startGame() {
        Platform.runLater(() -> {
            if (painter != null) {
                painter.stop();
            }
            System.out.println("Game starting");
            world = new World();
            time = 0;
            newObstacle = false;
            obstacles.clear();

            stage.getChildren().removeIf(node -> (node instanceof Rectangle) && node != player && node != more && node != nebo);

            obstacles.put(world.getObstacles().getLast().getID(), world.getObstacles().getLast().toRectangle());
            stage.getChildren().add(obstacles.get(world.getObstacles().getLast().getID()));

            player.setLayoutX(world.getPlayer().getX());
            player.setLayoutY(world.getPlayer().getY());

            startPhysicsThread();
            startPainter();
        });
    }

    private void saveScore(double score) {
        try {
            String entry = ((long) score) + System.lineSeparator();
            Files.write(Paths.get("scores.txt"), entry.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception e) {
            System.err.println("Failed to save score: " + e.getMessage());
        }
    }

    private void markGameStart() {
        try {
            String entry = "New game started." + System.lineSeparator();
            Files.write(Paths.get("scores.txt"), entry.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception e) {
            System.err.println("Failed to write game start: " + e.getMessage());
        }
    }

    private double[] getInputs() {
        // Example: 4 sensors (distance to obstacle, obstacle height, etc.)
        Obstacle nearest = world.getObstacles().getFirst();
        return new double[]{
            nearest.getX(),
            nearest.getWidth(),
            nearest.getHeight(),
            world.getPlayer().getX()
        };
    }

}