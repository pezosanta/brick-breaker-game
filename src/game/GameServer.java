package game;

import game.gameObjects.PowerUpType;
import game.gameObjects.Wall;
import gui.MenuListener;
import gui.menuHandler;
import networking.GameStateMessage;
import networking.WallBreakerProtocol;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class GameServer {

    private boolean play = false;
    private boolean firstRun = true;
    private boolean isMultiplayer = false;

    private int score = 0;
    private int paddleSpeed = 8;
    private int ballSpeedCounter = 0;
    private int paddleSizeCounter = 0;
    private int maxPowerUpLevel = 3;

    private int delay = 32;
    private Timer timer = new Timer(delay, this::serverLoop);

    private GameMap map;

    private Random random = new Random();

    private List<ObjectInputStream> clientsOutput = new ArrayList<>(2);
    private List<ObjectOutputStream> clientsInput = new ArrayList<>(2);

    public void sendTestMsg() {
        try {
            clientsInput.get(0).writeObject("Hello Marcika ez a szerver!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTestMsg() {
        try {
            String msg = (String) clientsOutput.get(0).readObject();
            return msg;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public GameServer(InputStream in){
        if (in != null) {
            map = GameMap.loadMapFromCSV(in);
        } else {
            map = new GameMap();
        }
    }

    public GameServer() {
        this(null);
    }

    public static GameServer startFromCheckpoint() {
        GameServer gameServer = new GameServer();
        GameMap gameMap = GameMap.loadCheckpoint();
        if (gameMap != null) {
            gameServer.map = gameMap;
        }
        return gameServer;
    }

    public void addClient(ObjectInputStream in, ObjectOutputStream out) {
        clientsOutput.add(in);
        clientsInput.add(out);
    }

    public GameClient createLocalClient() {
        PipedOutputStream clientOutPipe = new PipedOutputStream();
        PipedInputStream serverInPipe = new PipedInputStream();
        PipedOutputStream serverOutPipe = new PipedOutputStream();
        PipedInputStream clientInPipe = new PipedInputStream();

        try {
            serverInPipe.connect(clientOutPipe);
            clientInPipe.connect(serverOutPipe);

            ObjectOutputStream clientOut = new ObjectOutputStream(clientOutPipe);
            ObjectInputStream serverIn = new ObjectInputStream(serverInPipe);
            ObjectOutputStream serverOut = new ObjectOutputStream(serverOutPipe);
            ObjectInputStream clientIn = new ObjectInputStream(clientInPipe);

            this.addClient(serverIn, serverOut);

            return new GameClient(clientIn, clientOut);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void startServer() {
        timer.start();
    }

    public void stopServer() {
        timer.stop();
        if (!isMultiplayer) {
            map.createCheckpoint();
        }
    }

    private void serverLoop(ActionEvent actionEvent) {
        System.out.println("Server loop");
        // Receive client messages
        Queue<GameClient.KeyEventExt> keyEvents = new LinkedList<>();
        try {
            while (clientsOutput.get(0).available() > 0) {
                GameClient.KeyEventExt keyEvent = (GameClient.KeyEventExt) clientsOutput.get(0).readObject();
                keyEvents.add(keyEvent);
                System.out.println("Server received keyevent code: " + keyEvent.keyCode + " keyevent type: " + keyEvent.keyEventType);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Process client messages
        while (!keyEvents.isEmpty()) {
            GameClient.KeyEventExt keyEvent = keyEvents.poll();
            switch (keyEvent.keyCode) {
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_RIGHT:
                    if (keyEvent.keyEventType == GameClient.KeyEventType.RELEASED) {
                        stopMove();
                    } else {
                        if (keyEvent.keyCode == KeyEvent.VK_LEFT) moveLeft();
                        else moveRight();
                    }
                case KeyEvent.VK_ENTER:
                    if (!play) {
                        map.reset();
                        play = true;
                        score = 0;
                    }
                default:
                case KeyEvent.VK_ESCAPE:
                    /*for (MenuListener hl : listeners) {
                        stop();
                        hl.menuSwitchHandler(menuHandler.MENUSTATE.PAUSE);
                    }*/
            }
        }

        // Simulate game
        moveBallAndPaddle();
        boolean hitBottomWall = handleCollisions();
        if (hitBottomWall || map.getAvailableBricks() == 0) {
            play = false;
            firstRun = false;
        }

        // Send map to clients
        try {
            clientsInput.get(0).writeObject(GameStateMessage.MAP_PAYLOAD);
            clientsInput.get(0).writeObject(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void moveBallAndPaddle() {
        // Move ball
        Point ballpos = map.ball.getPosition();
        ballpos.x += map.ball.getSpeedX();
        ballpos.y += map.ball.getSpeedY();
        map.ball.setPosition(ballpos);

        Point paddlepos = map.paddle.getPosition();
        paddlepos.x += map.paddle.getSpeedX();
        if (paddlepos.x > (map.panelWidth - map.paddle.getRect().width-map.wallWidth)) {
            stopMove();
            paddlepos.x = map.panelWidth - map.paddle.getRect().width-map.wallWidth;
        } else if (paddlepos.x < map.wallWidth) {
            stopMove();
            paddlepos.x = map.wallWidth;
        }
        map.paddle.setPosition(paddlepos);
    }

    private boolean handleCollisions() {
        // Paddle
        if (map.ball.collidesWith(map.paddle)) {
            map.ball.handleCollisionWithPaddle(map.paddle);
        }

        // Walls
        for (Wall wall : map.walls) {
            if (map.ball.collidesWith(wall)) {
                map.ball.handleCollisionWith(wall);
            }
        }
        if (map.ball.collidesWith(map.walls[map.walls.length - 1])) {
            return true;
        }

        // Bricks
        A: for (int i = 0; i < map.bricks.length; i++){
            for (int j = 0; j < map.bricks[i].length; j++){
                if (map.bricks[i][j].getHealth() > 0 && map.ball.collidesWith(map.bricks[i][j])) {
                    map.ball.handleCollisionWith(map.bricks[i][j]);
                    map.bricks[i][j].decreaseHealth();
                    score += 5;
                    if (map.bricks[i][j].getHealth() == 0) {
                        if (random.nextFloat() > 0.7) {
                            int powUpIndex = random.nextInt(PowerUpType.values().length);
                            applyPowerUp(PowerUpType.values()[powUpIndex]);
                        }
                    }
                    break A;
                }
            }
        }

        return false;
    }

    public void stopMove(){
        if (firstRun == true){
            play = true;
        }
        map.paddle.setSpeedX(0);
    }

    public void moveRight(){
        if (firstRun == true){
            play = true;
        }
        if (map.paddle.getPosition().x < (map.panelWidth - map.paddle.getRect().width - map.wallWidth)) {
            map.paddle.setSpeedX(paddleSpeed);
        }
    }

    public void moveLeft(){
        if (firstRun == true){
            play = true;
        }
        if (map.paddle.getPosition().x > (map.wallWidth)) {
            map.paddle.setSpeedX(-paddleSpeed);
        }
    }

    private void applyPowerUp(PowerUpType powerup) {
        switch (powerup) {
            case None:
                break;

            case FasterBall:
                if (ballSpeedCounter < maxPowerUpLevel) {
                    delay /= 2;
                    timer.setDelay(delay);
                    paddleSpeed /= 2;
                    ballSpeedCounter++;
                }
                break;

            case SlowerBall:
                if (ballSpeedCounter > -maxPowerUpLevel) {
                    delay *= 2;
                    timer.setDelay(delay);
                    paddleSpeed *= 2;
                    ballSpeedCounter--;
                }
                break;

            case SmallRacket: {
                if(paddleSizeCounter > -maxPowerUpLevel) {
                    Rectangle rect = map.paddle.getRect();
                    rect.width /= 2;
                    map.paddle.setRect(rect.x, rect.y, rect.width, rect.height);
                    paddleSizeCounter--;
                }
                break;
            }
            case BigRacket: {
                if(paddleSizeCounter < maxPowerUpLevel) {
                    Rectangle rect = map.paddle.getRect();
                    rect.width *= 2;
                    map.paddle.setRect(rect.x, rect.y, rect.width, rect.height);
                    paddleSizeCounter++;
                }
                break;
            }
        }
        System.out.println("Powerup " + powerup.toString() + " applied.");
    }

}
