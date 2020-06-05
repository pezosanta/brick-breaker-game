package gui;

import game.GameClient;
import game.GameServer;

import javax.swing.*;
import java.awt.*;

public class menuHandler extends JPanel implements MenuListener
{
    public static enum MENUSTATE { MAIN, SINGLEPLAYER, MULTIPLAYER, SETTINGS, HELP, QUIT, SERVERMODE, CLIENTMODE, PAUSE };
    private MENUSTATE menuState = MENUSTATE.MAIN;

    private Menu currentMenu;
    private GameServer gameServer;
    private GameClient gameClient;

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
        if (menuState == MENUSTATE.SINGLEPLAYER) { gameClient.render(g); }
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
                this.removeKeyListener(gameClient);
                if (gameServer != null) gameServer.stopServer();
                if (gameClient != null) gameClient.finalizeClient();

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
                this.removeKeyListener(gameClient);
                if (gameServer != null) gameServer.stopServer();
                if (gameClient != null) gameClient.finalizeClient();

                //game = new Gameplay();
                gameServer = GameServer.startFromCheckpoint();
                gameClient = gameServer.createLocalClient();
                gameClient.addListener(this);
                gameServer.sendTestMsg();
                gameClient.sendTestMsg();
                System.out.println(gameClient.getTestMsg());
                System.out.println(gameServer.getTestMsg());
                System.out.println("Ennyi volt?");
                gameServer.startServer();

                this.addKeyListener(gameClient);

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
                this.removeKeyListener(gameClient);
                if (gameServer != null) gameServer.stopServer();
                if (gameClient != null) gameClient.finalizeClient();

                currentMenu = new multiplayerMenu();
                currentMenu.addListener(this);

                this.addMouseListener(currentMenu);
                this.addMouseMotionListener(currentMenu);

                repaint();
                break;

            case SERVERMODE:
                menuState = MENUSTATE.SERVERMODE;

                this.removeMouseListener(currentMenu);
                this.removeMouseMotionListener(currentMenu);
                this.removeKeyListener(gameClient);
                if (gameServer != null) gameServer.stopServer();
                if (gameClient != null) gameClient.finalizeClient();

                currentMenu = new serverModeMenu();
                currentMenu.addListener(this);

                this.addMouseListener(currentMenu);
                this.addMouseMotionListener(currentMenu);

                repaint();
                break;

            case PAUSE:
                menuState = MENUSTATE.PAUSE;

                this.removeMouseListener(currentMenu);
                this.removeMouseMotionListener(currentMenu);
                this.removeKeyListener(gameClient);
                if (gameServer != null) gameServer.stopServer();
                if (gameClient != null) gameClient.finalizeClient();

                currentMenu = new pauseMenu();
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
