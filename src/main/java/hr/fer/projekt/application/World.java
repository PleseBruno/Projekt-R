package hr.fer.projekt.application;

import hr.fer.projekt.entities.Obstacle;
import hr.fer.projekt.entities.Player;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class World {

    private List<Obstacle> obstacles;
    private int borderLeft = 0;
    private int borderRight = 600;

    private Player player;

    public Player getPlayer(){
        return player;
    }

    public List<Obstacle> getObstacles(){
        return obstacles;
    }

    private int objectCounter = 1;

    public World() {
        player = new Player(73, 178,57,44);
        obstacles = new ArrayList<Obstacle>();
        obstacles.add(Obstacle.randomObstacle(String.valueOf(objectCounter)));
    }

    public void generateObstacle() {
        objectCounter++;
        obstacles.add(Obstacle.randomObstacle(String.valueOf(objectCounter)));
    }

    public int getBorderLeft() {
        return borderLeft;
    }

    public int getBorderRight() {
        return borderRight;
    }
}
