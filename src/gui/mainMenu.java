package gui;

import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.event.MouseEvent;

public class mainMenu extends Menu
{
    private enum BUTTONSTATE {SINGLEPLAYER, MULTIPLAYER, HIGHSCORE, ABOUT, QUIT, OTHER};

    private CLICKEDSTATE clickedState               = CLICKEDSTATE.OTHER;
    private BUTTONSTATE buttonState                 = BUTTONSTATE.OTHER;
    private final BUTTONSTATE[] buttonStateArray    = new BUTTONSTATE[]
            { BUTTONSTATE.SINGLEPLAYER, BUTTONSTATE.MULTIPLAYER, BUTTONSTATE.HIGHSCORE, BUTTONSTATE.ABOUT, BUTTONSTATE.QUIT, BUTTONSTATE.OTHER };

    private final int[] rectangleYArray             = new int[]{ 180, 235, 290, 345, 400 };

    private final Rectangle singlePlayerButton      = new Rectangle(super.rectangleX, rectangleYArray[0], super.rectangleWidth, super.rectangleHeight);
    private final Rectangle multiplayerButton       = new Rectangle(super.rectangleX, rectangleYArray[1], super.rectangleWidth, super.rectangleHeight);
    private final Rectangle highScoreButton         = new Rectangle(super.rectangleX, rectangleYArray[2], super.rectangleWidth, super.rectangleHeight);
    private final Rectangle aboutButton             = new Rectangle(super.rectangleX, rectangleYArray[3], super.rectangleWidth, super.rectangleHeight);
    private final Rectangle quitButton              = new Rectangle(super.rectangleX, rectangleYArray[4], super.rectangleWidth, super.rectangleHeight);

    public mainMenu() {}

    @Override
    public void render(Graphics g)
    {
        super.render(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(super.inactiveColor);
        g2d.fill(singlePlayerButton);
        g2d.fill(multiplayerButton);
        g2d.fill(highScoreButton);
        g2d.fill(aboutButton);
        g2d.fill(quitButton);

        switch(buttonState)
        {
            case SINGLEPLAYER:
                if (clickedState == CLICKEDSTATE.CLICKED) { g2d.setColor(super.clickedColor); }
                else { g2d.setColor(super.activeColor); }

                g2d.fill(singlePlayerButton);
                break;

            case MULTIPLAYER:
                if (clickedState == CLICKEDSTATE.CLICKED) { g2d.setColor(super.clickedColor); }
                else { g2d.setColor(super.activeColor); }

                g2d.fill(multiplayerButton);
                break;

            case HIGHSCORE:
                if (clickedState == CLICKEDSTATE.CLICKED) { g2d.setColor(super.clickedColor); }
                else { g2d.setColor(super.activeColor); }

                g2d.fill(highScoreButton);
                break;

            case ABOUT:
                if (clickedState == CLICKEDSTATE.CLICKED) { g2d.setColor(super.clickedColor); }
                else { g2d.setColor(super.activeColor); }

                g2d.fill(aboutButton);
                break;

            case QUIT:
                if (clickedState == CLICKEDSTATE.CLICKED) { g2d.setColor(super.clickedColor); }
                else { g2d.setColor(super.activeColor); }

                g2d.fill(quitButton);
                break;

            default:
                g2d.setColor(super.inactiveColor);
                g2d.fill(singlePlayerButton);
                g2d.fill(multiplayerButton);
                g2d.fill(highScoreButton);
                g2d.fill(aboutButton);
                g2d.fill(quitButton);
        }

        Font fnt1 = new Font(super.fontStyle, Font.BOLD, super.fontSize);
        g.setFont(fnt1);
        g.setColor(super.fontColor);
        g.drawString("Single player", singlePlayerButton.x + 55, singlePlayerButton.y + 35);
        g.drawString("Multiplayer", multiplayerButton.x + 69, multiplayerButton.y + 35);
        g.drawString("High-scores", highScoreButton.x + 63, highScoreButton.y + 35);
        g.drawString("About", aboutButton.x + 106, aboutButton.y + 35);
        g.drawString("Quit", quitButton.x + 118, quitButton.y + 35);

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
        //menuHandlerObject.repaint();
        for (MenuListener hl : listeners)
        {
            hl.menuPaintHandler();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        clickedState = CLICKEDSTATE.OTHER;
        //menuHandlerObject.repaint();
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
                    hl.menuSwitchHandler(menuHandler.MENUSTATE.MULTIPLAYER);
                }
            }
            else if ((rectangleYArray[2] + super.rectangleHeight) >= e.getY() && e.getY() >= rectangleYArray[2])
            {
                for (MenuListener hl : listeners)
                {
                    hl.menuSwitchHandler(menuHandler.MENUSTATE.HIGHSCORE);
                }
            }
            else if ((rectangleYArray[3] + super.rectangleHeight) >= e.getY() && e.getY() >= rectangleYArray[3])
            {
                for (MenuListener hl : listeners)
                {
                    hl.menuSwitchHandler(menuHandler.MENUSTATE.ABOUT);
                }
            }
            else if ((rectangleYArray[4] + rectangleHeight) >= e.getY() && e.getY() >= rectangleYArray[4])
            {
                for (MenuListener hl : listeners)
                {
                    hl.menuSwitchHandler(menuHandler.MENUSTATE.QUIT);
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
