package hr.fer.projekt.temp;

import java.util.Scanner;
import javafx.scene.input.KeyEvent;

public class KeyPress{

    static private boolean Up, Down, Left, Right;
    KeyEvent event;

    public static void receiveKey(String code)
    {
            if (code.equals("a")) {
                Left = true;
            }
            if (code.equals("d")) {
                Right = true;
            }
            if (code.equals("w") && !Down) {
                Up = true;
            }
            if (code.equals("s") && !Up) {
                Down = true;
            }
    }

    public boolean isUp() {
        return Up;
    }

    public boolean isDown() {
        return Down;
    }

    public boolean isLeft() {
        return Left;
    }

    public boolean isRight() {
        return Right;
    }

    public void reset()
    {
        Up = false;
        Down = false;
        Left = false;
        Right = false;
    }
}
