package igrica;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        World world = new World();
        world.startGameThread();
    }
}