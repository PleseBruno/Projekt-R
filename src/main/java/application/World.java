package application;

import entities.Obstacle;
import entities.Player;
import temp.KeyPress;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class World {

    private List<Obstacle> obstacles;
    private int borderLeft = -128;
    private int borderRight = 128;
    public final double SCALAR = 10;
    private final double speed = 1 / SCALAR;

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

    public void step(KeyPress keyPress) {

        if (keyPress.isDown() && !player.isDived() && !player.isJumped()) {
            player.dive();
        }
        if (keyPress.isUp() && !player.isDived() && !player.isJumped()) {
            player.jump();
        }
        if (keyPress.isLeft() && player.getX() > borderLeft + 30) {
            player.moveLeft();
        }
        if (keyPress.isRight() && player.getX() < borderRight - 20) {
            player.moveRight();
        }

        if ((player.getX() + player.getMoveX() < borderLeft + 30) && player.getMoveX() != 0) {
            player.setMoveX(0);
            player.setX(borderLeft + 30);
        }

        if ((player.getX() + player.getMoveX() > borderRight - 20) && player.getMoveX() != 0) {
            player.setMoveX(0);
            player.setX(borderRight - 20);
        }

        generateObstacle();

        obstacles.removeIf(obstacle -> obstacle.getX() + obstacle.getWidth() <= -128);

        Obstacle.moveObstacles(speed , obstacles);

        player.moveVertical(obstacles);

        keyPress.reset();
    }
}
