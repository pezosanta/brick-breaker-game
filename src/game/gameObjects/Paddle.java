package game.gameObjects;

public class Paddle extends RectGameObject {
    private int speedX;

    public int getSpeedX() {
        return speedX;
    }

    public void setSpeedX(int speedX) {
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
