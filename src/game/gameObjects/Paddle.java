package game.gameObjects;

public class Paddle extends RectGameObject {
    private int speedX;

    public int getSpeedX() {
        return speedX;
    }

    public void setSpeedX(int speedX) {
        if (speedX < 0)
            throw new IllegalArgumentException("Speed must be non-negative!");
        this.speedX = speedX;
    }

    public Paddle(int x, int y, int width, int height) {
        super(x, y, width, height);
        setSpeedX(0);
    }

    public Paddle(int x, int y, int width, int height, int speedX) {
        super(x, y, width, height);
        setSpeedX(speedX);
    }
}
