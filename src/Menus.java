import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;

public class Menus extends JPanel implements MouseMotionListener, MouseListener
{
    public static final int WIDTH           = 320;
    public static final int HEIGHT          = WIDTH / 12 * 9;
    public static final int SCALE           = 2;
    //public static final String TITLE        = "Brick Breaker Game";

    public static final Color activeColor   = Color.DARK_GRAY;
    public static final Color inactiveColor = Color.BLACK;

    private mainMenu main;

    public Menus()
    {
        Graphics g = this.getGraphics();
        main = new mainMenu();

        this.addMouseMotionListener(this);
        this.addMouseListener(this);

        this.setOpaque(true);
        this.setBackground(Color.lightGray);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        main.render(g);
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {

    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        if ((mainMenu.x + mainMenu.width) >= e.getX() && e.getX() >= mainMenu.x)
        {
            if ((mainMenu.yArray[0] + mainMenu.height) >= e.getY() && e.getY() >= mainMenu.yArray[0])
            {
                mainMenu.state = mainMenu.STATE.SP;
            }
            else if ((mainMenu.yArray[1] + mainMenu.height) >= e.getY() && e.getY() >= mainMenu.yArray[1])
            {
                mainMenu.state = mainMenu.STATE.MP;
            }
            else if ((mainMenu.yArray[2] + mainMenu.height) >= e.getY() && e.getY() >= mainMenu.yArray[2])
            {
                mainMenu.state = mainMenu.STATE.SET;
            }
            else if ((mainMenu.yArray[3] + mainMenu.height) >= e.getY() && e.getY() >= mainMenu.yArray[3])
            {
                mainMenu.state = mainMenu.STATE.HELP;
            }
            else if ((mainMenu.yArray[4] + mainMenu.height) >= e.getY() && e.getY() >= mainMenu.yArray[4])
            {
                mainMenu.state = mainMenu.STATE.QUIT;
            }
            else
            {
                mainMenu.state = mainMenu.STATE.OTHER;
            }
        }
        else
        {
            mainMenu.state = mainMenu.STATE.OTHER;
        }
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        if ((mainMenu.x + mainMenu.width) >= e.getX() && e.getX() >= mainMenu.x)
        {
            if ((mainMenu.yArray[4] + mainMenu.height) >= e.getY() && e.getY() >= mainMenu.yArray[4])
            {
                System.exit(0);
            }
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
