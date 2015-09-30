package server;

import java.io.IOException;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by id14llm on 2015-09-08.
 */
public class ChatServer {

    CopyOnWriteArrayList<ClientThread> connectedClient = new CopyOnWriteArrayList<>();
    private int clientCount;
    private short id;
    int port;
    String serverName;
    InetAddress address;

    public ChatServer(String args[]) throws IOException, InterruptedException {

        clientCount = 0;
        address = InetAddress.getByName(args[0]);
        serverName = args[1];
        port = Integer.parseInt(args[2]);

        //Initialize all threads
        SendHeartBeatThread sendHb = new SendHeartBeatThread(this);
        ConnectionListener connectionListener = new ConnectionListener(this);

        Thread recieveConnections = new Thread(connectionListener);
        Thread sendHeartbeat = new Thread(sendHb);

        sendHeartbeat.start();
        recieveConnections.start();

        sendHeartbeat.join();
        recieveConnections.join();
        System.out.println("finnish");
    }

    public void registerNewClient(ClientThread ct) {
        connectedClient.add(ct);
        ++this.clientCount;
        System.out.println("Something happened");
    }

    public int nrOfConnectedClients(){ return clientCount; }
    public short getId(){ return id; }
    public String getServerName() { return serverName; }
    public InetAddress getInetAddress() { return address; }
    public int getPort() { return port; }
    public int getClientCount() { return clientCount; }

    public static void main(String[] args) {
        try {
            String[] strings = {"localhost", "Jakub", "8000"};
            ChatServer server = new ChatServer(strings);
        }
        catch (Exception ignore) {}
    }

}



