package gui;

import javax.swing.*;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class clientModeMenu extends Menu implements ActionListener
{
    private enum BUTTONSTATE {CONNECT, BACK, OTHER}

    private CLICKEDSTATE clickedState               = CLICKEDSTATE.OTHER;
    private BUTTONSTATE buttonState                 = BUTTONSTATE.OTHER;
    private final BUTTONSTATE[] buttonStateArray1   = new BUTTONSTATE[]{ BUTTONSTATE.BACK, BUTTONSTATE.OTHER};
    private final BUTTONSTATE[] buttonStateArray2   = new BUTTONSTATE[]{ BUTTONSTATE.CONNECT, BUTTONSTATE.OTHER};

    private final int[] rectangleYArray             = new int[]{400};
    private final int[] connectYArray               = new int[]{150};

    private final Rectangle connectButton           = new Rectangle(super.xTitle + 400, connectYArray[0], 192, super.rectangleHeight);
    private final Rectangle backButton              = new Rectangle(super.rectangleX, rectangleYArray[0], super.rectangleWidth, super.rectangleHeight);

    private List<String> buttonNames = new ArrayList<String>(Arrays.asList("connect", "back"));

    private Boolean startConnecting = false;

    private Timer timer;
    private int delay = 1000;
    private int counter = 60;

    private JTextField ipTextField;

    public clientModeMenu()
    {
        createJTextField();
        timer = new Timer(delay,this);
    }

    private void createJTextField()
    {
        Font ipFont = new Font(super.fontStyle, Font.BOLD, super.fontSize);
        ipTextField = new JTextField("Enter server IP address.");
        ipTextField.setBounds(super.xTitle,150,392,50); //592
        ipTextField.setFont(ipFont);
    }

    public JTextField getJTextField()
    {
        return ipTextField;
    }

    public List<Integer> getTextWidth(Graphics g)
    {
        List<Integer> textWidths = new ArrayList<Integer>();

        for (String buttonName : buttonNames)
        {
            String buttonNameUpperCase = buttonName.toUpperCase();
            textWidths.add(g.getFontMetrics().stringWidth(buttonNameUpperCase));
        }

        return textWidths;
    }

    @Override
    public void render(Graphics g)
    {
        super.render(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(super.inactiveColor);
        g2d.fill(backButton);

        switch(buttonState)
        {
            case CONNECT:
                if (clickedState == CLICKEDSTATE.CLICKED) { g2d.setColor(super.clickedColor); }
                else { g2d.setColor(super.activeColor); }

                g2d.fill(connectButton);
                break;

            case BACK:
                if (clickedState == CLICKEDSTATE.CLICKED) { g2d.setColor(super.clickedColor); }
                else { g2d.setColor(super.activeColor); }

                g2d.fill(backButton);
                break;

            default:
                g2d.setColor(super.inactiveColor);
                g2d.fill(connectButton);
                g2d.fill(backButton);
        }

        Font fnt1 = new Font(super.fontStyle, Font.BOLD, super.fontSize);
        g.setFont(fnt1);
        g.setColor(super.fontColor);
        List<Integer> textWidths = getTextWidth(g);
        g.drawString(buttonNames.get(0).toUpperCase(), connectButton.x + ((connectButton.width - textWidths.get(0)) / 2), connectButton.y + 35);
        g.drawString(buttonNames.get(1).toUpperCase(), backButton.x + ((backButton.width - textWidths.get(1)) / 2), backButton.y + 35);

        if (startConnecting)
        {
            g.setColor(super.inactiveColor);
            g.drawString("Connecting to the server: " + counter, backButton.x - 60, backButton.y - 100);
        }

        clickedState = CLICKEDSTATE.OTHER;
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        if ((backButton.x + backButton.width) >= e.getX() && e.getX() >= backButton.x )
        {
            buttonState = buttonStateArray1[super.mouseMovedHelper(rectangleYArray, e.getY())];
        }
        if ((connectButton.x + connectButton.width) >= e.getX() && e.getX() >= connectButton.x)
        {
           buttonState = buttonStateArray2[super.mouseMovedHelper(connectYArray, e.getY())];
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
        if ((connectButton.x + connectButton.width) >= e.getX() && e.getX() >= connectButton.x)
        {
            clickedState = super.clickedStateArray[super.mousePressedHelper(connectYArray, e.getY())];
        }
        else
        {
            clickedState = CLICKEDSTATE.OTHER;
        }

        for (MenuListener hl : listeners)
        {
            hl.menuPaintHandler();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        clickedState = CLICKEDSTATE.OTHER;
        for (MenuListener hl : listeners)
        {
            hl.menuPaintHandler();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        if ((backButton.x + backButton.width) >= e.getX() && e.getX() >= backButton.x && (backButton.y + backButton.height) >= e.getY() && e.getY() >= backButton.y)
        {
            for (MenuListener hl : listeners)
            {
                timer.stop();
                hl.menuSwitchHandler(menuHandler.MENUSTATE.MULTIPLAYER);
            }
        }
        else if ((connectButton.x + connectButton.width) >= e.getX() && e.getX() >= connectButton.x && (connectButton.y + connectButton.height) >= e.getY() && e.getY() >= connectButton.y)
        {
            startConnecting = true;
            timer.start();
            System.out.println(ipTextField.getText());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(counter > 0)
        {
            counter --;
            for (MenuListener hl : listeners)
            {
                hl.menuPaintHandler();
            }
        }
        else
        {
            for (MenuListener hl : listeners)
            {
                timer.stop();
                hl.menuSwitchHandler(menuHandler.MENUSTATE.MULTIPLAYER);
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
