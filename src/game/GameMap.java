package game;

import game.gameObjects.*;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class GameMap {
    public static final int ROWS = 4;
    public static final int COLS = 8;

    static final int brickWidth = 560/COLS;
    static final int brickHeight = 160/ROWS;
    static final int panelWidth = 700;
    static final int panelHeight = 600;
    static final int wallWidth = 3;
    static final int paddleWidth = 100;
    static final int paddleHeight = 8;
    static final int ballRadius = 20;

    Brick[][] bricks;
    Wall[] walls;
    Ball ball;
    Paddle paddle;

    final int[][] startingHealth;

    public GameMap() {
        this(null);
    }

    public GameMap(int[][] healths) {
        if (healths == null) {
            // Create array of ones
            healths = new int[ROWS][COLS];
            for (int[] health : healths) {
                Arrays.fill(health, 1);
            }
        }
        startingHealth = healths;

        this.reset();
    }

    public void reset() {
        bricks = new Brick[ROWS][COLS];
        for (int i=0; i<bricks.length; i++) {
            for (int j=0; j< bricks[i].length; j++) {
                bricks[i][j] = new Brick(j * brickWidth + 80, i * brickHeight + 50,
                        brickWidth*9/10, brickHeight*9/10, startingHealth[i][j]);
                bricks[i][j].setColor(Color.GREEN);
            }
        }

        //borders
        walls = new Wall[4];
        walls[0] = new Wall(0, 0, wallWidth, panelHeight);
        walls[1] = new Wall(0, 0, panelWidth, wallWidth);
        walls[2] = new Wall(panelWidth-wallWidth, 0, wallWidth, panelHeight);
        walls[3] = new Wall(0, panelHeight-wallWidth, panelWidth, wallWidth);
        walls[walls.length - 1].setColor(Color.RED);

        //ball
        ball = new Ball(120, 350, ballRadius);
        ball.setSpeedX(-3);
        ball.setSpeedY(-4);
        ball.setColor(Color.MAGENTA);

        //the paddle
        paddle = new Paddle(panelWidth/2 - paddleWidth/2, panelHeight-2*paddleHeight, paddleWidth, paddleHeight);
        paddle.setColor(Color.BLUE);
    }

    ArrayList<GameObject> getAllGameObjects() {
        ArrayList<GameObject> objects = new ArrayList<>(Arrays.asList(walls));
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
