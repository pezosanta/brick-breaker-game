package game;

import gui.MenuListener;
import gui.menuHandler;
import networking.GameStateMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class GameClient implements KeyListener {

    private GameMap map = new GameMap();
    private Queue<KeyEventExt> keyEvents = new LinkedList<>();

    private ObjectInputStream inStream;
    private ObjectOutputStream outStream;

    private boolean isStarted = false;
    private boolean hasEnded = false;
    private int score = 0;
    private Timer timer = new Timer(25, this::clientLoop);

    public void sendTestMsg() {
        try {
            outStream.writeObject("Hello Marcika ez a kliens!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTestMsg() {
        try {
            String msg = (String) inStream.readObject();
            return msg;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public java.util.List<MenuListener> listeners = new ArrayList<MenuListener>();
    public void addListener(MenuListener listenerToAdd) {
        listeners.add(listenerToAdd);
    }

    public GameClient(ObjectInputStream clientInput, ObjectOutputStream clientOutput) {
        this.inStream = clientInput;
        this.outStream = clientOutput;

        timer.start();
    }

    public void reset() {
        timer.stop();
        isStarted = false;
        hasEnded = false;
        keyEvents.clear();
        score = 0;

        timer.restart();
    }

    public void finalizeClient() {
        timer.stop();
        try {
            inStream.close();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void render(Graphics g){
        //background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, map.panelWidth, map.panelHeight);

        //drawing map
        map.draw((Graphics2D) g);

        //scores
        g.setColor(Color.white);
        g.setFont(new Font("serif",Font.BOLD,25));
        g.drawString(""+score,map.panelWidth*4/5,25+2*map.wallWidth);

        if (!isStarted) {
            drawStartInstruction(g);
        }

        if (hasEnded) {
            drawGameResult(g, map.getAvailableBricks() == 0);
        }

        g.dispose();
    }

    void drawStartInstruction(Graphics g) {
        g.setFont(new Font("serif",Font.BOLD,25));
        g.drawString("Press an arrow to start",230,350);
    }

    void drawGameResult(Graphics g, boolean hasWon) {
        g.setColor(Color.RED);
        g.setFont(new Font("serif",Font.BOLD,25));
        if (hasWon) {
            g.drawString("You Won!",260,300);
        } else {
            g.drawString("Game Over, Your score: " + String.valueOf(score), 190, 300);

        }

        g.setFont(new Font("serif",Font.BOLD,25));
        g.drawString("Press Enter to Restart",230,350);
    }

    void clientLoop(ActionEvent actionEvent) {
        System.out.println("client loop");
        try {
            // Receive states
            while (inStream.available() > 0) {
                GameStateMessage msg = (GameStateMessage) inStream.readObject();
                System.out.println("Client received msg: " + msg);
                if (msg == null) break;
                switch (msg) {
                    case RESET:
                        reset();
                    case START:
                        isStarted = true;
                    case MAP_PAYLOAD:
                        map = (GameMap) inStream.readObject();
                        if (map == null) {
                            finalizeClient();
                            throw new RuntimeException("Received GameMap is null!");
                        }
                        //outStream.writeObject(GameStateMessage.OK); //TODO
                        break;
                    case GAME_FINISHED:
                        hasEnded = true;
                    default:
                    case EXITED:
                        finalizeClient();
                        hasEnded = true;
                        System.out.println("Server has exited the game.");
                        listeners.forEach(hl -> hl.menuSwitchHandler(menuHandler.MENUSTATE.MAIN));
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            // Send user inputs to server
            while (!keyEvents.isEmpty()) {
                KeyEventExt event = keyEvents.poll();
                    outStream.writeObject(event);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        listeners.forEach(MenuListener::gamePaintHandler);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        KeyEventExt keyEvent = new KeyEventExt(e.getKeyCode(), KeyEventType.RELEASED);
        keyEvents.add(keyEvent);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        KeyEventExt keyEvent = new KeyEventExt(e.getKeyCode(), KeyEventType.PRESSED);
        keyEvents.add(keyEvent);
        // TODO: enter and escape handling? (Non-game characters, breaks loop)
    }

    class KeyEventExt {
        int keyCode;
        KeyEventType keyEventType;

        public KeyEventExt(int keyCode, KeyEventType keyEventType) {
            this.keyCode = keyCode;
            this.keyEventType = keyEventType;
        }
    }

    enum KeyEventType {
        PRESSED,
        RELEASED,
        NONE
    }
}
