package game.gameObjects;

import java.awt.*;
import java.io.Serializable;

public abstract class GameObject implements Serializable {
    private static final long serialVersionUID = 1L;

    protected Point position = new Point(0, 0);
    protected Color color = Color.GRAY;

    public GameObject() {}

    public GameObject(GameObject otherObj) {
        this.position = new Point(otherObj.position);
        this.color = otherObj.getColor();
    }

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
