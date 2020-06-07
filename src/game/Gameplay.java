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
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import static networking.WBMessage.MsgType.*;

public class Gameplay implements KeyListener, ActionListener {

    private boolean isStarted = false;
    private boolean isEnded = false;
    private boolean isReady = false;

    private boolean isMultiplayer = false;
    private boolean isServer = true;
    private boolean isClientReady = false;
    private WallBreakerProtocol wbProtocol;

    private int score = 0;
    private int paddleSpeed = 8;
    private int ballSpeedCounter = 0;
    private int paddleSizeCounter = 0;
    private int maxPowerUpLevel = 3;

    private Boolean isLoopExecuting = false;
    private Timer timer;
    private int delay = 32;

    private GameMap map;
    private Queue<WBMessage> myEvents = new ConcurrentLinkedQueue<>();

    private Random random = new Random();

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

        timer = new Timer(delay,this);
        timer.start();
    }

    public Gameplay() {
        this(null);
    }

    public Gameplay(boolean isServer, InputStream inStream, OutputStream outStream) {
        this.isServer = isServer;
        timer = new Timer(delay,this);
        isMultiplayer = true;

        wbProtocol = new WallBreakerProtocol(inStream, outStream);

        if (isServer) {
            map = new GameMap();
            boolean success = wbProtocol.sendMessage(new WBMessage(MAP, map));
            if (!success) throw new RuntimeException("Failed to send map!");

            WBMessage msg = wbProtocol.readMessage();
            if (msg == null || msg.msg != OK) {
                System.out.println(msg.toString());
                throw new RuntimeException("Client failed to respond properly!");
            }

        } else {
            map = null;
            WBMessage msg = wbProtocol.readMessage();
            if (msg == null || msg.msg != MAP) throw new RuntimeException("Bad initial message!");

            if (msg.map != null) {
                map = msg.map;
                boolean success = wbProtocol.sendMessage(new WBMessage(OK, null));
                if (!success) throw new RuntimeException("Failed to send OK message!");
            } else {
                throw new RuntimeException("Could not receive GameMap object from server! Aborting...");
            }
        }

        System.out.println("Multiplayer Gameplay initialized as: " + this.isServer);
        timer.start();
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
        } else if (wbProtocol != null) {
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
        synchronized (isLoopExecuting) {
            // Do server things: process own key events, process client messages, step gameplay, send map to client (and maybe control)
            if (isServer) {
                // Process own key events
                while (!myEvents.isEmpty()) {
                    WBMessage msg = myEvents.poll();
                    switch (msg.msg) {
                        case PLAYER_RELEASED:
                            stopMove();
                            break;
                        case PLAYER_PRESSED:
                            isReady = true;
                            switch ((int) msg.keyCode) {
                                case KeyEvent.VK_LEFT:
                                    moveLeft();
                                    break;
                                case KeyEvent.VK_RIGHT:
                                    moveRight();
                                    break;
                            }
                    }
                }

                // TODO: process client events if multiplayer
                if (isMultiplayer && !isEnded) {
                    // Process client messages
                    while (wbProtocol.isDataAvailable()) {
                        System.out.println("Client message received!");
                        WBMessage msg = wbProtocol.readMessage();
                        if (msg == null) msg = new WBMessage(EXITED, null);
                        switch (msg.msg) {
                            case PLAYER_PRESSED:
                                isClientReady = true;
                                switch ((int) msg.keyCode) {
                                    case KeyEvent.VK_LEFT:
                                        moveLeft();
                                        break;
                                    case KeyEvent.VK_RIGHT:
                                        moveRight();
                                        break;
                                }
                                break;
                            case PLAYER_RELEASED:
                                stopMove();
                                break;
                            case PLAYER_READY:
                                isClientReady = true;
                                break;
                            case EXITED:
                                stop();
                                System.out.println("Client has exited the game.");
                                listeners.forEach(hl -> hl.menuSwitchHandler(menuHandler.MENUSTATE.MAIN));
                                return;
                        }
                    }
                }

                // If players are ready: start game!
                if (isReady && !isStarted) {
                    if (!isMultiplayer) {
                        isStarted = true;
                    } else if (isMultiplayer && isClientReady) {
                        isStarted = true;
                        wbProtocol.sendMessage(new WBMessage(GAME_STARTED, null));
                    }
                }

                // Update game map if play is started but not ended
                if (isStarted && !isEnded) {
                    step();

                    // Send updated game map to client
                    if (isMultiplayer) {
                        WBMessage msg = new WBMessage(MAP, map);
                        wbProtocol.sendMessage(msg);
                        System.out.println("sent msg id = " + msg.id);
                        System.out.println("sent ballpos = " + ((GameMap)msg.map).ball.getPosition());
                        if (isEnded) { // This was the last step we took
                            wbProtocol.sendMessage(new WBMessage(GAME_FINISHED, null));
                        }
                    }
                }
            }

            // We are multiplayer client; receive map from server, send user inputs to server
            if (isMultiplayer && !isServer && !isEnded) {
                // Process server messages
                while (wbProtocol.isDataAvailable()) {
                    WBMessage msg = wbProtocol.readMessage();
                    if (msg == null) msg = new WBMessage(EXITED, null);
                    switch (msg.msg) {
                        case MAP:
                            if (msg.map == null) {
                                stop();
                                throw new RuntimeException("Received GameMap is null!");
                            }
                            map = msg.map;
                            System.out.println("CLIENT: Map received! ID is: " + msg.id);
                            System.out.println("received ballpos = " + msg.map.ball.getPosition());
                            break;
                        case GAME_STARTED:
                            isStarted = true;
                            break;
                        case GAME_FINISHED:
                            timer.stop();
                            isEnded = true;
                            break;
                        default:
                        case EXITED:
                            stop();
                            isEnded = true;
                            System.out.println("Server has exited the game.");
                            listeners.forEach(hl -> hl.menuSwitchHandler(menuHandler.MENUSTATE.MAIN));
                            return;
                    }
                }

                // Send player control input to server
                while (!myEvents.isEmpty()) {
                    System.out.println("Client event sent!");
                    wbProtocol.sendMessage(myEvents.poll());
                }
            }

            // Draw game (calls this.render implicitly)
            listeners.forEach(menuListener -> menuListener.gamePaintHandler());
        }
    }

    private void step() {
        moveBallAndPaddle();
        boolean hitBottomWall = handleCollisions();
        if (hitBottomWall || map.getAvailableBricks() == 0) {
            isEnded = true;
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
            case KeyEvent.VK_LEFT:
                myEvents.add(new WBMessage(PLAYER_RELEASED, e.getKeyCode()));
                break;
            default:
                break;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            myEvents.add(new WBMessage(PLAYER_PRESSED, e.getKeyCode()));
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            myEvents.add(new WBMessage(PLAYER_PRESSED, e.getKeyCode()));
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (isEnded) {
                if (!isMultiplayer) {
                    map.reset();
                    isReady = false;
                    isStarted = false;
                    isEnded = false;
                    score = 0;

                    for (MenuListener hl : listeners) {
                        hl.gamePaintHandler();
                    }
                } else {
                    // If in multiplayer, game ended and enter pressed: switch to main menu
                    listeners.forEach(menuListener -> menuListener.menuSwitchHandler(menuHandler.MENUSTATE.MAIN));
                }
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            stop();
            if (!isMultiplayer) {
                listeners.forEach(menuListener -> menuListener.menuSwitchHandler(menuHandler.MENUSTATE.PAUSE));
            } else {
                listeners.forEach(menuListener -> menuListener.menuSwitchHandler(menuHandler.MENUSTATE.MAIN));
            }
        }
    }

    public void stopMove(){
        map.paddle.setSpeedX(0);
    }

    public void moveRight(){
        if (map.paddle.getPosition().x < (map.panelWidth - map.paddle.getRect().width - map.wallWidth)) {
            map.paddle.setSpeedX(paddleSpeed);
        }
    }

    public void moveLeft(){
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
