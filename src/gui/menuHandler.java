package gui;

import game.Gameplay;

import javax.swing.*;
import java.awt.*;

public class menuHandler extends JPanel implements MenuListener
{
    public static enum MENUSTATE { MAIN, SINGLEPLAYER, MULTIPLAYER, HIGHSCORE, ABOUT, QUIT, SERVERMODE, CLIENTMODE, PAUSE };
    private MENUSTATE menuState = MENUSTATE.MAIN;

    private Menu currentMenu;
    private Gameplay game;

    public menuHandler()
    {
        currentMenu = new mainMenu();
        currentMenu.addListener(this);

        this.setPreferredSize(new Dimension(GUI.WIDTH, GUI.HEIGHT));

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
    public void gamePaintHandler()
    {
        repaint();
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

                this.removeMouseListener(currentMenu);
                this.removeMouseMotionListener(currentMenu);
                this.removeKeyListener(game);

                //game = new Gameplay();
                game = Gameplay.startFromCheckpoint();
                game.addListener(this);

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

            case ABOUT:
                menuState = MENUSTATE.ABOUT;

                this.removeMouseListener(currentMenu);
                this.removeMouseMotionListener(currentMenu);
                this.removeKeyListener(game);

                currentMenu = new aboutMenu();
                currentMenu.addListener(this);

                this.addMouseListener(currentMenu);
                this.addMouseMotionListener(currentMenu);

                repaint();
                break;

            case SERVERMODE:
                menuState = MENUSTATE.SERVERMODE;

                this.removeMouseListener(currentMenu);
                this.removeMouseMotionListener(currentMenu);
                this.removeKeyListener(game);

                currentMenu = new serverModeMenu();
                currentMenu.addListener(this);

                this.addMouseListener(currentMenu);
                this.addMouseMotionListener(currentMenu);

                repaint();
                break;

            case CLIENTMODE:
                menuState = MENUSTATE.CLIENTMODE;

                this.removeMouseListener(currentMenu);
                this.removeMouseMotionListener(currentMenu);
                this.removeKeyListener(game);

                currentMenu = new clientModeMenu();
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
