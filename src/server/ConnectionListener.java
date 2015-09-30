package server;

import pdu.ByteSequenceBuilder;
import pdu.PDU;

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
            Socket socket = serverSocket.accept();
            if(socket.isConnected()){
                new ClientThread(socket, server);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
