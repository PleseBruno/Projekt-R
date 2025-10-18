package igrica;

public class World implements Runnable {

    Thread gameThread;
    Thread playerThread;
    public int FPS = 3;
    KeyPress keyPress = new KeyPress();

    Player player = new Player(0,0,5,5);

    public World() {
        playerThread = new Thread(() -> keyPress.receiveKey());
        playerThread.start();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {



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

    public void update() {
        if (keyPress.isDown()) {
            player.dive();
        }
        if (keyPress.isUp()) {
            player.jump();
        }
        if (keyPress.isLeft()) {
            player.goLeft();
        }
        if (keyPress.isRight()) {
            player.goRight();
        }
    }

    public void repaint() {
        System.out.println(player.getY());
    }
}
