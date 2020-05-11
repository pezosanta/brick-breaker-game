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
        g.fillOval(position.x - radius/2, position.y - radius/2, radius, radius);
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

            int distanceX = Math.abs(this.position.x - (rect.x + rect.width/2));
            int distanceY = Math.abs(this.position.y - (rect.y + rect.height/2));

            if (distanceX > (rect.width/2 + this.radius)) {
                return false;
            }
            if (distanceY > (rect.height/2 + this.radius)) {
                return false;
            }

            if (distanceX <= (rect.width/2)) {
                return true;
            }
            if (distanceY <= (rect.height/2)) {
                return true;
            }

            double cornerDistance_sq = Math.pow((distanceX - rect.width/2.0), 2) +
                    Math.pow(distanceY - rect.height/2.0, 2);

            return (cornerDistance_sq <= (this.radius^2));
        }
        return true;
    }
}
