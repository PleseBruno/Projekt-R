package application;

import entities.Obstacle;
import entities.Player;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import temp.KeyPress;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main extends Application {

    public static void repaint(Player player, List<Obstacle> obstacles) {
        System.out.println("player: " + player.getX() + " " + player.getY() + " " + player.getMoveX() + " " + player.getMoveY());
        int counter = 0;
        for (Obstacle obstacle : obstacles){
            System.out.println("obstacle " + counter + " " + obstacle.getX() + " " + obstacle.getY());
            counter++;
        }
    }

    // Old headless loop preserved for debugging but not used by default.
    public static void runHeadlessLoop(){
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

    public static void main(String[] args){
        // Launch JavaFX application; if you want the headless console loop, call runHeadlessLoop() instead.
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root, Color.WHITE);
        stage.setWidth(1280);
        stage.setHeight(720);
        stage.setResizable(false);
        Rectangle duck = new Rectangle(25, 25, Color.YELLOW);
        duck.setX(590);
        duck.setY(335);
        root.getChildren().add(duck);
        stage.setScene(scene);
        stage.setTitle("Patikica v.1.0");
        stage.show();
    }
}