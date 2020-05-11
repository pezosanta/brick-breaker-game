package gui;

import game.Gameplay;

import javax.swing.*;
import java.awt.*;

public class menuHandler extends JPanel implements MenuListener
{
    public static enum MENUSTATE { MAIN, SINGLEPLAYER, MULTIPLAYER, SETTINGS, HELP, QUIT, HEROVSVILLAIN, COOPERATION, PAUSE };
    private MENUSTATE menuState = MENUSTATE.MAIN;

    private Menu currentMenu;
    private Gameplay game;

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
        if (menuState == MENUSTATE.SINGLEPLAYER) { game.render(g); }
        else { currentMenu.render(g); }
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

                this.removeMouseListener(currentMenu);
                this.removeMouseMotionListener(currentMenu);
                this.removeKeyListener(game);

                currentMenu = new mainMenu();
                currentMenu.addListener(this);

                this.addMouseListener(currentMenu);
                this.addMouseMotionListener(currentMenu);

                repaint();
                break;

            case SINGLEPLAYER:
                menuState = MENUSTATE.SINGLEPLAYER;

                game = new Gameplay();
                game.addListener(this);

                this.removeMouseListener(currentMenu);
                this.removeMouseMotionListener(currentMenu);
                this.removeKeyListener(game);

                this.addKeyListener(game);

                this.setFocusable(true);
                this.requestFocusInWindow();
                this.setFocusTraversalKeysEnabled(false);

                repaint();

                currentMenu = null;
                break;

            case MULTIPLAYER:
                menuState = MENUSTATE.MULTIPLAYER;

                this.removeMouseListener(currentMenu);
                this.removeMouseMotionListener(currentMenu);
                this.removeKeyListener(game);

                currentMenu = new multiplayerMenu();
                currentMenu.addListener(this);

                this.addMouseListener(currentMenu);
                this.addMouseMotionListener(currentMenu);

                repaint();
                break;

            case PAUSE:
                menuState = MENUSTATE.PAUSE;

                this.removeMouseListener(currentMenu);
                this.removeMouseMotionListener(currentMenu);
                this.removeKeyListener(game);

                currentMenu = new pauseMenu();
                currentMenu.addListener(this);

                this.addMouseListener(currentMenu);
                this.addMouseMotionListener(currentMenu);

                repaint();

                game = null;
                break;

            case QUIT:
                System.exit(0);
                break;
        }
    }
}
