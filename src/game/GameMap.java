package game;

import game.gameObjects.*;

import java.awt.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class GameMap {
    public static final int ROWS = 4;
    public static final int COLS = 8;

    public final Brick[][] bricks;
    public final Wall[] walls;
    public final Ball ball;
    public final Paddle paddle;

    public int brickWidth;
    public int brickHeight;

    public GameMap() {
        brickWidth = 560/COLS;
        brickHeight = 160/ROWS;

        bricks = new Brick[ROWS][COLS];
        for (int i=0; i<bricks.length; i++) {
            for (int j=0; j< bricks[i].length; j++) {
                bricks[i][j] = new Brick(j * brickWidth + 80, i * brickHeight + 50, brickWidth*9/10, brickHeight*9/10, 1);
                bricks[i][j].setColor(Color.GREEN);
            }
        }

        walls = new Wall[4];
        for (int i=0; i<walls.length; i++) {
            walls[i] = new Wall(0,0,0,0);
        }
        walls[walls.length - 1].setColor(Color.RED);

        ball = new Ball(0, 0, 1);
        ball.setColor(Color.MAGENTA);

        paddle = new Paddle(0, 0, 0, 0);
        paddle.setColor(Color.BLUE);
    }

    public ArrayList<GameObject> getAllGameObjects() {
        ArrayList<GameObject> objects = new ArrayList<GameObject>();
        objects.addAll(Arrays.asList(walls));
        for (Brick[] brickRow : bricks)
            objects.addAll(Arrays.asList(brickRow));
        objects.add(ball);
        objects.add(paddle);
        return objects;
    }

    public int getAvailableBricks() {
        int available = 0;
        for (Brick[] brick : bricks) {
            for (Brick value : brick) {
                if (value.getHealth() > 0)
                    available += 1;
            }
        }
        return available;
    }

    public void draw(Graphics2D g) {
        for (GameObject obj : getAllGameObjects()) {
            obj.draw(g);
        }
    }

    public static GameMap loadMapFromCSV(InputStream in) {
        int rows = 0;
        int cols = 0;
        ArrayList<Integer> values = new ArrayList<>();

        Scanner scanner = new Scanner(in);
        while (scanner.hasNextLine()) {
            rows += 1;
            String line = scanner.nextLine();
            String[] parts = line.split(",|;");
            cols = parts.length;
            for (String s : parts) {
                values.add(Integer.valueOf(s));
                System.out.print(s + "|");
            }
        }
        scanner.close();

        if (rows != ROWS || cols != COLS) {
            System.err.println("Something's wrong man in CSV loading!" + rows + cols);
            return new GameMap();
        }

        GameMap mapgen = new GameMap();
        for (int i=0; i<mapgen.bricks.length; i++) {
            for (int j=0; j< mapgen.bricks[i].length; j++) {
                mapgen.bricks[i][j].setHealth(values.get(i*cols + j));
            }
        }
        return mapgen;
    }
}
