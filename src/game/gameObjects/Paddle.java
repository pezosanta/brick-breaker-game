package game.gameObjects;

import java.awt.*;
import java.io.Serializable;

public class Paddle extends RectGameObject implements Serializable {
    private static final long serialVersionUID = 1L;

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

    public Paddle(Paddle otherPaddle) {
        super(otherPaddle);
        this.speedX = otherPaddle.speedX;
    }
}
