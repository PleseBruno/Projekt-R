package igrica;

public class Player extends Entity {

    public Player(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    private double ForceX = 0;
    private double ForceY = 0;
    private boolean jumped = false;
    private boolean dived = false;
    private final double gravity = 1;
    private final double buoyancy = 1.5;

    public double getGravity() {
        return gravity;
    }

    public double getBuoyancy() {
        return buoyancy;
    }

    public boolean isJumped() {
        return jumped;
    }

    public void setJumped(boolean jumped) {
        this.jumped = jumped;
    }

    public boolean isDived() {
        return dived;
    }

    public void setDived(boolean dived) {
        this.dived = dived;
    }

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
        ForceX = 3;
        setForceX(ForceX);
    }

    public void goLeft() {
        ForceX = -3;
        setForceX(ForceX);
    }

    public void playerUpdate() {
        if (getForceX() > 0) {
            setX(getX() + getForceX());
            setForceX(getForceX() - 1);
        }
        if (getForceX() < 0) {
            setX(getX() + getForceX());
            setForceX(getForceX() + 1);
        }
        if (isJumped()) {
            if (getY() + getForceY() < 0) {
                setY(0);
                setForceY(0);
            }
            else {
                setY(getY() + getForceY());
                setForceY(getForceY() - gravity);
            }
        }
        if (isDived()) {
            if  (getY() + getForceY() > 0) {
                setY(0);
                setForceY(0);
            }
            else {
                setY(getY() + getForceY());
                setForceY(getForceY() + buoyancy);
            }
        }

        //resetira isJumped i isDived

        if (getY() == 0 && (isJumped() || isDived())) {
            setForceY(0);
            setJumped(false);
            setDived(false);
        }

        for (Obstacle obstacle : obstacles) {
            int touch = touching(obstacle);
            System.out.println("Touching: " + touch);
            switch (touch) {
                case 1:
                {
                    setForceY(0);
                    setY(obstacle.getY() + getHeight());
                }

                case 2:
                {
                    setForceY(0);
                    setY(obstacle.getY() - obstacle.getHeight());
                }

                case 3:
                {
                    setForceX(0);
                    setX(obstacle.getX() - getWidth());
                }
            }
        }
    }

    public int touching(Obstacle obstacle){
        if (getY()  >= (obstacle.getY() - obstacle.getHeight())
                && (getY() - getHeight()) <= obstacle.getY()
                && (getX() + getWidth()) >=  obstacle.getX()
                && getX() <= (obstacle.getX() + obstacle.getWidth())
        ){
            if (getY() >= obstacle.getY()) return 1;
            if ((getY() - getHeight()) <= obstacle.getY() - obstacle.getHeight())  return 2;
            if (getX() <=  obstacle.getX()) return 3;
        }
        return 0;
    }

    public boolean isDead() {
        if (getX() + getWidth() < -98)
            return true;
        else
            return false;
    }
}

