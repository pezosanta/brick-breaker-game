package game.gameObjects;

public class Ball extends CircularGameObject {
    private int speedX;
    private int speedY;

    public int getSpeedX() {
        return speedX;
    }

    public void setSpeedX(int speedX) {
        if (speedX < 0)
            throw new IllegalArgumentException("Speed must be non-negative!");
        this.speedX = speedX;
    }

    public int getSpeedY() {
        return speedY;
    }

    public void setSpeedY(int speedY) {
        if (speedX < 0)
            throw new IllegalArgumentException("Speed must be non-negative!");
        this.speedY = speedY;
    }

    public Ball(int x, int y, int radius) {
        super(x, y, radius);
    }

    public Ball(int x, int y, int radius, int speedX, int speedY) {
        super(x, y, radius);
        setSpeedX(speedX);
        setSpeedY(speedY);
    }
}
