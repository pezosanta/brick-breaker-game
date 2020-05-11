package gui;

import java.awt.Dimension;
import javax.swing.*;

public class GUI extends JFrame
{
    public static final int WIDTH   = 640;
    public static final int HEIGHT  = 480;

    public final String TITLE       = "Brick Breaker Game";

    public GUI()
    {
        this.setTitle(TITLE);
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        this.setMinimumSize(new Dimension(WIDTH, HEIGHT));

        menuHandler menu = new menuHandler();

        this.setContentPane(menu);

        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setLocation(450, 150);
        this.setAlwaysOnTop(true);
    }

    public static void main(String[] args)
    {
        GUI gui = new GUI();
        gui.setVisible(true);
    }
}
