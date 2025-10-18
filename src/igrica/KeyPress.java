package igrica;

import java.util.Scanner;

public class KeyPress{

    private boolean Up, Down, Left, Right;

    public void receiveKey()
    {
        Scanner sc = new Scanner(System.in);

        while(true)
        {
            int code = sc.next().charAt(0);

            if (code == 'a') {
                Left = true;
            }
            if (code == 'd') {
                Right = true;
            }
            if (code == 'w' && !Down) {
                Up = true;
            }
            if (code == 's' && !Up) {
                Down = true;
            }
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
}
