package game.gameObjects;

import java.awt.*;

public class Paddle extends RectGameObject {
    public static final Color DEFAULT_COLOR = Color.BLUE;
    private int speedX;

    public int getSpeedX() {
        return speedX;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public Paddle(int x, int y, int width, int height) {
        this(x, y, width, height, 0);
    }

    public Paddle(int x, int y, int width, int height, int speedX) {
        super(x, y, width, height);
        setSpeedX(speedX);
        setColor(DEFAULT_COLOR);
    }
}
