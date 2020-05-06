import java.awt.Dimension;
import javax.swing.*;

public class GUI extends JFrame
{
    public static final int WIDTH   = 320;
    public static final int HEIGHT  = WIDTH / 12 * 9;
    public static final int SCALE   = 2;

    public final String TITLE       = "Brick Breaker Game";

    public GUI()
    {
        this.setTitle(TITLE);
        this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        this.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        this.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

        Menus menu = new Menus();
        this.add(menu);

        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setLocation(450, 150);
        this.setAlwaysOnTop(true);
        this.setVisible(true);
    }

    public static void main(String[] args)
    {
        GUI gui = new GUI();
    }
}
