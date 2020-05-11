package game.gameObjects;

import java.awt.*;
import java.awt.geom.Point2D;

public abstract class GameObject {
    protected Point position;
    protected Color color = Color.GRAY;

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public abstract void draw(Graphics2D g);
    public abstract boolean collidesWith(GameObject obj);
}
