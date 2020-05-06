import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;

import java.lang.Thread;

public class Menus extends JPanel implements MouseMotionListener, MouseListener
{
    public static final int xTitle          = 17;
    public static final int yTitle          = 75;
    public static final int sizeTitle       = 50;
    public static final Color activeColor   = Color.DARK_GRAY;
    public static final Color inactiveColor = Color.BLACK;
    public static final Color clickedColor  = Color.ORANGE;

    private enum MENUSTATE {MAIN, SINGLE, MULTI, SET, HELP};
    private MENUSTATE menuState             = MENUSTATE.MAIN;

    private mainMenu main;
    private multiplayerMenu multi;

    public Menus()
    {
        Graphics g = this.getGraphics();
        main = new mainMenu();
        multi = new multiplayerMenu();

        this.addMouseMotionListener(this);
        this.addMouseListener(this);

        this.setOpaque(true);
        this.setBackground(Color.lightGray);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (menuState == MENUSTATE.MAIN)
        {
            main.render(g);
        }
        else if (menuState == MENUSTATE.MULTI)
        {
            multi.render(g);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        if (menuState == MENUSTATE.MAIN)
        {
            if ((mainMenu.rectangleX + mainMenu.rectangleWidth) >= e.getX() && e.getX() >= mainMenu.rectangleX)
            {
                if ((mainMenu.rectangleYArray[0] + mainMenu.rectangleHeight) >= e.getY() && e.getY() >= mainMenu.rectangleYArray[0])
                {
                    mainMenu.buttonState = mainMenu.BUTTONSTATE.SP;
                }
                else if ((mainMenu.rectangleYArray[1] + mainMenu.rectangleHeight) >= e.getY() && e.getY() >= mainMenu.rectangleYArray[1])
                {
                    mainMenu.buttonState = mainMenu.BUTTONSTATE.MP;
                }
                else if ((mainMenu.rectangleYArray[2] + mainMenu.rectangleHeight) >= e.getY() && e.getY() >= mainMenu.rectangleYArray[2])
                {
                    mainMenu.buttonState = mainMenu.BUTTONSTATE.SET;
                }
                else if ((mainMenu.rectangleYArray[3] + mainMenu.rectangleHeight) >= e.getY() && e.getY() >= mainMenu.rectangleYArray[3])
                {
                    mainMenu.buttonState = mainMenu.BUTTONSTATE.HELP;
                }
                else if ((mainMenu.rectangleYArray[4] + mainMenu.rectangleHeight) >= e.getY() && e.getY() >= mainMenu.rectangleYArray[4])
                {
                    mainMenu.buttonState = mainMenu.BUTTONSTATE.QUIT;
                }
                else
                {
                    mainMenu.buttonState = mainMenu.BUTTONSTATE.OTHER;
                }
            }
            else
            {
                mainMenu.buttonState = mainMenu.BUTTONSTATE.OTHER;
            }
        }
        else if (menuState == MENUSTATE.MULTI)
        {
            if ((multiplayerMenu.rectangleX + multiplayerMenu.rectangleWidth) >= e.getX() && e.getX() >= multiplayerMenu.rectangleX)
            {
                if ((multiplayerMenu.rectangleYArray[0] + multiplayerMenu.rectangleHeight) >= e.getY() && e.getY() >= multiplayerMenu.rectangleYArray[0])
                {
                    multiplayerMenu.buttonState = multiplayerMenu.BUTTONSTATE.VS;
                }
                else if ((multiplayerMenu.rectangleYArray[1] + multiplayerMenu.rectangleHeight) >= e.getY() && e.getY() >= multiplayerMenu.rectangleYArray[1])
                {
                    multiplayerMenu.buttonState = multiplayerMenu.BUTTONSTATE.COOP;
                }
                else if ((multiplayerMenu.rectangleYArray[2] + multiplayerMenu.rectangleHeight) >= e.getY() && e.getY() >= multiplayerMenu.rectangleYArray[2])
                {
                    multiplayerMenu.buttonState = multiplayerMenu.BUTTONSTATE.BACK;
                }
                else
                {
                    multiplayerMenu.buttonState = multiplayerMenu.BUTTONSTATE.OTHER;
                }
            }
            else
            {
                multiplayerMenu.buttonState = multiplayerMenu.BUTTONSTATE.OTHER;
            }
        }
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        if (menuState == MENUSTATE.MAIN)
        {
            if ((mainMenu.rectangleX + mainMenu.rectangleWidth) >= e.getX() && e.getX() >= mainMenu.rectangleX)
            {
                if ((mainMenu.rectangleYArray[1] + mainMenu.rectangleHeight) >= e.getY() && e.getY() >= mainMenu.rectangleYArray[1])
                {
                    menuState = MENUSTATE.MULTI;
                }
                else if ((mainMenu.rectangleYArray[4] + mainMenu.rectangleHeight) >= e.getY() && e.getY() >= mainMenu.rectangleYArray[4])
                {
                    try
                    {
                        Thread.sleep(0);
                    }
                    catch(InterruptedException ex)
                    {
                        Thread.currentThread().interrupt();
                    }
                    System.exit(0);
                }
            }
        }
        else if (menuState == MENUSTATE.MULTI)
        {
            if ((multiplayerMenu.rectangleX + multiplayerMenu.rectangleWidth) >= e.getX() && e.getX() >= multiplayerMenu.rectangleX)
            {
                if ((multiplayerMenu.rectangleYArray[2] + multiplayerMenu.rectangleHeight) >= e.getY() && e.getY() >= multiplayerMenu.rectangleYArray[2])
                {
                    menuState = MENUSTATE.MAIN;
                }
            }
        }
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        if (menuState == MENUSTATE.MAIN)
        {
            if ((mainMenu.rectangleX + mainMenu.rectangleWidth) >= e.getX() && e.getX() >= mainMenu.rectangleX)
            {
                if ((mainMenu.rectangleYArray[0] + mainMenu.rectangleHeight) >= e.getY() && e.getY() >= mainMenu.rectangleYArray[0])
                {
                    mainMenu.clickedState = mainMenu.CLICKEDSTATE.CLICKED;
                }
                else if ((mainMenu.rectangleYArray[1] + mainMenu.rectangleHeight) >= e.getY() && e.getY() >= mainMenu.rectangleYArray[1])
                {
                    mainMenu.clickedState = mainMenu.CLICKEDSTATE.CLICKED;
                }
                else if ((mainMenu.rectangleYArray[2] + mainMenu.rectangleHeight) >= e.getY() && e.getY() >= mainMenu.rectangleYArray[2])
                {
                    mainMenu.clickedState = mainMenu.CLICKEDSTATE.CLICKED;
                }
                else if ((mainMenu.rectangleYArray[3] + mainMenu.rectangleHeight) >= e.getY() && e.getY() >= mainMenu.rectangleYArray[3])
                {
                    mainMenu.clickedState = mainMenu.CLICKEDSTATE.CLICKED;
                }
                else if ((mainMenu.rectangleYArray[4] + mainMenu.rectangleHeight) >= e.getY() && e.getY() >= mainMenu.rectangleYArray[4])
                {
                    mainMenu.clickedState = mainMenu.CLICKEDSTATE.CLICKED;
                }
            }
            repaint();
        }
        else if (menuState == MENUSTATE.MULTI)
        {
            if ((multiplayerMenu.rectangleX + multiplayerMenu.rectangleWidth) >= e.getX() && e.getX() >= multiplayerMenu.rectangleX)
            {
                if ((multiplayerMenu.rectangleYArray[0] + multiplayerMenu.rectangleHeight) >= e.getY() && e.getY() >= multiplayerMenu.rectangleYArray[0])
                {
                    multiplayerMenu.clickedState = multiplayerMenu.CLICKEDSTATE.CLICKED;
                }
                else if ((multiplayerMenu.rectangleYArray[1] + multiplayerMenu.rectangleHeight) >= e.getY() && e.getY() >= multiplayerMenu.rectangleYArray[1])
                {
                    multiplayerMenu.clickedState = multiplayerMenu.CLICKEDSTATE.CLICKED;
                }
                else if ((multiplayerMenu.rectangleYArray[2] + multiplayerMenu.rectangleHeight) >= e.getY() && e.getY() >= multiplayerMenu.rectangleYArray[2])
                {
                    multiplayerMenu.clickedState = multiplayerMenu.CLICKEDSTATE.CLICKED;
                }
            }
            repaint();
        }
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
