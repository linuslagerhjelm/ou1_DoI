package server;

import pdu.PDU;
import pdu.pduTypes.RegPDU;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by id14llm on 2015-09-08.
 */
public class ChatServer {

    private int clientCount;
    private short id;
    int port;
    String serverName;
    InetAddress address;
    List<Thread> activeThreads = new LinkedList<>();

    public ChatServer(String args[]) throws IOException, InterruptedException {

        clientCount = 0;
        address = InetAddress.getByName(args[0]);
        serverName = args[1];
        port = Integer.parseInt(args[2]);

        SendHeartBeatThread sendHb = new SendHeartBeatThread(this);
        Thread sendHeartbeat = new Thread(sendHb);
        sendHeartbeat.start();
        sendHeartbeat.join();
        System.out.println("finnish");
    }


    public int nrOfConnectedClients(){ return clientCount; }
    public short getId(){ return id; }
    public String getServerName() { return serverName; }
    public InetAddress getInetAddress() { return address; }
    public int getPort() { return port; }
    public int getClientCount() { return clientCount; }
    public void increaseClientCount(){ this.clientCount++; }

    public static void main(String[] args) {
        try {
            String[] strings = {"localhost", "Jakub", "8000"};
            ChatServer server = new ChatServer(strings);
        }
        catch (Exception ignore) {}
    }

}



