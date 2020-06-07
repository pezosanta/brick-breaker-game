package game.gameObjects;

import java.awt.*;

public class Brick extends RectGameObject {
    public static final Color[] DEFAULT_COLOR_LIST = {Color.BLACK, Color.GREEN, Color.BLUE, Color.RED, Color.ORANGE, Color.GRAY};
    public static final int MAX_ALLOWED_HEALTH = 35;

    private int maxHealth = 40;
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
        } else if (health > MAX_ALLOWED_HEALTH) {
            throw new IllegalArgumentException("Health cannot be greater than maxHealth!");
        }
        this.health = health;
        this.maxHealth = health;
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

    @Override
    public void draw(Graphics2D g) {
        if (health > 0)
            super.draw(g);
    }
}
