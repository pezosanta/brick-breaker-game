package networking;

import game.GameMap;

import java.io.*;

public class WallBreakerProtocol {

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public WallBreakerProtocol(InputStream in, OutputStream out) {
        try {
            inputStream = new ObjectInputStream(in);
            outputStream = new ObjectOutputStream(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean sendMessage(GameStateMessage msg) {
        try {
            outputStream.writeObject(msg);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean sendObject(Serializable obj) {
        try {
            outputStream.writeObject(obj);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public GameStateMessage readMessage() {
        try {
            return (GameStateMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public GameMap readMap() {
        try {
            return (GameMap) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close() {
        try {
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
