package game.gameObjects;

import java.awt.*;

public class Brick extends RectGameObject {
    public static final int MAX_ALLOWED_HEALTH = 10;

    private final int maxHealth;
    private int health;

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public boolean decreaseHealth() {
        if (health < 1)
            return true;
        else {
            this.health -= 1;
            return false;
        }
    }

    public Brick(int x, int y, int width, int height, int health) {
        super(x, y, width, height);
        if (health > MAX_ALLOWED_HEALTH)
            throw new IllegalArgumentException("Brick health is bigger than maximum allowed health!");
        this.maxHealth = health;
        this.health = health;
    }

    @Override
    public void draw(Graphics2D g) {
        if (health > 0)
            super.draw(g);
    }
}
