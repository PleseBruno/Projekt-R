package application;

import java.io.IOException;
import java.util.Scanner;
import java.util.List;

public class Main {

    public static void repaint(Player player, List<Obstacle> obstacles) {
        System.out.println("player: " + player.getX() + " " + player.getY() + " " + player.getMoveX() + " " + player.getMoveY());
        int counter = 0;
        for (Obstacle obstacle : obstacles){
            System.out.println("obstacle " + counter + " " + obstacle.getX() + " " + obstacle.getY());
            counter++;
        }
    }

    public static void main(String[] args) throws IOException {
        World world = new World();
        int FPS = 2;
        double refreshPerFps = 10;
        double speed = 1 / refreshPerFps;

        Scanner scanner = new Scanner(System.in);

        KeyPress keyPress = new KeyPress();

        double drawInterval = 1E9/(FPS * refreshPerFps);
        double nextDrawTime = drawInterval + System.nanoTime();
        int counter = 0;
        String line;
        while (!world.getPlayer().isDead()) {
            if (counter % 60 == 0) {
                keyPress.reset();
                if ((line = scanner.nextLine()) != null)
                    keyPress.receiveKey(line);
                counter = 0;
            }
            world.step(keyPress);

            if (counter % 20 == 0) {
                repaint(world.getPlayer(), world.getObstacles());
            }

            try {
                double remainingTime = nextDrawTime - System.nanoTime();

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                counter++;
                Thread.sleep((long) (remainingTime / 1E6));

                nextDrawTime = drawInterval + System.nanoTime();

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        scanner.close();
    }
}