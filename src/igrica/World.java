package igrica;

import java.util.ArrayList;
import java.util.List;

public class World implements Runnable {

    Thread gameThread;
    Thread playerThread;
    List<Obstacle> obstacles;

    private int borderLeft = -128;
    private int borderRight = 128;

    public int FPS = 2;
    private double speed = 0;
    KeyPress keyPress = new KeyPress();

    Player player = new Player(-10, 0,5,5);

    public World() {
        playerThread = new Thread(() -> keyPress.receiveKey());
        obstacles = new ArrayList<Obstacle>();
        obstacles.add(Obstacle.randomObstacle());
        playerThread.start();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        keyPress.reset();

        double drawInterval = 1E9/FPS;
        double nextDrawTime = drawInterval + System.nanoTime();

        while (gameThread != null) {

            update();

            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();

                if (remainingTime < 0) {
                    remainingTime = 0;
                }
                Thread.sleep((long) (remainingTime / 1E6));

                nextDrawTime = drawInterval + System.nanoTime();

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void generateObstacle() {
       if(obstacles.getLast().getX() <= 0){
           obstacles.add(Obstacle.randomObstacle());
       }
    }

    public void update() {

        if (keyPress.isDown() && !player.isDived() && !player.isJumped()) {
            player.dive();
        }
        if (keyPress.isUp() && !player.isDived() && !player.isJumped()) {
            player.jump();
        }
        if (keyPress.isLeft() && player.getX() > borderLeft + 30) {
            player.goLeft();
        }
        if (keyPress.isRight() && player.getX() < borderRight - 20) {
            player.goRight();
        }

        if ((player.getX() + player.getForceX() < borderLeft + 30) && player.getForceX() != 0) {
            player.setForceX(0);
            player.setX(borderLeft + 30);
        }

        if ((player.getX() + player.getForceX() > borderRight - 20) && player.getForceX() != 0) {
            player.setForceX(0);
            player.setX(borderRight - 20);
        }

        if(player.isDead()) {
            System.out.println("kraj");
            gameThread.interrupt();
        }

        generateObstacle();

        obstacles.removeIf(obstacle -> obstacle.getX() + obstacle.getWidth() <= -128);

        Obstacle.updateObstacles(speed , obstacles);

        player.playerUpdate(obstacles);

        keyPress.reset();
    }

    public void repaint() {
        System.out.println("player: " + player.getX() + " " + player.getY() + " " + player.getForceX() + " " + player.getForceY());
        int counter = 0;
        for (Obstacle obstacle : obstacles){
            System.out.println("obstacle " + counter + " " + obstacle.getX() + " " + obstacle.getY());
            counter++;
        }
    }
}
