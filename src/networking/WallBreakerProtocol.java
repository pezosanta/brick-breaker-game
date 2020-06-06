package networking;

import java.io.*;
import java.net.InetAddress;
import java.util.function.Consumer;

public class WallBreakerProtocol {

    private InputStream inStream;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public WallBreakerProtocol(InputStream in, OutputStream out) {
        try {
            inStream = in;
            outputStream = new ObjectOutputStream(out);
            outputStream.flush();
            inputStream = new ObjectInputStream(in);
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

    public boolean isDataAvailable() {
        try {
            return inStream.available() > 0;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void close() {
        try {
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.print("I am ");
        System.out.println(WallBreakerProtocol.class);

        WallBreakerConnection wbhost = new WallBreakerConnection(true);
        WallBreakerConnection wbclient = new WallBreakerConnection(false);
        InetAddress hostAddress = wbhost.getAddress();

        Consumer<Boolean> connectionListener = new Consumer<Boolean>() {
            @Override
            public void accept(Boolean success) {
                System.out.println("Connection at host side was successful: " + success);
                if (!success) return;
                WallBreakerProtocol wbProtocol = new WallBreakerProtocol(wbhost.getInputStream(), wbhost.getOutputStream());

                System.out.println("Trying to get messages...");

                while (true) {
                    if (wbProtocol.isDataAvailable()) {
                        WBMessage msg = wbProtocol.readMessage();
                        if (msg == null) msg = new WBMessage(WBMessage.MsgType.EXITED, "");

                        System.out.println("Message type: " + msg.msg);
                        System.out.println("Message payload: " + msg.payload);
                        if (msg.msg == WBMessage.MsgType.EXITED) break;
                    }
                }

                System.out.println("Message receiving finished.");

                wbProtocol.close();
                wbhost.close();
            }
        };

        Thread tclient = new Thread(() -> {
            boolean success = wbclient.connect(hostAddress);
            System.out.println("Connection at client side was successful: " + success);
            if (!success) return;
            WallBreakerProtocol wbProtocol = new WallBreakerProtocol(wbclient.getInputStream(), wbclient.getOutputStream());

            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < 5; i++) {
                String messagePayload = ("" + i + ". számú üzenet.");
                wbProtocol.sendMessage(new WBMessage(WBMessage.MsgType.OK, messagePayload));
            }
            wbProtocol.sendMessage(new WBMessage(WBMessage.MsgType.EXITED, "exit"));

            System.out.println("Client: Messages sent.");
            wbProtocol.close();
            wbclient.close();
        });

        Thread thost = wbhost.waitForConnection(connectionListener);
        tclient.start();

        try {
            thost.join();
            tclient.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
