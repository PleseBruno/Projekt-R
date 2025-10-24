package entities;

import java.util.List;
import java.util.Random;

public class Obstacle extends Entity {

    public Obstacle(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    public static void moveObstacles(double length, List<Obstacle> obstacles) {
        for (Obstacle obstacle : obstacles){
            obstacle.setX(obstacle.getX() - length);
        }
    }

    public static Obstacle randomObstacle(){
        Random rand = new Random();
        int randomNum = rand.nextInt(3);

        switch (randomNum){
            case 0: return new Obstacle(128, 10, 20, 40);
            case 1: return new Obstacle(128, 10, 20, 80);
            case 2: return new Obstacle(128, 10, 20, 60);
            default: throw new IllegalArgumentException("Exception in class Obstacle line 23");
        }
    }
}
