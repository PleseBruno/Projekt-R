package application;

import hr.fer.projekt.application.World;
import hr.fer.projekt.entities.Obstacle;
import hr.fer.projekt.entities.Player;
import hr.fer.projekt.temp.KeyPress;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.List;

public class Main extends Application {

    // Toggle to run the old headless loop concurrently with the JavaFX UI.
    /**
     * TODO: fix so that game runs normally when headless set to false
     */
    public static boolean RUN_HEADLESS = true;

    // UI references used by repaint to update the scene.
    private static Group rootRef;
    private static Rectangle duckRef;
    private static Group obstaclesGroupRef;

    // World-to-stage mapping constants
    private static final int STAGE_PX_W = 1280;
    private static final int STAGE_PX_H = 720;
    private static final int WORLD_W = 256;    // world width in world units
    private static final int WORLD_H = 144;    // world height in world units
    private static final int BORDER_LEFT = -128; // world left border
    private static double scaleX;
    private static double scaleY;

    private static double worldToPixelX(double worldX, double rectPixelWidth) {
        return (worldX - BORDER_LEFT) * scaleX;
    }

    private static double worldToPixelY(double worldY, double rectPixelHeight) {
        return (WORLD_H / 2.0 - worldY) * scaleY;
    }

    /**
     * The function is called when
     *
     *
     * @param player
     * @param obstacles
     */
    public static void repaint(Player player, List<Obstacle> obstacles) {
        System.out.println("player: " + player.getX() + " " + player.getY() + " " + player.getMoveX() + " " + player.getMoveY());
        int counter = 0;
        for (Obstacle obstacle : obstacles){
            System.out.println("obstacle " + counter + " " + obstacle.getX() + " " + obstacle.getY());
            counter++;
        }

        // If the JavaFX UI is available, update it on the JavaFX Application Thread.
        if (rootRef != null && duckRef != null && obstaclesGroupRef != null) {
            Platform.runLater(() -> {
                // Compute pixel sizes from world sizes
                double duckWorldW = player.getWidth();
                double duckWorldH = player.getHeight();
                double duckPxW = duckWorldW * scaleX;
                double duckPxH = duckWorldH * scaleY;

                // Update player (duck) size and position (centered on world coords)
                duckRef.setWidth(duckPxW);
                duckRef.setHeight(duckPxH);
                duckRef.setX(worldToPixelX(player.getX(), duckPxW));
                duckRef.setY(worldToPixelY(player.getY(), duckPxH));

                // Redraw obstacles as green rectangles sized according to obstacle world size
                obstaclesGroupRef.getChildren().clear();
                for (Obstacle obstacle : obstacles) {
                    double obsPxW = obstacle.getWidth() * scaleX;
                    double obsPxH = obstacle.getHeight() * scaleY;
                    Rectangle r = new Rectangle(obsPxW, obsPxH, Color.SADDLEBROWN);
                    r.setX(worldToPixelX(obstacle.getX(), obsPxW));
                    r.setY(worldToPixelY(obstacle.getY(), obsPxH));
                    obstaclesGroupRef.getChildren().add(r);
                }
            });
        }
    }

    // Old headless loop preserved for debugging. When RUN_HEADLESS is true this runs in a background thread.
    public static void oldMain(){
        World world = new World();
        int FPS = 30;
        double refreshPerFps = 10;
        double speed = 1 / refreshPerFps;

        KeyPress keyPress = new KeyPress();

        double drawInterval = 1E9/(FPS * refreshPerFps);
        double nextDrawTime = drawInterval + System.nanoTime();
        int counter = 0;
        String line;
        while (!world.getPlayer().isDead()) {
            if (counter % 60 == 0) {
                keyPress.reset();
                counter = 0;
            }
            world.step(keyPress);

            if (counter % 20 == 0) {
                // This now also triggers redraw in the JavaFX application (if running).
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
    }


    /**
     * This function is called on launch.
     * <p>
     * It sets up the JavaFX scene, computes the scale factors
     *
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root, Color.LIGHTBLUE);
        Rectangle bottom = new Rectangle(STAGE_PX_W, STAGE_PX_H / 2.0, Color.DODGERBLUE);
        bottom.setY(STAGE_PX_H / 2.0 + 10);
        stage.setWidth(STAGE_PX_W);
        stage.setHeight(STAGE_PX_H);
        stage.setResizable(false);

        // compute scale factors from stage and world sizes
        scaleX = STAGE_PX_W / (double) WORLD_W;
        scaleY = STAGE_PX_H / (double) WORLD_H;


        World uiWorld = new World();
        Player uiPlayer = uiWorld.getPlayer();


        double duckPxW = uiPlayer.getWidth() * scaleX;
        double duckPxH = uiPlayer.getHeight() * scaleY;
        Rectangle duck = new Rectangle(duckPxW, duckPxH, Color.BLACK);
        duck.setX(worldToPixelX(uiPlayer.getX(), duckPxW));
        duck.setY(worldToPixelY(uiPlayer.getY(), duckPxH));


        Group obstaclesGroup = new Group();

        for (Obstacle o : uiWorld.getObstacles()) {
            double obsPxW = o.getWidth() * scaleX;
            double obsPxH = o.getHeight() * scaleY;
            Rectangle r = new Rectangle(obsPxW, obsPxH, Color.BLUE);
            r.setX(worldToPixelX(o.getX(), obsPxW));
            r.setY(worldToPixelY(o.getY(), obsPxH));
            obstaclesGroup.getChildren().add(r);
        }
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.A) {
                KeyPress.receiveKey("a");
            }  if (e.getCode() == KeyCode.D) {
                KeyPress.receiveKey("d");
            }  if (e.getCode() == KeyCode.W) {
                KeyPress.receiveKey("w");
                e.consume();
            }  if (e.getCode() == KeyCode.S) {
                KeyPress.receiveKey("s");
                e.consume();
            }
        });

        rootRef = root;
        duckRef = duck;
        obstaclesGroupRef = obstaclesGroup;

        root.getChildren().add(bottom);

        root.getChildren().addAll(obstaclesGroup, duck);


        stage.setScene(scene);
        stage.setTitle("Patikica v.0.1");
        stage.show();


        if (RUN_HEADLESS) {
            Thread headless = new Thread(Main::oldMain, "HeadlessLoop");
            headless.setDaemon(true);
            headless.start();
        }
    }
}