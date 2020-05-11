package gui;

import javax.swing.*;
import java.awt.*;

interface MenuListener
{
    void menuPaintHandler();
    void menuSwitchHandler(menuHandler.MENUSTATE newMenuState);
}

public class menuHandler extends JPanel implements MenuListener
{
    public static enum MENUSTATE { MAIN, SINGLEPLAYER, MULTIPLAYER, SETTINGS, HELP, QUIT, HEROVSVILLAIN, COOPERATION };
    private MENUSTATE menuState = MENUSTATE.MAIN;

    private Menu currentMenu;

    public menuHandler()
    {
        currentMenu = new mainMenu();
        currentMenu.addListener(this);

        this.addMouseListener(currentMenu);
        this.addMouseMotionListener(currentMenu);

        this.setOpaque(true);
        this.setBackground(Color.lightGray);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        currentMenu.render(g);
    }

    @Override
    public void menuPaintHandler()
    {
        repaint();
    }

    @Override
    public void menuSwitchHandler(MENUSTATE newMenuState)
    {
        switch(newMenuState)
        {
            case MAIN:
                menuState = MENUSTATE.MAIN;
                currentMenu = new mainMenu();
                currentMenu.addListener(this);
                this.addMouseListener(currentMenu);
                this.addMouseMotionListener(currentMenu);
                repaint();
                break;

            case MULTIPLAYER:
                menuState = MENUSTATE.MULTIPLAYER;
                currentMenu = new multiplayerMenu();
                currentMenu.addListener(this);
                this.addMouseListener(currentMenu);
                this.addMouseMotionListener(currentMenu);
                repaint();
                break;

            case QUIT:
                System.exit(0);
                break;
        }
    }
}
