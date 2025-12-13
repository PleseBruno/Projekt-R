package hr.fer.projekt.controllers;

import hr.fer.projekt.neuronskaMreza.*;
import hr.fer.projekt.entities.*;
import hr.fer.projekt.application.*;

public class  HeadlessGameInstance {
    
    private final NeuralNetwork neuralNetwork;
    private final int NETWORK_REACTION_TIME_TICKS = 60;
    private final World world;
    private final long TICK_TIME_MS = 0;
    private final double STARTING_GAME_SPEED = 0.5;
    private volatile int time = 0;
    private volatile boolean gameRunning = true;

    private boolean aPressed = false, dPressed = false,
            sPressed = false, wPressed = false;
    
    public HeadlessGameInstance(NeuralNetwork nn) {
        this.neuralNetwork = nn;
        this.world = new World();
    }

    private double[] getInputs() {
        // Example: 4 sensors (distance to obstacle, obstacle height, etc.)
        Obstacle nearest = world.getObstacles().getFirst();
        return new double[]{
                nearest.getX(),
                nearest.getY(),
                nearest.getWidth(),
                nearest.getHeight(),
                world.getPlayer().getX(),
                world.getPlayer().getY(),
                time
        };
    }

    
    public double run() throws InterruptedException {
        int timeCounter = 0;
        while (gameRunning && !world.getPlayer().isDead()) {
            
            step();

            if (timeCounter % NETWORK_REACTION_TIME_TICKS == 0) {
                processInputs();
                timeCounter = 0;
            }
            time++;
            //if(time% 30 == 0 )consolePrint();

            Thread.sleep(TICK_TIME_MS);
            timeCounter++;
        }
        
        // Fitness = survival time
        return time;
    }
    

    void consolePrint(){
        System.out.println("Time: " + time +
                " | Player Y: " + world.getPlayer().getY() +
                " | Player X: " + world.getPlayer().getX() +
                " | Nearest Obstacle X: " + world.getObstacles().getFirst().getX() +
                " | Nearest Obstacle Y: " + world.getObstacles().getFirst().getY()
        );
    }

    private void processInputs() {
        // Get sensor inputs from world
        double[] sensors = getInputs();
        
        // Get neural network output
        double[] output = neuralNetwork.generateOutput(sensors);
        
        // Process output to control player
        if (output.length != 4) {
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

    private void step() {

        // Update world physics
        updatePhysics();
    }
    

    

    
    private void updatePhysics() {

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
        }
        
        Obstacle.moveObstacles(STARTING_GAME_SPEED + time / 5000.0, world.getObstacles());
        world.getObstacles().removeIf(o -> o.getX() + o.getWidth() <= -50);
        world.getPlayer().moveVertical(world.getObstacles());
    }

}