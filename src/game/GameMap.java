package game;

import game.gameObjects.*;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class GameMap implements Serializable {
    private static final long serialVersionUID = 1L;

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
    Paddle paddle2;

    final int[][] startingHealth;

    public GameMap() {
        this((int[][]) null);
    }

    public GameMap(int[][] healths) {
        Random r = new Random();
        if (healths == null) {
            // Create array of ones
            healths = new int[ROWS][COLS];

            for (int i = 0 ; i < ROWS; i++) {
                for (int j = 0; j < COLS; j++){
                    float strength = r.nextFloat();
                    if (strength < 0.5){
                        healths[i][j] = 1;
                    }
                    else if (strength < 0.8333){
                        healths[i][j] = 2;
                    }
                    else{
                        healths[i][j] = 3;
                    }
                }
            }
        }
        startingHealth = healths;

        this.reset();
    }

    public GameMap(GameMap otherMap) {
        int[][] startingHealth_temp;
        this.ball = new Ball(otherMap.ball);
        this.paddle = new Paddle(otherMap.paddle);
        this.paddle2 = new Paddle(otherMap.paddle2);

        // Deep copy bricks
        this.bricks = new Brick[otherMap.bricks.length][];
        for (int i=0; i<otherMap.bricks.length; i++) {
            this.bricks[i] = new Brick[otherMap.bricks[i].length];
            for (int j=0; j<otherMap.bricks[i].length; j++) {
                this.bricks[i][j] = new Brick(otherMap.bricks[i][j]);
            }
        }

        // Deep copy walls
        this.walls = new Wall[otherMap.walls.length];
        for (int i=0; i<otherMap.walls.length; i++) {
            this.walls[i] = new Wall(otherMap.walls[i]);
        }

        // Deep copy startingHealth ints
        startingHealth_temp = new int[otherMap.startingHealth.length][];
        for (int i=0; i<otherMap.startingHealth.length; i++) {
            startingHealth_temp[i] = new int[otherMap.startingHealth[i].length];
            startingHealth_temp[i] = Arrays.copyOf(otherMap.startingHealth[i], otherMap.startingHealth[i].length);
        }
        this.startingHealth = startingHealth_temp;
    }

    public void reset() {
        bricks = new Brick[ROWS][COLS];
        for (int i=0; i<bricks.length; i++) {
            for (int j=0; j< bricks[i].length; j++) {
                bricks[i][j] = new Brick(j * brickWidth + 80, i * brickHeight + 50,
                        brickWidth*9/10, brickHeight*9/10, startingHealth[i][j]);
            }
        }

        //borders
        walls = new Wall[4];
        walls[0] = new Wall(0, 0, wallWidth, panelHeight);
        walls[1] = new Wall(0, 0, panelWidth, wallWidth);
        walls[2] = new Wall(panelWidth-wallWidth, 0, wallWidth, panelHeight);
        walls[3] = new Wall(0, panelHeight-wallWidth, panelWidth, wallWidth);

        //ball
        ball = new Ball(120, 350, ballRadius);
        ball.setSpeedX(-3);
        ball.setSpeedY(-4);

        //the paddles
        paddle = new Paddle(3*panelWidth/4 - paddleWidth/2, panelHeight-2*paddleHeight, paddleWidth, paddleHeight);
        paddle.setColor(Color.BLUE);
        paddle2 = new Paddle(0,0,0,0);
    }

    public void addSecondPaddle() {
        paddle2 = new Paddle(panelWidth/4 - paddleWidth/2, panelHeight-2*paddleHeight, paddleWidth, paddleHeight);
        paddle2.setColor(Color.GRAY);
    }

    public void createCheckpoint() {
        // TODO: create checkpoint from serialized object
        //Saving of object in a file
        try {
            FileOutputStream file = new FileOutputStream(".gamemap_ckpt.ser");
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(this);

            out.close();
            file.close();

            System.out.println("Object has been serialized");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GameMap loadCheckpoint() {
        GameMap gamemap = null;
        // Deserialization
        try {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(".gamemap_ckpt.ser");
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            gamemap = (GameMap)in.readObject();

            in.close();
            file.close();

            System.out.println("Object has been deserialized ");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return gamemap;
    }

    ArrayList<GameObject> getAllGameObjects() {
        ArrayList<GameObject> objects = new ArrayList<>(Arrays.asList(walls));
        for (Brick[] brickRow : bricks)
            objects.addAll(Arrays.asList(brickRow));
        objects.add(ball);
        objects.add(paddle);
        objects.add(paddle2);
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

        int[][] healths = new int[ROWS][COLS];
        for (int i=0; i<healths.length; i++) {
            for (int j=0; j< healths[i].length; j++) {
                healths[i][j] = values.get(i*cols + j);
            }
        }

        return new GameMap(healths);
    }
}
