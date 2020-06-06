package gui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

public class Menu implements MouseMotionListener, MouseListener
{
    protected final int xTitle                          = (GUI.WIDTH - 592) / 2;
    protected final int yTitle                          = 75;
    protected final int sizeTitle                       = 50;

    protected final int rectangleWidth                  = 300;
    protected final int rectangleHeight                 = 50;
    protected final int rectangleX                      = (GUI.WIDTH - rectangleWidth) / 2;

    protected final String fontStyle                    = "Apple casual";
    protected final int fontSize                        = 30;
    protected final Color fontColor                     = Color.WHITE;

    protected final Color activeColor                   = Color.DARK_GRAY;
    protected final Color inactiveColor                 = Color.BLACK;
    protected final Color clickedColor                  = Color.ORANGE;

    protected enum CLICKEDSTATE { CLICKED, OTHER };
    protected final CLICKEDSTATE[] clickedStateArray    = new CLICKEDSTATE[] { CLICKEDSTATE.CLICKED, CLICKEDSTATE.OTHER };

    protected List<MenuListener> listeners = new ArrayList<MenuListener>();

    public void addListener(MenuListener listenerToAdd)
    {
        listeners.add(listenerToAdd);
    }

    public void render(Graphics g)
    {
        Font fnt0 = new Font(fontStyle, Font.BOLD, sizeTitle);
        g.setFont(fnt0);
        g.setColor(Color.black);
        g.drawString("BRICK BREAKER GAME", xTitle, yTitle);
        //System.out.println(g.getFontMetrics().stringWidth("BRICK BREAKER GAME"));
    }

    protected int mouseMovedHelper (int[] rectangleYArray, int mouseY)
    {
        int buttonStateIndex = rectangleYArray.length;      // buttonStateArray.length = rectangleYArray.length + 1

        for (int i = 0; i < rectangleYArray.length; i++)
        {
            if ((rectangleYArray[i] + rectangleHeight) >= mouseY && mouseY >= rectangleYArray[i])
            {
                buttonStateIndex = i;
                break;
            }
        }

        return buttonStateIndex;
    }

    protected int mousePressedHelper (int[] rectangleYArray, int mouseY)
    {
        int clickedStateIndex = clickedStateArray.length -1;

        for (int i = 0; i < rectangleYArray.length; i++)
        {
            if ((rectangleYArray[i] + rectangleHeight) >= mouseY && mouseY >= rectangleYArray[i])
            {
                clickedStateIndex = 0;
            }
        }

        return clickedStateIndex;
    }

    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void mouseDragged(MouseEvent e) { }

    @Override
    public void mouseMoved(MouseEvent e) { }
}
