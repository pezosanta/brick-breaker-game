package game.gameObjects;

import java.awt.*;
import java.io.Serializable;

public class Wall extends RectGameObject implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final Color DEFAULT_COLOR = Color.WHITE;

    public Wall(int x, int y, int width, int height) {
        super(x, y, width, height);
        setColor(DEFAULT_COLOR);
    }

    public Wall(int x, int y, int width, int height, Color color) {
        super(x, y, width, height);
        this.color = color;
    }

    public Wall(Wall otherWall) {
        super(otherWall);
    }
}
