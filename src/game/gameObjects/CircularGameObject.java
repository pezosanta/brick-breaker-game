package game.gameObjects;

import java.awt.*;

public class CircularGameObject extends GameObject {
    protected int radius;

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        if (radius < 1)
            throw new IllegalArgumentException("Radius should be a positive integer!");
        this.radius = radius;
    }

    public CircularGameObject(int x, int y, int radius) {
        setPosition(new Point(x, y));
        setRadius(radius);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(this.color);
        g.fillOval(position.x, position.y, radius, radius);
    }

    @Override
    public boolean collidesWith(GameObject obj) {
        if (obj instanceof CircularGameObject) {
            CircularGameObject circleObj = (CircularGameObject) obj;
            int radiusSum = this.radius + circleObj.getRadius();
            double distance = this.position.distance(circleObj.position);

            return radiusSum > distance;

        } else if (obj instanceof RectGameObject) {
            RectGameObject rectObj = (RectGameObject) obj;
            Rectangle rect = rectObj.getRect();
            int circleDistanceX = Math.abs(this.position.x - rect.width/2 - rect.x);
            int circleDistanceY = Math.abs(this.position.y - rect.height/2 - rect.y);

            if (circleDistanceX > (rect.width/2 + this.radius)) { return false; }
            if (circleDistanceY > (rect.height/2 + this.radius)) { return false; }

            if (circleDistanceX <= (rect.width/2)) { return true; }
            if (circleDistanceY <= (rect.height/2)) { return true; }

            float cornerDistance_sq = (circleDistanceX - rect.width/2)^2 +
                    (circleDistanceY - rect.height/2)^2;

            return (cornerDistance_sq <= (this.radius^2));
        }
        return true;
    }
}
