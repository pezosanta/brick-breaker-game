package game;

import game.gameObjects.Brick;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;

    private Timer timer;
    private int delay = 8;

    private GameMap map;

    public Gameplay(){
        map = new GameMap();

        //borders
        map.walls[0].setRect(0, 0, 3, 592);
        map.walls[1].setRect(0, 0, 692, 3);
        map.walls[2].setRect(681, 0, 3, 592);
        //map.walls[3].setRect(681, 0, 3, 592);

        //the paddle
        map.paddle.setRect(320, 550,100,8);

        //ball
        map.ball.setRadius(20);
        map.ball.setPosition(new Point(120, 350));
        map.ball.setSpeedX(-1);
        map.ball.setSpeedY(-2);

        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay,this);
        timer.start();
    }

    public void paint(Graphics g){
        //background
        g.setColor(Color.BLACK);
        g.fillRect(1,1,692,592);

        //drawing map
        map.draw((Graphics2D) g);

        //scores
        g.setColor(Color.white);
        g.setFont(new Font("serif",Font.BOLD,25));
        g.drawString(""+score,590,30);

        if (map.getAvailableBricks() <= 0) {
            play = false;
            map.ball.setPosition(new Point(0, 0));
            g.setColor(Color.RED);
            g.setFont(new Font("serif",Font.BOLD,30));
            g.drawString("You Won!",260,300);

            g.setFont(new Font("serif",Font.BOLD,20));
            g.drawString("Press Enter to Restart",230,350);
        }

        if (map.ball.getPosition().y + map.ball.getRadius() > 570){
            play = false;
            map.ball.setSpeedX(0);
            map.ball.setSpeedY(0);

            g.setColor(Color.RED);
            g.setFont(new Font("serif",Font.BOLD,25));
            g.drawString("Game Over, Your score: ",190,300);
            g.drawString(String.valueOf(score), 460, 300);

            g.setFont(new Font("serif",Font.BOLD,25));
            g.drawString("Press Enter to Restart",230,350);
        }

        g.dispose();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(play){
            if (map.ball.collidesWith(map.paddle)) {
                map.ball.collidesWith(map.paddle);
                map.ball.setSpeedY( -1 * map.ball.getSpeedY() );
            }
            //logic with bricks
            A: for (int i = 0; i < map.bricks.length; i++){
                for (int j = 0; j < map.bricks[i].length; j++){
                    if (map.bricks[i][j].getHealth() > 0){
                        Rectangle brickRect = map.bricks[i][j].getRect();
                        Brick cbrick = map.bricks[i][j];

                        if (map.ball.collidesWith(map.bricks[i][j])) {
                            map.bricks[i][j].decreaseHealth();
                            score += 5;

                            /*int diffx = Math.abs(map.ball.getPosition().x - brickRect.x);
                            int diffy = Math.abs(map.ball.getPosition().y - brickRect.y);

                            if (diffx / brickRect.width >= diffy / brickRect.height) {
                                map.ball.setSpeedX( -1 * map.ball.getSpeedX() );
                            } else {
                                map.ball.setSpeedY( -1 * map.ball.getSpeedY() );
                            }*/

                            if (map.ball.getPosition().x + 19 <= brickRect.x ||
                                    map.ball.getPosition().x + 1 >= brickRect.x + brickRect.width) {
                                map.ball.setSpeedX( -1 * map.ball.getSpeedX() );
                            } else {
                                map.ball.setSpeedY( -1 * map.ball.getSpeedY() );
                            }
                            break A;
                        }
                    }
                }
            }

            Point ballpos = map.ball.getPosition();
            ballpos.x += map.ball.getSpeedX();
            ballpos.y += map.ball.getSpeedY();
            map.ball.setPosition(ballpos);
            if (map.ball.collidesWith(map.walls[0])){
                map.ball.setSpeedX( -1 * map.ball.getSpeedX() );
            }
            if(map.ball.collidesWith(map.walls[1])){
                map.ball.setSpeedY( -1 * map.ball.getSpeedY() );
            }
            if(map.ball.collidesWith(map.walls[2])){
                map.ball.setSpeedX( -1 * map.ball.getSpeedX() );
            }
        }

        repaint();
        timer.start();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (map.paddle.getPosition().x >= 600){
                map.paddle.setPosition(new Point(600, map.paddle.getPosition().y));
            } else {
                moveRight();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            if (map.paddle.getPosition().x <= 10){
                map.paddle.setPosition(new Point(10, map.paddle.getPosition().y));
            } else {
                moveLeft();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER){
            if (!play) {
                play = true;
                map = new GameMap();
                map.ball.setPosition(new Point(120, 350));
                map.ball.setSpeedX(-1);
                map.ball.setSpeedY(-2);
                map.paddle.setPosition(new Point(310, map.paddle.getPosition().y));
                score = 0;
                repaint();
            }
        }
    }
    public void moveRight(){
        play = true;
        Point paddlepos = map.paddle.getPosition();
        paddlepos.x += 20;
        map.paddle.setPosition(paddlepos);
    }

    public void moveLeft(){
        play = true;
        Point paddlepos = map.paddle.getPosition();
        paddlepos.x -= 20;
        map.paddle.setPosition(paddlepos);
    }



}
