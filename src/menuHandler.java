import javax.swing.*;
import java.awt.*;

interface MenuListener
{
    void menuSwitchHandler(menuHandler.MENUSTATE newMenuState);
}

public class menuHandler extends JPanel implements MenuListener
{
    public static enum MENUSTATE { MAIN, SINGLEPLAYER, MULTIPLAYER, SETTINGS, HELP, QUIT, HEROVSVILLAIN, COOPERATION };
    private MENUSTATE menuState = MENUSTATE.MAIN;

    private Menu currentMenu;

    public menuHandler()
    {
        currentMenu = new mainMenu(this);
        currentMenu.addListener(this);

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
    public void menuSwitchHandler(MENUSTATE newMenuState)
    {
        switch(newMenuState)
        {
            case MAIN:
                currentMenu = new mainMenu(this);
                currentMenu.addListener(this);
                repaint();
                break;

            case MULTIPLAYER:
                currentMenu = new multiplayerMenu(this);
                currentMenu.addListener(this);
                repaint();
                break;

            case QUIT:
                System.exit(0);
                break;
        }
    }
}
