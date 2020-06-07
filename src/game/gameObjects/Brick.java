package game.gameObjects;

import java.awt.*;
import java.io.Serializable;

public class Brick extends RectGameObject implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final Color[] DEFAULT_COLOR_LIST = {Color.BLACK, Color.GREEN, Color.BLUE, Color.RED, Color.ORANGE, Color.GRAY};
    public static final int MAX_ALLOWED_HEALTH = 5;

    private final int maxHealth;
    private int health;

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        if (health < 0) {
            throw new IllegalArgumentException("Health cannot be less than zero!");
        } else if (health > maxHealth) {
            throw new IllegalArgumentException("Health cannot be greater than maxHealth!");
        }
        this.health = health;
        setColor(DEFAULT_COLOR_LIST[health]);
    }

    public void decreaseHealth() {
        if (health > 0) {
            this.health -= 1;
        }
        setColor(DEFAULT_COLOR_LIST[health]);
    }

    public Brick(int x, int y, int width, int height, int health) {
        super(x, y, width, height);
        if (health > MAX_ALLOWED_HEALTH)
            throw new IllegalArgumentException("Brick health is bigger than maximum allowed health!");
        this.maxHealth = health;
        this.health = health;
        setColor(DEFAULT_COLOR_LIST[health]);
    }

    public Brick(Brick otherBrick) {
        super(otherBrick);
        this.health = otherBrick.health;
        this.maxHealth = otherBrick.maxHealth;
    }

    @Override
    public void draw(Graphics2D g) {
        if (health > 0)
            super.draw(g);
    }
}
