package game.gameObjects;

import java.awt.*;

public class Ball extends CircularGameObject {
    private int speedX;
    private int speedY;

    public int getSpeedX() {
        return speedX;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public int getSpeedY() {
        return speedY;
    }

    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }

    public Ball(int x, int y, int radius) {
        super(x, y, radius);
        setSpeedX(0);
        setSpeedY(0);
    }

    public Ball(int x, int y, int radius, int speedX, int speedY) {
        super(x, y, radius);
        setSpeedX(speedX);
        setSpeedY(speedY);
    }

    public void handleCollisionWith(RectGameObject rectObj) {
        if (!this.collidesWith(rectObj)) {
            throw new IllegalArgumentException("Given argument does not collide with me pls!");
        }
        Rectangle brickRect = rectObj.getRect();

        int diffx = Math.abs(this.getPosition().x - (brickRect.x + brickRect.width / 2));
        int diffy = Math.abs(this.getPosition().y - (brickRect.y + brickRect.height / 2));

        if (diffx / brickRect.width >= diffy / brickRect.height) { // dx/dy >= width/height -> alpha < alpha0
            this.setSpeedX( -1 * this.getSpeedX() );
        } else {
            this.setSpeedY( -1 * this.getSpeedY() );
        }
    }

    public void handleCollisionWithPaddle(Paddle paddle) {
        if (!this.collidesWith(paddle)) {
            throw new IllegalArgumentException("Given argument does not collide with me pls!");
        }
        Rectangle brickRect = paddle.getRect();

        if ((this.getPosition().x - paddle.getPosition().x) <= (brickRect.width*0.2)){
            if(this.getSpeedX() > 0){
                this.setSpeedX(5);
            }
            else {
                this.setSpeedX(-5);
            }
            this.setSpeedY(-1);
        }
        if (((this.getPosition().x - paddle.getPosition().x) <= (brickRect.width*0.4)) && ((this.getPosition().x - paddle.getPosition().x) > (brickRect.width*0.2))){
            if(this.getSpeedX() > 0){
                this.setSpeedX(4);
            }
            else {
                this.setSpeedX(-4);
            }
            this.setSpeedY(-3);
        }
        if (((this.getPosition().x - paddle.getPosition().x) <= (brickRect.width*0.6)) && ((this.getPosition().x - paddle.getPosition().x) > (brickRect.width*0.4))){
            if(this.getSpeedX() > 0){
                this.setSpeedX(3);
            }
            else{
                this.setSpeedX(-3);
            }
            this.setSpeedY(-4);
        }
        if (((this.getPosition().x - paddle.getPosition().x) <= (brickRect.width*0.8)) && ((this.getPosition().x - paddle.getPosition().x) > (brickRect.width*0.6))){
            if(this.getSpeedX() > 0){
                this.setSpeedX(4);
            }
            else{
                this.setSpeedX(-4);
            }
            this.setSpeedY(-3);
        }
        if (((this.getPosition().x - paddle.getPosition().x) <= (brickRect.width)) && ((this.getPosition().x - paddle.getPosition().x) > (brickRect.width*0.8))){
            if(this.getSpeedX() > 0){
                this.setSpeedX(5);
            }
            else{
                this.setSpeedX(-5);
            }
            this.setSpeedY(-1);
        }
    }
}
