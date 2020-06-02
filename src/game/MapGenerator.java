package game;

import java.awt.*;
import java.util.Random;

enum PowerUpType {
    None,
    FasterBall,
    SlowerBall,
    SmallRacket,
    BigRacket,
}

class MapElement {
    public int strength;
    public PowerUpType powerup = PowerUpType.None;

    public MapElement(int strength) {
        this.strength = strength;
    }
}

public class MapGenerator {
    public MapElement map[][];
    public int brickWidth;
    public int brickHeight;

    public MapGenerator(int row, int col) {
        Random r = new Random();
        map = new MapElement[row][col];
        PowerUpType[] powerups = PowerUpType.values();
        for (int i = 0; i < map.length; i++){
            for (int j = 0; j < map[0].length; j++){
                int strength = r.nextInt(3) + 1;
                MapElement e = new MapElement(strength);

                if (r.nextFloat() > 0.0)
                    e.powerup = powerups[r.nextInt(powerups.length)];

                map[i][j] = e;
            }

        }
        brickWidth = 540/col;
        brickHeight = 150/row;
    }
    public void draw(Graphics2D g) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                int strength = map[i][j].strength;
                if (strength > 0) {
                    if (strength == 1) {
                        g.setColor(Color.white);
                    }
                    else if (strength == 2){
                        g.setColor(Color.blue);
                    }
                    else if (strength == 3){
                        g.setColor(Color.red);
                    }
                    g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);

                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.black);
                    g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                }

            }
        }
    }
    public MapElement hitBrick(int row, int col) {
        map[row][col].strength--;
        return map[row][col];
    }
}
