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
        setForceY(ForceY);
    }
    public void dive() {
        ForceY = -10;
        setForceY(ForceY);
    }
    public void goRight() {
        ForceX = 10;
        setForceX(ForceX);
    }
    public void goLeft() {
        ForceX = -10;
        setForceX(ForceX);
    }

    public void playerUpdate() {
        if (getForceX() > 0) {
            setX(getX() + getForceX());
            setForceX(getForceX() - 2);
        }
        if (getForceX() < 0) {
            setX(getX() + getForceX());
            setForceX(getForceX() + 2);
        }
        if (getForceY() > 0 || getY() != 0) {
            setY(getY() + getForceY());
            setForceY(getForceY() - 2);
        }
        if (getForceY() < 0 || getY() != 0) {
            setY(getY() + getForceY());
            setForceY(0);
        }
    }
}
