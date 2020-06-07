package gui;

import networking.WallBreakerConnection;

import java.io.InputStream;

public interface MenuListener
{
    void gamePaintHandler();
    void menuPaintHandler();
    void menuSwitchHandler(menuHandler.MENUSTATE newMenuState);
    void spSwitchHandler(menuHandler.MENUSTATE newMenuState, InputStream fileStream);
    void mpSwitchHandler(menuHandler.MENUSTATE newMenuState, WallBreakerConnection wbConnection, boolean isServer);
}
