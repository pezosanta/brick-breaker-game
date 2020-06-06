package game;

import game.gameObjects.PowerUpType;
import game.gameObjects.Wall;
import gui.MenuListener;
import gui.menuHandler;
import networking.WBMessage;
import networking.WallBreakerProtocol;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import static networking.WBMessage.MsgType.*;

public class Gameplay implements KeyListener, ActionListener {

    private boolean isStarted = false;
    private boolean isEnded = false;

    private boolean isMultiplayer = false;
    private boolean isServer = true;
    private WallBreakerProtocol wbProtocol;

    private int score = 0;
    private int paddleSpeed = 8;
    private int ballSpeedCounter = 0;
    private int paddleSizeCounter = 0;
    private int maxPowerUpLevel = 3;

    private Timer timer;
    private int delay = 16;

    private GameMap map;

    private Random random;

    private java.util.List<MenuListener> listeners = new ArrayList<MenuListener>();
    public void addListener(MenuListener listenerToAdd)
    {
        listeners.add(listenerToAdd);
    }

    public Gameplay(InputStream in){
        if (in != null) {
            map = GameMap.loadMapFromCSV(in);
        } else {
            map = new GameMap();
        }
        random = new Random();

        timer = new Timer(delay,this);
        timer.start();
    }

    public Gameplay() {
        this(null);
    }

    public Gameplay(boolean isServer, ObjectInputStream inStream, ObjectOutputStream outStream) {
        this();
        this.isMultiplayer = true;
        this.isServer = isServer;
        this.wbProtocol = new WallBreakerProtocol(inStream, outStream);

        if (isServer) {
            boolean success = wbProtocol.sendMessage(new WBMessage(MAP, map));
            if (!success) throw new RuntimeException("Failed to send map!");

            WBMessage msg = wbProtocol.readMessage();
            if (msg.msg != OK) {
                System.out.println(msg.toString());
                throw new RuntimeException("Client failed to respond properly!");
            }

        } else {
            WBMessage msg = wbProtocol.readMessage();
            if (msg == null || msg.msg != MAP) throw new RuntimeException("Bad initial message!");

            if (msg.payload != null) {
                map = (GameMap) msg.payload;
                boolean success = wbProtocol.sendMessage(new WBMessage(OK, null));
                if (!success) throw new RuntimeException("Failed to send OK message!");
            } else {
                throw new RuntimeException("Could not receive GameMap object from server! Aborting...");
            }
        }
    }

    public static Gameplay startFromCheckpoint() {
        Gameplay gameplay = new Gameplay();
        GameMap gameMap = GameMap.loadCheckpoint();
        if (gameMap != null) {
            gameplay.map = gameMap;
        }
        return gameplay;
    }

    public void stop() {
        timer.stop();
        if (!isMultiplayer) {
            map.createCheckpoint();
        } else {
            wbProtocol.sendMessage(new WBMessage(EXITED, null));
            wbProtocol.close();
            wbProtocol = null;
        }
    }

    public void render(Graphics g){
        //background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, map.panelWidth, map.panelHeight);

        //drawing map
        map.draw((Graphics2D) g);

        //scores
        g.setColor(Color.white);
        g.setFont(new Font("serif",Font.BOLD,25));
        g.drawString(""+score,map.panelWidth*4/5,25+2*map.wallWidth);

        if (isEnded) {
            g.setColor(Color.RED);
            if (map.getAvailableBricks() == 0) {
                g.setFont(new Font("serif", Font.BOLD, 30));
                g.drawString("You Won!", 260, 300);
            } else {
                g.setFont(new Font("serif",Font.BOLD,25));
                g.drawString("Game Over, Your score: " + String.valueOf(score),190,300);
            }

            g.setFont(new Font("serif",Font.BOLD,20));
            g.drawString("Press Enter to Restart",230,350);
        } else if (!isStarted) {
            g.setFont(new Font("serif",Font.BOLD,20));
            g.drawString("Press arrow to start",230,350);
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isStarted && !isEnded) {
            step();
        }

        for (MenuListener hl : listeners)
        {
            hl.gamePaintHandler();
        }
    }

    private void step() {
        if (isServer) {
            moveBallAndPaddle();
            boolean hitBottomWall = handleCollisions();
            if (hitBottomWall || map.getAvailableBricks() == 0) {
                isEnded = true;
            }
            if (isMultiplayer) {
                boolean success = wbProtocol.sendMessage(new WBMessage(MAP, map));
                WBMessage msg = wbProtocol.readMessage();
                if (msg.msg != OK) {
                    stop();
                    throw new RuntimeException("Client failed to read map payload.");
                }
            }
        } else { // We are multiplayer client; receive map, send ctrl
            while (wbProtocol.dataAvailable()) {
                WBMessage msg = wbProtocol.readMessage();
                if (msg == null) msg = new WBMessage(EXITED, null);
                switch (msg.msg) {
                    case MAP:
                        if (msg.payload == null) {
                            stop();
                            throw new RuntimeException("Received GameMap is null!");
                        }
                        map = (GameMap) msg.payload;
                        wbProtocol.sendMessage(new WBMessage(OK, null));
                        // TODO: send player control input
                        break;
                    case GAME_FINISHED:
                        // TODO: show highscore, stop game
                        stop();
                        isEnded = true;
                    default:
                    case EXITED:
                        stop();
                        isEnded = true;
                        // TODO: show message that connection was lost
                        System.out.println("Server has exited the game.");
                        listeners.forEach(hl -> hl.menuSwitchHandler(menuHandler.MENUSTATE.MAIN));
                }
            }
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

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_LEFT: {
                stopMove();
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            moveRight();
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            moveLeft();
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (isEnded) {
                map.reset();
                isStarted = false;
                isEnded = false;
                score = 0;

                for (MenuListener hl : listeners)
                {
                    hl.gamePaintHandler();
                }
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            for (MenuListener hl : listeners)
            {
                stop();
                hl.menuSwitchHandler(menuHandler.MENUSTATE.PAUSE);
            }
        }
    }

    public void stopMove(){
        if (isEnded == false){
            isStarted = true;
        }
        map.paddle.setSpeedX(0);
    }

    public void moveRight(){
        if (isEnded == false){
            isStarted = true;
        }
        if (map.paddle.getPosition().x < (map.panelWidth - map.paddle.getRect().width - map.wallWidth)) {
            map.paddle.setSpeedX(paddleSpeed);
        }
    }

    public void moveLeft(){
        if (isEnded == false){
            isStarted = true;
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
