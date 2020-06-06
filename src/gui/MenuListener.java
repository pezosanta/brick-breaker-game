package gui;

import networking.WallBreakerConnection;

public interface MenuListener
{
    void gamePaintHandler();
    void menuPaintHandler();
    void menuSwitchHandler(menuHandler.MENUSTATE newMenuState);
    void mpSwitchHandler(menuHandler.MENUSTATE newMenuState, WallBreakerConnection wbConnection, boolean isServer);
}
