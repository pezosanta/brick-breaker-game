package networking;

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

    public boolean sendMessage(WBMessage msg) {
        try {
            outputStream.writeObject(msg);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public WBMessage readMessage() {
        try {
            return (WBMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean dataAvailable() {
        try {
            return inputStream.available() > 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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
