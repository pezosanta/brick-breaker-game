package gui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class mapChooserMenu extends Menu
{
    private enum BUTTONSTATE { MAP1, MAP2, MAP3, MAP4, BACK, OTHER};

    private CLICKEDSTATE clickedState               = CLICKEDSTATE.OTHER;
    private BUTTONSTATE buttonState                 = BUTTONSTATE.OTHER;
    private final BUTTONSTATE[] buttonStateArray    = new BUTTONSTATE[]
            { BUTTONSTATE.MAP1, BUTTONSTATE.MAP2, BUTTONSTATE.MAP3, BUTTONSTATE.MAP4, BUTTONSTATE.BACK, BUTTONSTATE.OTHER };

    private final int[] rectangleYArray             = new int[]{ 180, 235, 290, 345, 400 };

    private final Rectangle map1Button              = new Rectangle(super.rectangleX, rectangleYArray[0], super.rectangleWidth, super.rectangleHeight);
    private final Rectangle map2Button              = new Rectangle(super.rectangleX, rectangleYArray[1], super.rectangleWidth, super.rectangleHeight);
    private final Rectangle map3Button              = new Rectangle(super.rectangleX, rectangleYArray[2], super.rectangleWidth, super.rectangleHeight);
    private final Rectangle map4Button              = new Rectangle(super.rectangleX, rectangleYArray[3], super.rectangleWidth, super.rectangleHeight);
    private final Rectangle backButton              = new Rectangle(super.rectangleX, rectangleYArray[4], super.rectangleWidth, super.rectangleHeight);


    private File folder = new File("./resources/maps/");
    private List<String> mapNames = new ArrayList<String>();

    public mapChooserMenu() { getMapNames(); }

    public void getMapNames()
    {
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++)
        {
            if (listOfFiles[i].isFile())
            {
                mapNames.add(listOfFiles[i].getName().split("\\.")[0]);
            }
            else if (listOfFiles[i].isDirectory())
            {
                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }
    }

    @Override
    public void render(Graphics g)
    {
        super.render(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(super.inactiveColor);
        g2d.fill(map1Button);
        g2d.fill(map2Button);
        g2d.fill(map3Button);
        g2d.fill(map4Button);
        g2d.fill(backButton);

        switch(buttonState)
        {
            case MAP1:
                if (clickedState == CLICKEDSTATE.CLICKED) { g2d.setColor(super.clickedColor); }
                else { g2d.setColor(super.activeColor); }

                g2d.fill(map1Button);
                break;

            case MAP2:
                if (clickedState == CLICKEDSTATE.CLICKED) { g2d.setColor(super.clickedColor); }
                else { g2d.setColor(super.activeColor); }

                g2d.fill(map2Button);
                break;

            case MAP3:
                if (clickedState == CLICKEDSTATE.CLICKED) { g2d.setColor(super.clickedColor); }
                else { g2d.setColor(super.activeColor); }

                g2d.fill(map3Button);
                break;

            case MAP4:
                if (clickedState == CLICKEDSTATE.CLICKED) { g2d.setColor(super.clickedColor); }
                else { g2d.setColor(super.activeColor); }

                g2d.fill(map4Button);
                break;

            case BACK:
                if (clickedState == CLICKEDSTATE.CLICKED) { g2d.setColor(super.clickedColor); }
                else { g2d.setColor(super.activeColor); }

                g2d.fill(backButton);
                break;

            default:
                g2d.setColor(super.inactiveColor);
                g2d.fill(map1Button);
                g2d.fill(map2Button);
                g2d.fill(map3Button);
                g2d.fill(map4Button);
                g2d.fill(backButton);
        }

        Font fnt1 = new Font(super.fontStyle, Font.BOLD, super.fontSize);
        g.setFont(fnt1);
        g.setColor(super.fontColor);
        g.drawString("MAP 1", map1Button.x + 55, map1Button.y + 35);
        g.drawString("MAP 2", map2Button.x + 69, map2Button.y + 35);
        g.drawString("MAP 3", map3Button.x + 63, map3Button.y + 35);
        g.drawString("MAP 4", map4Button.x + 106, map4Button.y + 35);
        g.drawString("BACK", backButton.x + 118, backButton.y + 35);

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
                    hl.menuSwitchHandler(menuHandler.MENUSTATE.SINGLEPLAYER);
                }
            }
            else if ((rectangleYArray[2] + super.rectangleHeight) >= e.getY() && e.getY() >= rectangleYArray[2])
            {
                for (MenuListener hl : listeners)
                {
                    hl.menuSwitchHandler(menuHandler.MENUSTATE.SINGLEPLAYER);
                }
            }
            else if ((rectangleYArray[3] + super.rectangleHeight) >= e.getY() && e.getY() >= rectangleYArray[3])
            {
                for (MenuListener hl : listeners)
                {
                    hl.menuSwitchHandler(menuHandler.MENUSTATE.SINGLEPLAYER);
                }
            }
            else if ((rectangleYArray[4] + rectangleHeight) >= e.getY() && e.getY() >= rectangleYArray[4])
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