package game.gameObjects;

import java.awt.*;
import java.io.Serializable;

public class CircularGameObject extends GameObject implements Serializable {
    private static final long serialVersionUID = 1L;

    protected int radius;

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        if (radius < 1)
            throw new IllegalArgumentException("Radius should be a positive integer!");
        this.radius = radius;
    }

    public CircularGameObject(CircularGameObject otherCircle) {
        super(otherCircle);
        this.radius = otherCircle.radius;
    }

    public CircularGameObject(int x, int y, int radius) {
        setPosition(new Point(x, y));
        setRadius(radius);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(color);
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

            // source: http://www.jeffreythompson.org/collision-detection/circle-rect.php
            // temporary variables to set edges for testing
            float testX = this.position.x;
            float testY = this.position.y;

            // which edge is closest?
            if (this.position.x < rect.x) {
                testX = rect.x;      // test left edge
            }
            else if (this.position.x > rect.x+rect.width) {
                testX = rect.x+rect.width;   // right edge
            }
            if (this.position.y < rect.y) {
                testY = rect.y;      // top edge
            }
            else if (this.position.y > rect.y+rect.height) {
                testY = rect.y+rect.height;   // bottom edge
            }

            // get distance from closest edges
            float distX = this.position.x-testX;
            float distY = this.position.y-testY;
            float distance = (float) Math.sqrt( (distX*distX) + (distY*distY) );

            // if the distance is less than the radius, collision!
            return distance <= this.radius;
        }
        return true;
    }
}
