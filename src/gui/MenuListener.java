package gui;

public interface MenuListener
{
    void gamePaintHandler();
    void menuPaintHandler();
    void menuSwitchHandler(menuHandler.MENUSTATE newMenuState);
}
