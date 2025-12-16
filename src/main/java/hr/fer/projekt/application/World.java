package hr.fer.projekt.application;

import hr.fer.projekt.entities.Obstacle;
import hr.fer.projekt.entities.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {

    private List<Obstacle> obstacles;
    private int borderLeft = 0;
    private int borderRight = 600;

    private Player player;

    public Player getPlayer(){
        return player;
    }

    public Random rand;

    public List<Obstacle> getObstacles(){
        return obstacles;
    }

    private int objectCounter = 1;

    public World(Random rand) {
        player = new Player(73, 178,57,44);
        obstacles = new ArrayList<Obstacle>();
        this.rand = rand;
        obstacles.add(Obstacle.randomObstacle(String.valueOf(objectCounter), rand));
    }

    public void generateObstacle() {
        objectCounter++;
        obstacles.add(Obstacle.randomObstacle(String.valueOf(objectCounter), rand));
    }

    public Random getRandom() {
        return rand;
    }

    public int getBorderLeft() {
        return borderLeft;
    }

    public int getBorderRight() {
        return borderRight;
    }
}
