package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;


public class aboutMenu extends Menu
{
    private enum BUTTONSTATE {BACK, OTHER}

    private CLICKEDSTATE clickedState               = CLICKEDSTATE.OTHER;
    private BUTTONSTATE buttonState                 = BUTTONSTATE.OTHER;
    private final BUTTONSTATE[] buttonStateArray    = new BUTTONSTATE[]{BUTTONSTATE.BACK, BUTTONSTATE.OTHER};

    private final int[] rectangleYArray             = new int[]{400};

    private final Rectangle backButton              = new Rectangle(super.rectangleX, rectangleYArray[0], super.rectangleWidth, super.rectangleHeight);

    private final Rectangle aboutBackground         = new Rectangle(super.xTitle, super.yTitle + 60, 592, 195);

    public aboutMenu() {}

    @Override
    public void render(Graphics g)
    {
        super.render(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(super.inactiveColor);
        g2d.fill(backButton);

        switch(buttonState)
        {
            case BACK:
                if (clickedState == CLICKEDSTATE.CLICKED) { g2d.setColor(super.clickedColor); }
                else { g2d.setColor(super.activeColor); }

                g2d.fill(backButton);
                break;

            default:
                g2d.setColor(super.inactiveColor);
                g2d.fill(backButton);
        }

        Font fnt1 = new Font(super.fontStyle, Font.BOLD, super.fontSize);
        g.setFont(fnt1);
        g.setColor(super.fontColor);
        g.drawString("Back", backButton.x + 112, backButton.y + 35);

        g2d.setColor(super.activeColor);
        g2d.fill(aboutBackground);

        Font fnt2 = new Font("Apple casual", Font.BOLD, 12);
        g.setFont(fnt2);
        g.setColor(super.fontColor);
        g.drawString("This is a homework project of the \"Software Technology for Embedded Systems\" course at " +
                "Budapest", aboutBackground.x + 10, aboutBackground.y + 20);
        g.drawString("University of Technology and Economics. Our task was to create a JAVA based implementation "
                + "of the", aboutBackground.x + 10, aboutBackground.y + 40);
        g.drawString("so called Brick Breaker Game with both Single Player and real-time Multiplayer " +
                "game mode options.", aboutBackground.x + 10, aboutBackground.y + 60);

        g.drawString("Contributors:", aboutBackground.x + 10, aboutBackground.y + 100);
        g.drawString("Tamas Horvath", aboutBackground.x + 250, aboutBackground.y + 100);
        g.drawString("Peter Santa", aboutBackground.x + 260, aboutBackground.y + 120);
        g.drawString("Marton Tim", aboutBackground.x + 261, aboutBackground.y + 140);

        g.drawString("Advisor:", aboutBackground.x + 10, aboutBackground.y + 180);
        g.drawString("Zsolt Karandi", aboutBackground.x + 257, aboutBackground.y + 180);

        //g.drawString("Waiting for Client to connect: " + counter, backButton.x - 100, backButton.y - 200);
        //g.drawString("Your IP address: " + ipAddress, backButton.x - 100, backButton.y - 130);

        clickedState = CLICKEDSTATE.OTHER;
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
                    hl.menuSwitchHandler(menuHandler.MENUSTATE.MAIN);
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
