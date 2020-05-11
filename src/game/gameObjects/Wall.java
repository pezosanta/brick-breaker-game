package game.gameObjects;

import java.awt.*;

public class Wall extends RectGameObject {
    public static final Color DEFAULT_COLOR = Color.WHITE;

    public Wall(int x, int y, int width, int height) {
        super(x, y, width, height);
        color = DEFAULT_COLOR;
    }

    public Wall(int x, int y, int width, int height, Color color) {
        super(x, y, width, height);
        this.color = color;
    }
}
