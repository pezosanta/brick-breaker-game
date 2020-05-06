import com.sun.tools.javac.Main;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;

public class mainMenu
{
    public static enum BUTTONSTATE {SP, MP, SET, HELP, QUIT, OTHER};
    public static enum CLICKEDSTATE {CLICKED, OTHER};

    public static BUTTONSTATE buttonState       = BUTTONSTATE.OTHER;
    public static CLICKEDSTATE clickedState     = CLICKEDSTATE.OTHER;

    public static final int rectangleX          = GUI.WIDTH / 4;
    public static final int[] rectangleYArray   = new int[]{ 120, 175, 230, 285, 340 };
    public static final int rectangleWidth      = 300;
    public static final int rectangleHeight     = 50;

    private final Rectangle singlePlayerButton  = new Rectangle(rectangleX, rectangleYArray[0], rectangleWidth, rectangleHeight);
    private final Rectangle multiplayerButton   = new Rectangle(rectangleX, rectangleYArray[1], rectangleWidth, rectangleHeight);
    private final Rectangle settingsButton      = new Rectangle(rectangleX, rectangleYArray[2], rectangleWidth, rectangleHeight);
    private final Rectangle helpButton          = new Rectangle(rectangleX, rectangleYArray[3], rectangleWidth, rectangleHeight);
    private final Rectangle quitButton          = new Rectangle(rectangleX, rectangleYArray[4], rectangleWidth, rectangleHeight);


    public void render(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Menus.inactiveColor);
        g2d.fill(singlePlayerButton);
        g2d.fill(multiplayerButton);
        g2d.fill(settingsButton);
        g2d.fill(helpButton);
        g2d.fill(quitButton);

        if (buttonState == BUTTONSTATE.SP)
        {
            g2d.setColor(Menus.activeColor);

            if (clickedState == CLICKEDSTATE.CLICKED) { g2d.setColor(Menus.clickedColor); }

            g2d.fill(singlePlayerButton);
        }
        else if (buttonState == BUTTONSTATE.MP)
        {
            g2d.setColor(Menus.activeColor);

            if (clickedState == CLICKEDSTATE.CLICKED) { g2d.setColor(Menus.clickedColor); }

            g2d.fill(multiplayerButton);
        }
        else if (buttonState == BUTTONSTATE.SET)
        {
            g2d.setColor(Menus.activeColor);

            if (clickedState == CLICKEDSTATE.CLICKED) { g2d.setColor(Menus.clickedColor); }

            g2d.fill(settingsButton);
        }
        else if (buttonState == BUTTONSTATE.HELP)
        {
            g2d.setColor(Menus.activeColor);

            if (clickedState == CLICKEDSTATE.CLICKED) { g2d.setColor(Menus.clickedColor); }

            g2d.fill(helpButton);
        }
        else if (buttonState == BUTTONSTATE.QUIT)
        {
            g2d.setColor(Menus.activeColor);

            if (clickedState == CLICKEDSTATE.CLICKED) { g2d.setColor(Menus.clickedColor); }

            g2d.fill(quitButton);
        }
        else if (buttonState == BUTTONSTATE.OTHER)
        {
            g2d.setColor(Menus.inactiveColor);
            g2d.fill(singlePlayerButton);
            g2d.fill(multiplayerButton);
            g2d.fill(settingsButton);
            g2d.fill(helpButton);
            g2d.fill(quitButton);
        }

        Font fnt0 = new Font("arial", Font.BOLD, Menus.sizeTitle);
        g.setFont(fnt0);
        g.setColor(Color.black);
        g.drawString("BRICK BREAKER GAME", Menus.xTitle, Menus.yTitle);

        Font fnt1 = new Font("arial", Font.BOLD, 30);
        g.setFont(fnt1);
        g.setColor(Color.white);
        g.drawString("Single player", singlePlayerButton.x + 54, singlePlayerButton.y + 35);
        g.drawString("Multiplayer", multiplayerButton.x + 70, multiplayerButton.y + 35);
        g.drawString("Settings", settingsButton.x + 90, settingsButton.y + 35);
        g.drawString("Help", helpButton.x + 118, helpButton.y + 35);
        g.drawString("Quit", quitButton.x + 118, quitButton.y + 35);

        clickedState = CLICKEDSTATE.OTHER;
    }
}
