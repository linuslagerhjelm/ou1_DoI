package server;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by id14llm on 2015-09-08.
 */
public class ChatServer {

    CopyOnWriteArrayList<Thread> connectedClient = new CopyOnWriteArrayList<>();
    private short id;
    int port;
    String serverName;
    InetAddress address;

    public ChatServer(String args[]) throws IOException, InterruptedException {

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

    public void registerNewClient(Thread ct) {
        connectedClient.add(ct);
    }

    public Set<String> getNicknames() {
        Set<String> nicks = new HashSet<>();
        //TODO: fix this
        /*for(Thread ct: connectedClient){
            nicks.add(ct.getNickname());
        }*/
        System.out.println(nicks.toString());
        return nicks;
    }

    public void setId(short s) { this.id = s; }
    public short getId(){ return id; }
    public String getServerName() { return serverName; }
    public InetAddress getInetAddress() { return address; }
    public int getPort() { return port; }
    public int getClientCount() { return connectedClient.size(); }

    public static void main(String[] args) {
        try {
            String[] strings = {"localhost", "Jakub", "8000"};
            ChatServer server = new ChatServer(strings);
        }
        catch (Exception ignore) {}
    }

}



