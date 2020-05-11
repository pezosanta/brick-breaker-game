package game;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame obj = new JFrame();
        Gameplay gamePlay = new Gameplay();
        gamePlay.setPreferredSize(new Dimension(700, 600));
        obj.add(gamePlay);
        obj.pack();
        obj.setTitle("Brick Breaker Game");
        obj.setResizable(false);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.setVisible(true);
    }
}
