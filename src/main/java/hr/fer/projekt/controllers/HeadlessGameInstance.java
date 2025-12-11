package hr.fer.projekt.controllers;

import hr.fer.projekt.neuronskaMreza.*;
import hr.fer.projekt.entities.*;
import hr.fer.projekt.application.*;

public class HeadlessGameInstance {
    
    private final NeuralNetwork neuralNetwork;
    private final World world;
    private final int GAME_SPEED_STEPS = 1;
    private final int TICK_TIME_MS = 3;
    private final double STARTING_GAME_SPEED = 0.5;
    private volatile double time = 0;
    private volatile boolean gameRunning = true;
    private static final long MAX_GAME_TIME = 60000; // 60 seconds max
    
    public HeadlessGameInstance(NeuralNetwork nn) {
        this.neuralNetwork = nn;
        this.world = new World();
    }
    
    /**
     * Run the game until player dies and return fitness score
     */
    public double run() {
        long startTime = System.currentTimeMillis();
        
        while (gameRunning && !world.getPlayer().isDead()) {
            // Timeout after max game time
            if (System.currentTimeMillis() - startTime > MAX_GAME_TIME) {
                break;
            }
            
            step();
            
            try {
                Thread.sleep(TICK_TIME_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            
            time++;
        }
        
        // Fitness = survival time
        return time;
    }
    
    private void step() {
        // Get sensor inputs from world
        double[] sensors = getSensorInputs();
        
        // Get neural network output
        double[] output = neuralNetwork.generateOutput(sensors);
        
        // Convert output to actions (0=left, 1=right, 2=jump, 3=dive)
        int action = argMax(output);
        executeAction(action);
        
        // Update world physics
        updatePhysics();
    }
    
    private double[] getSensorInputs() {
        // Example: 4 sensors (distance to obstacle, obstacle height, etc.)
        Obstacle nearest = world.getObstacles().getLast();
        return new double[]{
            nearest.getX(),
            nearest.getWidth(),
            nearest.getHeight(),
            world.getPlayer().getX()
        };
    }
    
    private void executeAction(int action) {
        switch (action) {
            case 0: // Move left
                if (world.getPlayer().getX() > world.getBorderLeft() + 70) {
                    world.getPlayer().moveLeft();
                }
                break;
            case 1: // Move right
                if (world.getPlayer().getX() < world.getBorderRight() - 57) {
                    world.getPlayer().moveRight();
                }
                break;
            case 2: // Jump
                if (!world.getPlayer().isDived() && !world.getPlayer().isJumped()) {
                    world.getPlayer().jump();
                }
                break;
            case 3: // Dive
                if (!world.getPlayer().isDived() && !world.getPlayer().isJumped()) {
                    world.getPlayer().dive();
                }
                break;
        }
    }
    
    private void updatePhysics() {
        if (world.getObstacles().getLast().getX() + world.getObstacles().getLast().getWidth() < 250) {
            world.generateObstacle();
        }
        
        Obstacle.moveObstacles(STARTING_GAME_SPEED + time / 1000.0, world.getObstacles());
        world.getObstacles().removeIf(o -> o.getX() + o.getWidth() <= -50);
        world.getPlayer().moveVertical(world.getObstacles());
    }
    
    private int argMax(double[] arr) {
        int idx = 0;
        double max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
                idx = i;
            }
        }
        return idx;
    }
}