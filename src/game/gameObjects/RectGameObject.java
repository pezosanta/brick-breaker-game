package game.gameObjects;

import java.awt.*;
import java.io.Serializable;

public class RectGameObject extends GameObject implements Serializable {
    private static final long serialVersionUID = 1L;

    public Rectangle getRect() {
        return rect;
    }

    @Override
    public void setPosition(Point position) {
        super.setPosition(position);
        this.rect = new Rectangle(position, new Dimension(rect.width, rect.height));
    }

    public void setRect(int x, int y, int width, int height) {
        position = new Point(x, y);
        this.rect = new Rectangle(this.position, new Dimension(width, height));
    }

    protected Rectangle rect;

    public RectGameObject(int x, int y, int width, int height) {
        setRect(x, y, width, height);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(color);
        g.fill(rect);
    }

    @Override
    public boolean collidesWith(GameObject obj) {
        if (obj instanceof CircularGameObject) {
            return obj.collidesWith(this);
        } else if (obj instanceof RectGameObject) {
            RectGameObject rectObj = (RectGameObject) obj;
            return this.rect.intersects(rectObj.rect);
        }
        return true;
    }
}
