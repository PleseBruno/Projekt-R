package igrica;

public class Player extends Entity {

    public Player(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    private double ForceX = 0;
    private double ForceY = 0;

    public double getForceX() {
        return ForceX;
    }

    public void setForceX(double forceX) {
        ForceX = forceX;
    }

    public double getForceY() {
        return ForceY;
    }

    public void setForceY(double forceY) {
        ForceY = forceY;
    }

    public void jump() {
        ForceY = 10;
        while (getForceY() > 0 || getY() != 0) {
            setY(getY() + getForceY());
            setForceY(getForceY() - 1);
        }
    }
    public void dive() {
        ForceY = -10;
        while (getForceY() < 0 || getY() != 0) {
            setY(getY() - getForceY());
            setForceY(getForceY() + 1);
        }
    }
    public void goRight() {
        ForceX = 10;
        while (getForceX() != 0) {
            setX(getX() + getForceX());
            setForceX(getForceX() - 1);
        }
    }
    public void goLeft() {
        ForceX = -10;
        while (getForceX() != 0) {
            setX(getX() - getForceX());
            setForceX(getForceX() + 1);
        }
    }
}
