package networking;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class WallBreakerConnection {
    static final int PORT = 31013;
    private ServerSocket serverSocket;
    private Socket socket;

    public WallBreakerConnection(boolean isServer) {
        socket = null;
        if (isServer) {
            try {
                serverSocket = new ServerSocket(PORT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean waitForConnection() {
        if (serverSocket == null) {
            throw new RuntimeException("This object is not initialized as server!");
        }

        try {
            //serverSocket.setSoTimeout(10000);
            socket = serverSocket.accept();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean connect(InetAddress ipAddress) {
        try {
            socket = new Socket(ipAddress, PORT);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected();
    }

    public InetAddress getAddress() {
        if (serverSocket != null) {
            return serverSocket.getInetAddress();
        } else {
            return socket.getInetAddress();
        }
    }

    public InputStream getInputStream() {
        try {
            return socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public OutputStream getOutputStream() {
        try {
            return socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void close() {
        try {
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.print("I am ");
        System.out.println(WallBreakerConnection.class);

        WallBreakerConnection wbhost = new WallBreakerConnection(true);
        WallBreakerConnection wbclient = new WallBreakerConnection(false);
        InetAddress hostAddress = wbhost.getAddress();

        Thread thost = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean success = wbhost.waitForConnection();
                System.out.println("Connection at host side was successful: " + success);

                try (ObjectInputStream inStream = new ObjectInputStream(wbhost.getInputStream())) {
                    System.out.println("Trying to get messages...");

                    while (true) {
                        if (inStream.available() > 0) {
                            String message = inStream.readUTF();
                            System.out.println(message);
                            if (message.contains("exit"))
                                break;
                        }
                    }

                    System.out.println("Message receiving finished.");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                wbhost.close();
            }
        });
        Thread tclient = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean success = wbclient.connect(hostAddress);
                System.out.println("Connection at client side was successful: " + success);

                try {
                    ObjectOutputStream outStream = new ObjectOutputStream(wbclient.getOutputStream());
                    for (int i = 0; i < 5; i++) {
                        outStream.writeUTF("" + i + ". számú üzenet.");
                    }
                    outStream.writeUTF("exit");
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                wbclient.close();
            }
        });

        thost.start();
        tclient.start();

        try {
            thost.join();
            tclient.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
