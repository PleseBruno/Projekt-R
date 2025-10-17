import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        Player p1 = new Player(0, 0, 0, 0, 0);

        int key;
        while ((key = System.in.read()) != 'q') {
            p1.setKeyPressed(key);
            p1.step();
        }
    }
}