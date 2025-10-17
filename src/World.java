public class World implements Runnable {

    Thread gameThread;
    public int FPS = 60;

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
        }
    }

    public void update() {

    }

    public void repaint() {

    }
}
