package igrica;

public abstract class Entity {

    private double x;
    private double y;

    public Entity(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }
}
