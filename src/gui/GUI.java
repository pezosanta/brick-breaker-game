package gui;

import java.awt.Dimension;
import javax.swing.*;

public class GUI extends JFrame
{
    public static final int WIDTH   = 700; //640;
    public static final int HEIGHT  = 600; //480;

    public final String TITLE       = "Brick Breaker Game";

    public GUI()
    {
        this.setTitle(TITLE);

        menuHandler menu = new menuHandler();
        menu.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        this.setContentPane(menu);
        this.pack();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setLocation(450, 150);
    }

    public static void main(String[] args)
    {
        GUI gui = new GUI();
        gui.setVisible(true);
    }
}
