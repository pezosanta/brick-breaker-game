package game;

import game.gameObjects.Brick;
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

public class Gameplay implements KeyListener, ActionListener {
    final int panelWidth = 700;
    final int panelHeight = 600;
    final int wallWidth = 3;
    final int paddleWidth = 100;
    final int paddleHeight = 8;
    final int ballRadius = 20;

    private boolean play = false;
    private boolean firstRun = true;
    private int score = 0;

    private Timer timer;
    private int delay = 8;

    private GameMap baseMap;
    private GameMap map;

    private java.util.List<MenuListener> listeners = new ArrayList<MenuListener>();
    public void addListener(MenuListener listenerToAdd)
    {
        listeners.add(listenerToAdd);
    }

    private void reset() {

        map = baseMap;

        //borders
        map.walls[0].setRect(0, 0, wallWidth, panelHeight);
        map.walls[1].setRect(0, 0, panelWidth, wallWidth);
        map.walls[2].setRect(panelWidth-wallWidth, 0, wallWidth, panelHeight);
        map.walls[3].setRect(0, panelHeight-wallWidth, panelWidth, wallWidth);

        //the paddle
        map.paddle.setRect(panelWidth/2 - paddleWidth/2, panelHeight-2*paddleHeight, paddleWidth, paddleHeight);

        //ball
        map.ball.setRadius(ballRadius);
        map.ball.setPosition(new Point(120, 350));
        map.ball.setSpeedX(-1);
        map.ball.setSpeedY(-2);
    }

    public Gameplay(InputStream in){
        baseMap = GameMap.loadMapFromCSV(in);
        reset();

        timer = new Timer(delay,this);
        timer.start();
    }

    public Gameplay(){
        baseMap = new GameMap();
        reset();

        timer = new Timer(delay,this);
        timer.start();
    }

    public void render(Graphics g){
        //background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, panelWidth, panelHeight);

        //drawing map
        map.draw((Graphics2D) g);

        //scores
        g.setColor(Color.white);
        g.setFont(new Font("serif",Font.BOLD,25));
        g.drawString(""+score,panelWidth*4/5,25+2*wallWidth);

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
        boolean hitBottomWall = handleCollisions();
        if (hitBottomWall || map.getAvailableBricks() == 0) {
            play = false;
            firstRun = false;
        } else {
            moveBallAndPaddle();
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
        if (paddlepos.x > (panelWidth - paddleWidth)) {
            stopMove();
            paddlepos.x = panelWidth - paddleWidth;
        } else if (paddlepos.x < wallWidth) {
            stopMove();
            paddlepos.x = wallWidth;
        }
        map.paddle.setPosition(paddlepos);
    }

    private boolean handleCollisions() {
        // Paddle
        if (map.ball.collidesWith(map.paddle)) {
            map.ball.handleCollisionWith(map.paddle);
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
                reset();
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
        if (map.paddle.getPosition().x < (panelWidth - paddleWidth)) {
            map.paddle.setSpeedX(10);
        }
    }

    public void moveLeft(){
        play = true;
        if (map.paddle.getPosition().x > (wallWidth)) {
            map.paddle.setSpeedX(-10);
        }
    }

}
