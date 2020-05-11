import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;

public class mainMenu extends Menu //implements MouseMotionListener, MouseListener
{
    private enum BUTTONSTATE {SINGLEPLAYER, MULTIPLAYER, SETTINGS, HELP, QUIT, OTHER};

    private CLICKEDSTATE clickedState               = CLICKEDSTATE.OTHER;
    private BUTTONSTATE buttonState                 = BUTTONSTATE.OTHER;
    private final BUTTONSTATE[] buttonStateArray    = new BUTTONSTATE[]
            { BUTTONSTATE.SINGLEPLAYER, BUTTONSTATE.MULTIPLAYER, BUTTONSTATE.SETTINGS, BUTTONSTATE.HELP, BUTTONSTATE.QUIT, BUTTONSTATE.OTHER };

    private final int[] rectangleYArray             = new int[]{ 120, 175, 230, 285, 340 };

    private final Rectangle singlePlayerButton      = new Rectangle(super.rectangleX, rectangleYArray[0], super.rectangleWidth, super.rectangleHeight);
    private final Rectangle multiplayerButton       = new Rectangle(super.rectangleX, rectangleYArray[1], super.rectangleWidth, super.rectangleHeight);
    private final Rectangle settingsButton          = new Rectangle(super.rectangleX, rectangleYArray[2], super.rectangleWidth, super.rectangleHeight);
    private final Rectangle helpButton              = new Rectangle(super.rectangleX, rectangleYArray[3], super.rectangleWidth, super.rectangleHeight);
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
        g2d.fill(settingsButton);
        g2d.fill(helpButton);
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

            case SETTINGS:
                if (clickedState == CLICKEDSTATE.CLICKED) { g2d.setColor(super.clickedColor); }
                else { g2d.setColor(super.activeColor); }

                g2d.fill(settingsButton);
                break;

            case HELP:
                if (clickedState == CLICKEDSTATE.CLICKED) { g2d.setColor(super.clickedColor); }
                else { g2d.setColor(super.activeColor); }

                g2d.fill(helpButton);
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
                g2d.fill(settingsButton);
                g2d.fill(helpButton);
                g2d.fill(quitButton);
        }

        Font fnt1 = new Font(super.fontStyle, Font.BOLD, super.fontSize);
        g.setFont(fnt1);
        g.setColor(super.fontColor);
        g.drawString("Single player", singlePlayerButton.x + 54, singlePlayerButton.y + 35);
        g.drawString("Multiplayer", multiplayerButton.x + 70, multiplayerButton.y + 35);
        g.drawString("Settings", settingsButton.x + 90, settingsButton.y + 35);
        g.drawString("Help", helpButton.x + 118, helpButton.y + 35);
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
                    hl.menuSwitchHandler(menuHandler.MENUSTATE.SETTINGS);
                }
            }
            else if ((rectangleYArray[3] + super.rectangleHeight) >= e.getY() && e.getY() >= rectangleYArray[3])
            {
                for (MenuListener hl : listeners)
                {
                    hl.menuSwitchHandler(menuHandler.MENUSTATE.HELP);
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
