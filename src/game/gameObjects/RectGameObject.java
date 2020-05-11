package game.gameObjects;

import java.awt.*;

public class RectGameObject extends GameObject {
    public Rectangle getRect() {
        return rect;
    }

    public void setRect(int x, int y, int width, int height) {
        setPosition(new Point(x, y));
        this.rect = new Rectangle(this.position, new Dimension(width, height));
    }

    protected Rectangle rect;

    public RectGameObject(int x, int y, int width, int height) {
        setRect(x, y, width, height);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(this.color);
        g.fill(rect);
    }

    @Override
    public boolean collidesWith(GameObject obj) {
        if (obj instanceof CircularGameObject) {
            return obj.collidesWith(this);
        } else if (obj instanceof RectGameObject) {
            RectGameObject rectObj = (RectGameObject) obj;
            return rect.intersects(rectObj.rect);
        }
        return true;
    }
}
