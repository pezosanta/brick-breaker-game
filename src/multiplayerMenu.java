import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;

public class multiplayerMenu
{
    public static enum BUTTONSTATE {VS, COOP, BACK, OTHER};
    public static enum CLICKEDSTATE {CLICKED, OTHER};

    public static BUTTONSTATE buttonState       = BUTTONSTATE.OTHER;
    public static CLICKEDSTATE clickedState     = CLICKEDSTATE.OTHER;

    public static final int rectangleX          = GUI.WIDTH / 4;
    public static final int[] rectangleYArray   = new int[]{ 120, 175, 230 };
    public static final int rectangleWidth      = 300;
    public static final int rectangleHeight     = 50;

    private final Rectangle heroVsVillainButton = new Rectangle(rectangleX, rectangleYArray[0], rectangleWidth, rectangleHeight);
    private final Rectangle coopButton          = new Rectangle(rectangleX, rectangleYArray[1], rectangleWidth, rectangleHeight);
    private final Rectangle backButton          = new Rectangle(rectangleX, rectangleYArray[2], rectangleWidth, rectangleHeight);

    public void render(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Menus.inactiveColor);
        g2d.fill(heroVsVillainButton);
        g2d.fill(coopButton);
        g2d.fill(backButton);

        if (buttonState == BUTTONSTATE.VS)
        {
            g2d.setColor(Menus.activeColor);

            if (clickedState == CLICKEDSTATE.CLICKED) { g2d.setColor(Menus.clickedColor); }

            g2d.fill(heroVsVillainButton);
        }
        else if (buttonState == BUTTONSTATE.COOP)
        {
            g2d.setColor(Menus.activeColor);

            if (clickedState == CLICKEDSTATE.CLICKED) { g2d.setColor(Menus.clickedColor); }

            g2d.fill(coopButton);
        }
        else if (buttonState == BUTTONSTATE.BACK)
        {
            g2d.setColor(Menus.activeColor);

            if (clickedState == CLICKEDSTATE.CLICKED) { g2d.setColor(Menus.clickedColor); }

            g2d.fill(backButton);
        }
        else if (buttonState == BUTTONSTATE.OTHER)
        {
            g2d.setColor(Menus.inactiveColor);
            g2d.fill(heroVsVillainButton);
            g2d.fill(coopButton);
            g2d.fill(backButton);
        }

        Font fnt0 = new Font("arial", Font.BOLD, Menus.sizeTitle);
        g.setFont(fnt0);
        g.setColor(Color.black);
        g.drawString("BRICK BREAKER GAME", Menus.xTitle, Menus.yTitle);

        Font fnt1 = new Font("arial", Font.BOLD, 30);
        g.setFont(fnt1);
        g.setColor(Color.white);
        g.drawString("Hero vs Villain", heroVsVillainButton.x + 46, heroVsVillainButton.y + 35);
        g.drawString("CO-OP", coopButton.x + 98, coopButton.y + 35);
        g.drawString("Back", backButton.x + 112, backButton.y + 35);

        clickedState = CLICKEDSTATE.OTHER;
    }
}
