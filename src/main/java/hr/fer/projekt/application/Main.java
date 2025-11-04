//package hr.fer.projekt.application;
//
//import hr.fer.projekt.application.World;
//import hr.fer.projekt.entities.Obstacle;
//import hr.fer.projekt.entities.Player;
//import hr.fer.projekt.temp.KeyPress;
//import javafx.application.Platform;
//import javafx.scene.input.KeyCode;
//
//import javafx.application.Application;
//import javafx.scene.Group;
//import javafx.scene.Scene;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Rectangle;
//import javafx.stage.Stage;
//
//import java.util.List;
//
//public class Main extends Application {
//
//    public static boolean RUN_HEADLESS = true;
//
//    private static Group rootRef;
//    private static Rectangle duckRef;
//    private static Group obstaclesGroupRef;
//
//    public static void repaint(Player player, List<Obstacle> obstacles) {
//        System.out.println("player: " + player.getX() + " " + player.getY() + " " + player.getMoveX() + " " + player.getMoveY());
//        int counter = 0;
//        for (Obstacle obstacle : obstacles){
//            System.out.println("obstacle " + counter + " " + obstacle.getX() + " " + obstacle.getY());
//            counter++;
//        }
//
//        if (rootRef != null && duckRef != null && obstaclesGroupRef != null) {
//            Platform.runLater(() -> {
//
//                duckRef.setWidth(player.getWidth());
//                duckRef.setHeight(player.getHeight());
//
//                obstaclesGroupRef.getChildren().clear();
//                for (Obstacle obstacle : obstacles) {
//
//                    Rectangle r = new Rectangle(obstacle.getWidth(), obstacle.getHeight(), Color.SADDLEBROWN);
//                    r.setX(obstacle.getX());
//                    r.setY(obstacle.getY());
//
//                    obstaclesGroupRef.getChildren().add(r);
//                }
//            });
//        }
//    }
//
//    public static void oldMain(){
//        World world = new World();
//        int FPS = 30;
//        double refreshPerFps = 10;
//        double speed = 1 / refreshPerFps;
//
//        KeyPress keyPress = new KeyPress();
//
//        double drawInterval = 1E9/(FPS * refreshPerFps);
//        double nextDrawTime = drawInterval + System.nanoTime();
//
//        while (!world.getPlayer().isDead()) {
//
//            repaint(world.getPlayer(), world.getObstacles());
//
//            try {
//                double remainingTime = nextDrawTime - System.nanoTime();
//
//                if (remainingTime < 0) {
//                    remainingTime = 0;
//                }
//
//                Thread.sleep((long) (remainingTime / 1E6));
//
//                nextDrawTime = drawInterval + System.nanoTime();
//
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
//
//    @Override
//    public void start(Stage stage) throws Exception {
//        Group root = new Group();
//        Scene scene = new Scene(root, Color.LIGHTBLUE);
//        Rectangle bottom = new Rectangle(600, 200, Color.DODGERBLUE);
//        bottom.setY(210);
//        stage.setWidth(600);
//        stage.setHeight(400);
//        stage.setResizable(false);
//
//        World uiWorld = new World();
//        Player uiPlayer = uiWorld.getPlayer();
//
//        Rectangle duck = new Rectangle(uiPlayer.getWidth(), uiPlayer.getHeight(), Color.BLACK);
//        duck.setX(uiPlayer.getX());
//        duck.setY(uiPlayer.getY());
//
//        Group obstaclesGroup = new Group();
//
//        for (Obstacle obs : uiWorld.getObstacles()) {
//            Rectangle rectangle = new Rectangle(obs.getWidth(), obs.getHeight(), Color.BLUE);
//            rectangle.setX(obs.getX());
//            rectangle.setY(obs.getY());
//            obstaclesGroup.getChildren().add(rectangle);
//        }
//        scene.setOnKeyPressed(event -> {
//            if (event.getCode() == KeyCode.A) {
//                KeyPress.receiveKey("a");
//            }  if (event.getCode() == KeyCode.D) {
//                KeyPress.receiveKey("d");
//            }  if (event.getCode() == KeyCode.W) {
//                KeyPress.receiveKey("w");
//                event.consume();
//            }  if (event.getCode() == KeyCode.S) {
//                KeyPress.receiveKey("s");
//                event.consume();
//            }
//        });
//
//        rootRef = root;
//        duckRef = duck;
//        obstaclesGroupRef = obstaclesGroup;
//
//        root.getChildren().add(bottom);
//
//        root.getChildren().addAll(obstaclesGroup, duck);
//
//        stage.setScene(scene);
//        stage.setTitle("Patkica v.0.2");
//        stage.show();
//
//        if (RUN_HEADLESS) {
//            Thread headless = new Thread(Main::oldMain, "HeadlessLoop");
//            headless.setDaemon(true);
//            headless.start();
//        }
//    }
//}