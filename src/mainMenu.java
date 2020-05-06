import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;

public class mainMenu
{
    public static enum STATE {SP, MP, SET, HELP, QUIT, OTHER};
    public static STATE state = STATE.OTHER;

    public static final int xBBG = 17;
    public static final int yBBG = 75;
    public static final int sizeBBG = 50;

    public static final int x = Menus.WIDTH / 2;
    public static final int[] yArray = new int[]{ 120, 175, 230, 285, 340 };
    public static final int width = 300;
    public static final int height = 50;

    private Rectangle singlePlayerButton    = new Rectangle(x, yArray[0], width, height);
    private Rectangle multiplayerButton     = new Rectangle(x, yArray[1], width, height);
    private Rectangle settingsButton        = new Rectangle(x, yArray[2], width, height);
    private Rectangle helpButton            = new Rectangle(x, yArray[3], width, height);
    private Rectangle quitButton            = new Rectangle(x, yArray[4], width, height);


    public void render(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        Font fnt0 = new Font("arial", Font.BOLD, sizeBBG);
        g.setFont(fnt0);
        g.setColor(Color.black);
        g.drawString("BRICK BREAKER GAME", xBBG, yBBG);

        g2d.setColor(Menus.inactiveColor);
        g2d.fill(singlePlayerButton);
        g2d.fill(multiplayerButton);
        g2d.fill(settingsButton);
        g2d.fill(helpButton);
        g2d.fill(quitButton);

        if (state == STATE.SP)
        {
            g2d.setColor(Menus.activeColor);
            g2d.fill(singlePlayerButton);
        }
        else if (state == STATE.MP)
        {
            g2d.setColor(Menus.activeColor);
            g2d.fill(multiplayerButton);
        }
        else if (state == STATE.SET)
        {
            g2d.setColor(Menus.activeColor);
            g2d.fill(settingsButton);
        }
        else if (state == STATE.HELP)
        {
            g2d.setColor(Menus.activeColor);
            g2d.fill(helpButton);
        }
        else if (state == STATE.QUIT)
        {
            g2d.setColor(Menus.activeColor);
            g2d.fill(quitButton);
        }
        else if (state == STATE.OTHER)
        {
            g2d.setColor(Menus.inactiveColor);
            g2d.fill(singlePlayerButton);
            g2d.fill(multiplayerButton);
            g2d.fill(settingsButton);
            g2d.fill(helpButton);
            g2d.fill(quitButton);
        }

        Font fnt1 = new Font("arial", Font.BOLD, 30);
        g.setFont(fnt1);
        g.setColor(Color.white);
        g.drawString("Single player", singlePlayerButton.x + 54, singlePlayerButton.y + 35);
        g.drawString("Multiplayer", multiplayerButton.x + 66, multiplayerButton.y + 35);
        g.drawString("Settings", settingsButton.x + 90, settingsButton.y + 35);
        g.drawString("Help", helpButton.x + 118, helpButton.y + 35);
        g.drawString("Quit", quitButton.x + 118, quitButton.y + 35);
    }
}
