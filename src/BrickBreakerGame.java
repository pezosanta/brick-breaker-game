import game.Gameplay;
import gui.GUI;

import javax.swing.*;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;

public class BrickBreakerGame {
    private GUI gui;

    public BrickBreakerGame() {
        gui = new GUI();
    }

    private void showGUI() {
        gui.setVisible(true);
    }

    public void startGame(String filename) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream fileStream = classloader.getResourceAsStream(filename);

        JFrame obj = new JFrame();
        Gameplay gamePlay = new Gameplay(fileStream);
        obj.setBounds(10,10,700,600);
        obj.setTitle("Brick Breaker Game");
        obj.setResizable(false);
        obj.setVisible(true);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.add(gamePlay);
    }

    public ArrayList<String> getAvailableMaps() {

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        URL mapsPath = classloader.getResource("maps");

        return new ArrayList<>(Arrays.asList(new File(mapsPath.getPath()).list()));
    }

    public static void main(String[] args) {
        BrickBreakerGame game = new BrickBreakerGame();
        ArrayList<String> maps = game.getAvailableMaps();
        System.out.println(maps.size());
        game.startGame(maps.get(1));

        //game.showGUI();
    }
}
