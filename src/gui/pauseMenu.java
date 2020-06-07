package gui;

import java.awt.*;
import java.awt.event.MouseEvent;

public class pauseMenu extends Menu
{
    private enum BUTTONSTATE {RESUME, BACK, OTHER};

    private CLICKEDSTATE clickedState               = Menu.CLICKEDSTATE.OTHER;
    private BUTTONSTATE buttonState                 = BUTTONSTATE.OTHER;
    private final BUTTONSTATE[] buttonStateArray    = new BUTTONSTATE[] { BUTTONSTATE.RESUME, BUTTONSTATE.BACK, BUTTONSTATE.OTHER };

    private final int[] rectangleYArray             = new int[]{ 120, 175 };

    private final Rectangle resumeButton            = new Rectangle(super.rectangleX, rectangleYArray[0], super.rectangleWidth, super.rectangleHeight);
    private final Rectangle backButton              = new Rectangle(super.rectangleX, rectangleYArray[1], super.rectangleWidth, super.rectangleHeight);

    public pauseMenu(){}

    @Override
    public void render(Graphics g)
    {
        super.render(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(super.inactiveColor);
        g2d.fill(resumeButton);
        g2d.fill(backButton);

        switch(buttonState)
        {
            case RESUME:
                if (clickedState == CLICKEDSTATE.CLICKED) { g2d.setColor(super.clickedColor); }
                else { g2d.setColor(super.activeColor); }

                g2d.fill(resumeButton);
                break;

            case BACK:
                if (clickedState == Menu.CLICKEDSTATE.CLICKED) { g2d.setColor(super.clickedColor); }
                else { g2d.setColor(super.activeColor); }

                g2d.fill(backButton);
                break;

            default:
                g2d.setColor(super.inactiveColor);
                g2d.fill(resumeButton);
                g2d.fill(backButton);
        }

        Font fnt1 = new Font(super.fontStyle, Font.BOLD, super.fontSize);
        g.setFont(fnt1);
        g.setColor(super.fontColor);
        g.drawString("Resume", resumeButton.x + 90, resumeButton.y + 35);
        g.drawString("Back", backButton.x + 112, backButton.y + 35);

        clickedState = Menu.CLICKEDSTATE.OTHER;
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        if ((super.rectangleX + super.rectangleWidth) >= e.getX() && e.getX() >= super.rectangleX)
        {
            buttonState = buttonStateArray[super.mouseMovedHelper(rectangleYArray, e.getY())];
        }
        else
        {
            buttonState = BUTTONSTATE.OTHER;
        }

        for (MenuListener hl : listeners)
        {
            hl.menuPaintHandler();
        }
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        if ((super.rectangleX + super.rectangleWidth) >= e.getX() && e.getX() >= super.rectangleX)
        {
            clickedState = super.clickedStateArray[super.mousePressedHelper(rectangleYArray, e.getY())];
        }
        else
        {
            clickedState = CLICKEDSTATE.OTHER;
        }

        for (MenuListener hl : listeners)
        {
            hl.menuPaintHandler();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        clickedState = CLICKEDSTATE.OTHER;
        for (MenuListener hl : listeners)
        {
            hl.menuPaintHandler();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        if ((super.rectangleX + super.rectangleWidth) >= e.getX() && e.getX() >= super.rectangleX)
        {
            if ((rectangleYArray[0] + super.rectangleHeight) >= e.getY() && e.getY() >= rectangleYArray[0])
            {
                for (MenuListener hl : listeners)
                {
                    hl.menuSwitchHandler(menuHandler.MENUSTATE.SINGLEPLAYER);
                }
            }
            else if ((rectangleYArray[1] + super.rectangleHeight) >= e.getY() && e.getY() >= rectangleYArray[1])
            {
                for (MenuListener hl : listeners)
                {
                    hl.menuSwitchHandler(menuHandler.MENUSTATE.MAPCHOOSER);
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }
}
