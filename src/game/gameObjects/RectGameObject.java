package game.gameObjects;

import java.awt.*;

public class RectGameObject extends GameObject {
    public Rectangle getRect() {
        return rect;
    }

    protected final Rectangle rect;

    public RectGameObject(int x, int y, int width, int height) {
        setPosition(new Point(x, y));
        this.rect = new Rectangle(this.position, new Dimension(width, height));
    }

    @Override
    public void draw(Graphics2D g) {
        g.draw(rect);
    }

    @Override
    public boolean collidesWith(GameObject obj) {
        if (obj.getClass().isInstance(CircularGameObject.class)) {
            return obj.collidesWith(this);
        } else if (obj.getClass().isInstance((RectGameObject.class))) {
            RectGameObject rectObj = (RectGameObject) obj;
            return rect.intersects(rectObj.rect);
        }
        return true;
    }
}
