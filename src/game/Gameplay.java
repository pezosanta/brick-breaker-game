package game;

import game.gameObjects.PowerUpType;
import game.gameObjects.Wall;
import gui.MenuListener;
import gui.menuHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class Gameplay implements KeyListener, ActionListener {

    private boolean play = false;
    private boolean firstRun = true;
    private int score = 0;

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
        map = GameMap.loadMapFromCSV(in);
        random = new Random();

        timer = new Timer(delay,this);
        timer.start();
    }

    public Gameplay(){
        map = new GameMap();
        random = new Random();

        timer = new Timer(delay,this);
        timer.start();
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

        if (!play && map.getAvailableBricks() <= 0) {
            g.setColor(Color.RED);
            g.setFont(new Font("serif",Font.BOLD,30));
            g.drawString("You Won!",260,300);

            g.setFont(new Font("serif",Font.BOLD,20));
            g.drawString("Press Enter to Restart",230,350);
        } else if (!play && !firstRun){
            g.setColor(Color.RED);
            g.setFont(new Font("serif",Font.BOLD,25));
            g.drawString("Game Over, Your score: " + String.valueOf(score),190,300);

            g.setFont(new Font("serif",Font.BOLD,25));
            g.drawString("Press Enter to Restart",230,350);
        }

        g.dispose();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (play) {
            step();
        }

        for (MenuListener hl : listeners)
        {
            hl.gamePaintHandler();
        }

        if (play) {
            //timer.start();
        }
    }

    private void step() {
        moveBallAndPaddle();
        boolean hitBottomWall = handleCollisions();
        if (hitBottomWall || map.getAvailableBricks() == 0) {
            play = false;
            firstRun = false;
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
        if (paddlepos.x > (map.panelWidth - map.paddleWidth)) {
            stopMove();
            paddlepos.x = map.panelWidth - map.paddleWidth;
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
            if (!play) {
                map.reset();
                play = true;
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
                timer.stop();
                hl.menuSwitchHandler(menuHandler.MENUSTATE.PAUSE);
            }
        }
    }

    public void stopMove(){
        play = true;
        map.paddle.setSpeedX(0);
    }

    public void moveRight(){
        play = true;
        if (map.paddle.getPosition().x < (map.panelWidth - map.paddle.getRect().width - map.wallWidth)) {
            map.paddle.setSpeedX(10);
        }
    }

    public void moveLeft(){
        play = true;
        if (map.paddle.getPosition().x > (map.wallWidth)) {
            map.paddle.setSpeedX(-10);
        }
    }

    private void applyPowerUp(PowerUpType powerup) {
        switch (powerup) {
            case None:
                break;

            case FasterBall:
                delay /= 2;
                timer.setDelay(delay);
                break;

            case SlowerBall:
                delay *= 2;
                timer.setDelay(delay);
                break;

            case SmallRacket: {
                Rectangle rect = map.paddle.getRect();
                rect.width /= 2;
                map.paddle.setRect(rect.x, rect.y, rect.width, rect.height);
                break;
            }
            case BigRacket: {
                Rectangle rect = map.paddle.getRect();
                rect.width *= 2;
                map.paddle.setRect(rect.x, rect.y, rect.width, rect.height);
                break;
            }
        }
        System.out.println("Powerup " + powerup.toString() + " applied.");
    }

}
