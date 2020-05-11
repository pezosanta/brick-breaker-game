package gui;

import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.event.MouseEvent;

public class multiplayerMenu extends Menu
{
    private enum BUTTONSTATE {HEROVSVILLAIN, COOPERATION, BACK, OTHER};

    private CLICKEDSTATE clickedState               = CLICKEDSTATE.OTHER;
    private BUTTONSTATE buttonState                 = BUTTONSTATE.OTHER;
    private final BUTTONSTATE[] buttonStateArray    = new BUTTONSTATE[]
            { BUTTONSTATE.HEROVSVILLAIN, BUTTONSTATE.COOPERATION, BUTTONSTATE.BACK, BUTTONSTATE.OTHER };

    private final int[] rectangleYArray             = new int[]{ 120, 175, 230 };

    private final Rectangle heroVsVillainButton     = new Rectangle(super.rectangleX, rectangleYArray[0], super.rectangleWidth, super.rectangleHeight);
    private final Rectangle cooperationButton       = new Rectangle(super.rectangleX, rectangleYArray[1], super.rectangleWidth, super.rectangleHeight);
    private final Rectangle backButton              = new Rectangle(super.rectangleX, rectangleYArray[2], super.rectangleWidth, super.rectangleHeight);

    public multiplayerMenu(){}

    @Override
    public void render(Graphics g)
    {
        super.render(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(super.inactiveColor);
        g2d.fill(heroVsVillainButton);
        g2d.fill(cooperationButton);
        g2d.fill(backButton);

        switch(buttonState)
        {
            case HEROVSVILLAIN:
                if (clickedState == CLICKEDSTATE.CLICKED) { g2d.setColor(super.clickedColor); }
                else { g2d.setColor(super.activeColor); }

                g2d.fill(heroVsVillainButton);
                break;

            case COOPERATION:
                if (clickedState == CLICKEDSTATE.CLICKED) { g2d.setColor(super.clickedColor); }
                else { g2d.setColor(super.activeColor); }

                g2d.fill(cooperationButton);
                break;

            case BACK:
                if (clickedState == CLICKEDSTATE.CLICKED) { g2d.setColor(super.clickedColor); }
                else { g2d.setColor(super.activeColor); }

                g2d.fill(backButton);
                break;

            default:
                g2d.setColor(super.inactiveColor);
                g2d.fill(heroVsVillainButton);
                g2d.fill(cooperationButton);
                g2d.fill(backButton);
        }

        Font fnt1 = new Font(super.fontStyle, Font.BOLD, super.fontSize);
        g.setFont(fnt1);
        g.setColor(super.fontColor);
        g.drawString("Hero vs Villain", heroVsVillainButton.x + 46, heroVsVillainButton.y + 35);
        g.drawString("CO-OP", cooperationButton.x + 98, cooperationButton.y + 35);
        g.drawString("Back", backButton.x + 112, backButton.y + 35);

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
                    hl.menuSwitchHandler(menuHandler.MENUSTATE.HEROVSVILLAIN);
                }
            }
            else if ((rectangleYArray[1] + super.rectangleHeight) >= e.getY() && e.getY() >= rectangleYArray[1])
            {
                for (MenuListener hl : listeners)
                {
                    hl.menuSwitchHandler(menuHandler.MENUSTATE.COOPERATION);
                }
            }
            else if ((rectangleYArray[2] + super.rectangleHeight) >= e.getY() && e.getY() >= rectangleYArray[2])
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
