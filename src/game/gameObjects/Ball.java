package game.gameObjects;

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
}
