package temp;

import java.util.Scanner;

public class KeyPress{

    private boolean Up, Down, Left, Right;

    public void receiveKey(String code)
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
