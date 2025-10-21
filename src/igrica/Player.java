package igrica;

import java.util.List;

public class Player extends Entity {

    public Player(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    private final double SCALER = 10;
    
    private double moveX = 0;
    private double moveY = 0;
    private boolean jumped = false;
    private boolean dived = false;
    private final double gravity = 1.25 / (SCALER * SCALER);
    private final double friction = 1 / (SCALER * SCALER);
    private final double buoyancy = 0.75 / (SCALER * SCALER);

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

    public double getMoveX() {
        return moveX;
    }

    public void setMoveX(double moveX) {
        moveX = moveX;
    }

    public double getMoveY() {
        return moveY;
    }

    public void setMoveY(double moveY) {
        moveY = moveY;
    }

    public void jump() {
        moveY = 10 / SCALER;
        setJumped(true);
        setMoveY(moveY);
    }

    public void dive() {
        moveY = -10 / SCALER;
        setDived(true);
        setMoveY(moveY);
    }

    public void goRight() {
        moveX = 3 / SCALER;
        setMoveX(moveX);
    }

    public void goLeft() {
        moveX = -3 / SCALER;
        setMoveX(moveX);
    }

    public boolean isJumping(){
        return (getMoveY() > 0 && getY() >= 0) || (getMoveY() < 0 && getY() >= 0);
    }

    public boolean isDiving() {
        return (getMoveY() < 0 && getY() <= 0) || (getMoveY() > 0 && getY() <= 0);
    }

    public void playerUpdate(List<Obstacle> obstacles) {

        if (getMoveX() > 0) {
            if (getMoveX() - friction < 0) {
                setMoveX(0);
            }
            else {
                setX(getX() + getMoveX());
                setMoveX(getMoveX() - friction);
            }
        }
        if (getMoveX() < 0) {
            if (getMoveX() + friction > 0) {
                setMoveX(0);
            }
            else {
                setX(getX() + getMoveX());
                setMoveX(getMoveX() + friction);
            }
        }
        if (isJumped()) {
            if (getY() + getMoveY() < 0) {
                setY(0);
                setMoveY(0);
            }
            else {
                setY(getY() + getMoveY());
                setMoveY(getMoveY() - gravity);
            }
        }
        if (isDived()) {
            if  (getY() + getMoveY() > 0) {
                setY(0);
                setMoveY(0);
            }
            else {
                setY(getY() + getMoveY());
                setMoveY(getMoveY() + buoyancy);
            }
        }

        //resetira isJumped i isDived

        if (getY() == 0 && (isJumped() || isDived())) {
            setMoveY(0);
            setJumped(false);
            setDived(false);
        }

        for (Obstacle obstacle : obstacles) {
            int touch = touching(obstacle);
            switch (touch) {
                case 1:
                {
                    setMoveY(0);
                    setY(obstacle.getY() + getHeight());
                    break;
                }
                case 2:
                {
                    setMoveY(0);
                    setY(obstacle.getY() - obstacle.getHeight());
                    break;
                }
                case 3:
                {
                    setMoveX(0);
                    setX(obstacle.getX() - getWidth());
                    break;
                }
            }
        }
    }

    public int touching(Obstacle obstacle){
        if (getY()  > (obstacle.getY() - obstacle.getHeight())
                && (getY() - getHeight()) < obstacle.getY()
                && (getX() + getWidth()) >  obstacle.getX()
                && getX() < (obstacle.getX() + obstacle.getWidth())
        ){
            if (getY() > obstacle.getY()) return 1;
            if ((getY() - getHeight()) < obstacle.getY() - obstacle.getHeight())  return 2;
            if (getX() <  obstacle.getX()) return 3;
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

