
import static java.lang.Math.pow;

public class Player {
    private double x;
    private double y;
    private  double forceX;
    private  double forceY;
    private double distance;
    private int key;
    private long time;
    public long timeStart;

    public Player(double x, double y, double forceX, double forceY, double distance) {
        this.x = x;
        this.y = y;
        this.forceX = forceX;
        this.forceY = forceY;
        this.distance = distance;
    }

    public void step() {
        keyPressed();
        updatePlayer();
    }
    public void setKeyPressed(int key){
        this.key = key;
    }

    public void keyPressed(){

        if(key == 'd'){
            forceX += 10;
        }
        if(key == 'a'){
            forceX -= 10;
        }
        if(key == 'w'){
            forceY += 15;
            timeStart = System.currentTimeMillis();
        }
        else if(key == 's'){
            forceY -= 15;
            timeStart = System.currentTimeMillis();
        }
    }

    public void updatePlayer(){
        x += forceX;

        if (getForceY() >= 0) {
            y = (getForceY() * (System.currentTimeMillis() - timeStart) / 1000 + 0.001) - 0.5 * 9.81 * pow((double) (System.currentTimeMillis() - timeStart - 10) / 1000, 2);
            if (getY() < 0) {
                setForceY(0);
                setY(0);
            }
        }
        else if (getForceY() < 0) {
            y = (getForceY() * -1 * (System.currentTimeMillis() - timeStart) / 1000 + 0.001) - 0.5 * 9.81 * pow((double) (System.currentTimeMillis() - timeStart - 10) / 1000, 2);
            y *= -1;
            if (getY() > 0) {
                setForceY(0);
                setY(0);
            }
        }
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getForceX() {
        return forceX;
    }

    public void setForceX(double forceX) {
        this.forceX = forceX;
    }

    public double getForceY() {
        return forceY;
    }

    public void setForceY(double forceY) {
        this.forceY = forceY;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
