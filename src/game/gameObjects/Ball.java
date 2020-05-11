package game.gameObjects;

import java.awt.*;

public class Ball extends CircularGameObject {
    private int speedX;
    private int speedY;

    public int getSpeedX() {
        return speedX;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public int getSpeedY() {
        return speedY;
    }

    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }

    public Ball(int x, int y, int radius) {
        super(x, y, radius);
        setSpeedX(0);
        setSpeedY(0);
    }

    public Ball(int x, int y, int radius, int speedX, int speedY) {
        super(x, y, radius);
        setSpeedX(speedX);
        setSpeedY(speedY);
    }

    public void handleCollisionWith(RectGameObject rectObj) {
        if (!this.collidesWith(rectObj)) {
            throw new IllegalArgumentException("Given argument does not collide with me pls!");
        }
        Rectangle brickRect = rectObj.getRect();

        int diffx = Math.abs(this.getPosition().x - brickRect.x);
        int diffy = Math.abs(this.getPosition().y - brickRect.y);

        if (diffx / brickRect.width >= diffy / brickRect.height) {
            this.setSpeedX( -1 * this.getSpeedX() );
        } else {
            this.setSpeedY( -1 * this.getSpeedY() );
        }
    }
}
