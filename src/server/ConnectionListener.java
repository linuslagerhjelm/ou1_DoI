package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by id14llm on 2015-09-29.
 */
public class ConnectionListener implements Runnable {

    ChatServer server;

    public ConnectionListener(ChatServer s){
        this.server = s;
    }
    @Override
    public void run() {
            try {
                ServerSocket serverSocket = new ServerSocket(server.getPort());
                while (true){
                    Socket socket = serverSocket.accept();
                    if(socket.isConnected()){
                        Thread t = new Thread(new ClientThread(socket, server));
                        t.start();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
