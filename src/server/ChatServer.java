package server;

import pdu.pduTypes.NicksPDU;
import pdu.pduTypes.UJoinPDU;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by id14llm on 2015-09-08.
 */
public class ChatServer {

    ConcurrentHashMap<String,ClientThread> connectedClients = new ConcurrentHashMap<>();
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



        //Håll koll på kön om något finns i kön skicka det

        sendHeartbeat.join();
        recieveConnections.join();
        System.out.println("finnish");
    }

        public void registerNewClient(ClientThread ct) {
            if(!connectedClients.containsKey(ct.getNickname())) {
                try {
                    Date timeStamp = new Date();
                    for(ClientThread t: connectedClients.values()){
                        t.sendPDU(new UJoinPDU(ct.getNickname(), timeStamp));
                        System.out.println("INNE");
                    }
                    connectedClients.putIfAbsent(ct.getNickname(), ct);
                    ct.sendPDU(new NicksPDU(connectedClients.keySet()));
                    System.out.println("UTE");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    public void disconnectClient(ClientThread ct) {
        connectedClients.remove(ct.getNickname());
    }

    public Set<String> getNicknames() {
        return connectedClients.keySet();
    }

    public void setId(short s) { this.id = s; }
    public short getId(){ return id; }
    public String getServerName() { return serverName; }
    public InetAddress getInetAddress() { return address; }
    public int getPort() { return port; }
    public int getClientCount() { return connectedClients.size(); }

    public static void main(String[] args) {
        try {
            String[] strings = {"localhost", "TAGGA FREDAG ", "8000"};
            ChatServer server = new ChatServer(strings);
        }
        catch (Exception ignore) {}
    }

}



