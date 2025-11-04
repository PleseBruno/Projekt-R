package hr.fer.projekt.entities;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

public class Obstacle extends Entity implements Comparable<Obstacle> {

    private Color color;
    private String ID;

    public Obstacle(double x, double y, double width, double height, Color color, String ID) {
        super(x, y, width, height);
        this.color = color;
        this.ID = ID;
//        shape = new Rectangle(width, height);
//        shape.setFill(color);
//        shape.setArcHeight(5.0);
//       shape.setArcWidth(5.0);
//        shape.setStroke(Color.BLACK);
//        shape.setStrokeType(StrokeType.INSIDE);
//        shape.setLayoutX(x);

//        shape.setLayoutY(y);
//        shape.setId(this.toString());
    }

    public Color getColor() {
        return color;
    }

    public String getID() {
        return ID;
    }

    public static void moveObstacles(double length, List<Obstacle> obstacles) {
        for (Obstacle obstacle : obstacles){
            obstacle.setX(obstacle.getX() - length);
        }
    }

    public static Obstacle randomObstacle(String ID){
        Random rand = new Random();
        int randomNum = rand.nextInt(3);

        switch (randomNum){
            case 0: return new Obstacle(700, 125, 75, 175, Color.ALICEBLUE, ID);
            case 1: return new Obstacle(700, 160, 150, 75, Color.WHITE, ID);
            case 2: return new Obstacle(700, 150, 100, 250, Color.SANDYBROWN, ID);
            default: throw new IllegalArgumentException("Exception in class Obstacle line 23");
        }
    }

    public Rectangle toRectangle() {
        Rectangle rectangle = new Rectangle(getWidth(), getHeight());
        rectangle.setArcHeight(5.0);
        rectangle.setArcWidth(5.0);
        rectangle.setLayoutX(getX());
        rectangle.setLayoutY(getY());
        rectangle.setId(this.getID());
        rectangle.setFill(getColor());
        rectangle.setStrokeType(StrokeType.INSIDE);
        rectangle.setStroke(Color.BLACK);
        return rectangle;
    }


    @Override
    public int compareTo(Obstacle o) {
        return this.getID().compareTo(o.getID());
    }
}
