import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class GUI extends JFrame
{
    public static final int WIDTH   = 320;
    public static final int HEIGHT  = WIDTH / 12 * 9;
    public static final int SCALE   = 2;

    public final String TITLE       = "Brick Breaker Game";
    /*
    public final Color activeColor = Color.DARK_GRAY;
    public final Color inactiveColor = Color.BLACK;
    public  Color singlePlayerButtonColor = inactiveColor;
    public  Color multiplayerButtonColor = inactiveColor;
    public  Color settingsButtonColor = inactiveColor;
    public  Color helpButtonColor = inactiveColor;
    public  Color quitButtonColor = inactiveColor;

    /*
    public Rectangle playButton = new Rectangle(WIDTH / 2 + 120, 150, 100, 50);
    public Rectangle helpButton = new Rectangle(WIDTH / 2 + 120, 250, 100, 50);
    public Rectangle quitButton = new Rectangle(WIDTH / 2 + 120, 350, 100, 50);
    public Rectangle singlePlayerButton = new Rectangle(WIDTH / 2 + 20, 150, 300, 50);
    public Rectangle multiplayerButton  = new Rectangle(WIDTH / 2 + 20, 205, 300, 50);
    public Rectangle settingsButton     = new Rectangle(WIDTH / 2 + 20, 260, 300, 50);
    public Rectangle helpButton         = new Rectangle(WIDTH / 2 + 20, 315, 300, 50);
    public Rectangle quitButton         = new Rectangle(WIDTH / 2 + 20, 370, 300, 50);
    //private JFrame frame;
    private JPanel panel;
    */

    public GUI()
    {

        //panel = new JPanel();

        this.setTitle(TITLE);
        this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        this.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        this.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

        Menus menu = new Menus();
        this.add(menu);
        //this.setBackground(Color.gray);
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setLocation(450, 150);
        this.setAlwaysOnTop(true);
        this.setVisible(true);

        /*
        //frame = new JFrame(TITLE);

        this.add(panel);
        panel.addMouseMotionListener(this);
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setLocation(450, 150);
        this.setAlwaysOnTop(true);
        this.setVisible(true);
         */
    }
    /*
    public void paintComponent (Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        Font fnt0 = new Font("arial", Font.BOLD, 50);
        g.setFont(fnt0);
        g.setColor(Color.black);
        g.drawString("BRICK BREAKER GAME", WIDTH / 14, 100);

        g2d.setColor(singlePlayerButtonColor);
        g2d.fill(singlePlayerButton);
        g2d.setColor(multiplayerButtonColor);
        g2d.fill(multiplayerButton);
        g2d.setColor(settingsButtonColor);
        g2d.fill(settingsButton);
        g2d.setColor(helpButtonColor);
        g2d.fill(helpButton);
        g2d.setColor(quitButtonColor);
        g2d.fill(quitButton);

        Font fnt1 = new Font("arial", Font.BOLD, 30);
        g.setFont(fnt1);
        g.setColor(Color.white);
        g.drawString("Single player", singlePlayerButton.x + 54, singlePlayerButton.y + 35);
        g.drawString("Multiplayer", multiplayerButton.x + 66, multiplayerButton.y + 35);
        g.drawString("Settings", settingsButton.x + 90, settingsButton.y + 35);
        g.drawString("Help", helpButton.x + 118, helpButton.y + 35);
        g.drawString("Quit", quitButton.x + 118, quitButton.y + 35);
    }
    */
    public static void main(String[] args)
    {
        GUI gui = new GUI();
    }



}
