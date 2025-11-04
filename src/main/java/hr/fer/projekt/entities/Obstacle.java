package hr.fer.projekt.entities;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.util.List;
import java.util.Random;

public class Obstacle extends Entity {

    private Rectangle shape;

    public Obstacle(double x, double y, double width, double height, Color color) {
        super(x, y, width, height);
        shape = new Rectangle(width, height);
        shape.setFill(color);
        shape.setArcHeight(5.0);
        shape.setArcWidth(5.0);
        shape.setStroke(Color.BLACK);
        shape.setStrokeType(StrokeType.INSIDE);
        shape.setLayoutX(x);
        shape.setLayoutY(y);
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
            case 0: return new Obstacle(700, 125, 75, 175, Color.ALICEBLUE);
            case 1: return new Obstacle(700, 160, 150, 75, Color.WHITE);
            case 2: return new Obstacle(700, 150, 100, 250, Color.SANDYBROWN);
            default: throw new IllegalArgumentException("Exception in class Obstacle line 23");
        }
    }

    @Override
    public double getX() {
        return shape.getLayoutX();
    }


    @Override
    public void setX(double x) {
        shape.setLayoutX(x);
    }

    public Rectangle getShape() {
        return shape;
    }

    public void setShape(Rectangle shape) {
        this.shape = shape;
    }
}
