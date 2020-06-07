package gui;

import networking.WallBreakerConnection;

import javax.swing.*;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.*;
import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

public class serverModeMenu extends Menu implements ActionListener
{

    private final WallBreakerConnection wbHost;

    private enum BUTTONSTATE {BACK, OTHER}

    private CLICKEDSTATE clickedState               = CLICKEDSTATE.OTHER;
    private BUTTONSTATE buttonState                 = BUTTONSTATE.OTHER;
    private final BUTTONSTATE[] buttonStateArray    = new BUTTONSTATE[]{BUTTONSTATE.BACK, BUTTONSTATE.OTHER};

    private final int[] rectangleYArray             = new int[]{400};

    private final Rectangle backButton              = new Rectangle(super.rectangleX, rectangleYArray[0], super.rectangleWidth, super.rectangleHeight);

    private String ipAddress;
    private Timer timer;
    private int delay = 1000;
    private int counter = 60;

    private Thread thost;

    public serverModeMenu()
    {
        ipAddress = getIPAddress();
        timer = new Timer(delay,this);
        timer.start();
        wbHost = new WallBreakerConnection(true);
        thost = wbHost.waitForConnection(this::connectionListener);
    }

    private void connectionListener(boolean hasConnected) {
        timer.stop();

        if (!hasConnected) {
            wbHost.close();
        } else {
            // Connection successful
            listeners.forEach(menuListener -> menuListener.mpSwitchHandler(menuHandler.MENUSTATE.MULTIPLAYERGAME,
                    wbHost, true));
        }
    }

    private String getIPAddress() {
        try {
            return getFirstNonLoopbackAddress(true, false).getHostAddress();
        } catch (SocketException e) {
            e.printStackTrace();
            return "";
        }
    }

    // from: https://stackoverflow.com/a/901943
    private static InetAddress getFirstNonLoopbackAddress(boolean preferIpv4, boolean preferIPv6) throws SocketException {
        Enumeration en = NetworkInterface.getNetworkInterfaces();
        while (en.hasMoreElements()) {
            NetworkInterface i = (NetworkInterface) en.nextElement();
            for (Enumeration en2 = i.getInetAddresses(); en2.hasMoreElements();) {
                InetAddress addr = (InetAddress) en2.nextElement();
                if (!addr.isLoopbackAddress()) {
                    if (addr instanceof Inet4Address) {
                        if (preferIPv6) {
                            continue;
                        }
                        return addr;
                    }
                    if (addr instanceof Inet6Address) {
                        if (preferIpv4) {
                            continue;
                        }
                        return addr;
                    }
                }
            }
        }
        return null;
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
            case BACK:
                if (clickedState == CLICKEDSTATE.CLICKED) { g2d.setColor(super.clickedColor); }
                else { g2d.setColor(super.activeColor); }

                g2d.fill(backButton);
                break;

            default:
                g2d.setColor(super.inactiveColor);
                g2d.fill(backButton);
        }

        Font fnt1 = new Font(super.fontStyle, Font.BOLD, super.fontSize);
        g.setFont(fnt1);
        g.setColor(super.fontColor);
        g.drawString("Back", backButton.x + 112, backButton.y + 35);

        g.setColor(super.inactiveColor);
        g.drawString("Waiting for Client to connect: " + counter, backButton.x - 100, backButton.y - 200);
        g.drawString("Your IP address: " + ipAddress, backButton.x - 80, backButton.y - 130);

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
        if ((super.rectangleX + super.rectangleWidth) >= e.getX() && e.getX() >= super.rectangleX)
        {
            if ((rectangleYArray[0] + super.rectangleHeight) >= e.getY() && e.getY() >= rectangleYArray[0])
            {
                timer.stop();
                wbHost.close();
                for (MenuListener hl : listeners)
                {
                    hl.menuSwitchHandler(menuHandler.MENUSTATE.MULTIPLAYER);
                }
            }
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
            timer.stop();
            wbHost.close();
            for (MenuListener hl : listeners)
            {
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
