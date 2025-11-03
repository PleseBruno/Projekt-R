package hr.fer.projekt.application;

import hr.fer.projekt.entities.Obstacle;
import hr.fer.projekt.entities.Player;
import hr.fer.projekt.temp.KeyPress;

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

    public World() {
        player = new Player(-10, 0,5,5);
        obstacles = new LinkedList<Obstacle>();
        obstacles.add(Obstacle.randomObstacle());
    }

    public void generateObstacle() {
       if(((LinkedList<Obstacle>) obstacles).getLast().getX() <= 0){
           obstacles.add(Obstacle.randomObstacle());
       }
    }

    public int getBorderLeft() {
        return borderLeft;
    }

    public int getBorderRight() {
        return borderRight;
    }
}
