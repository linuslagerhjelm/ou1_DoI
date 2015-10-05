package server;

import pdu.PDU;
import pdu.pduTypes.NicksPDU;
import pdu.pduTypes.UCNickPDU;
import pdu.pduTypes.UJoinPDU;
import pdu.pduTypes.ULeavePDU;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by id14llm on 2015-09-08.
 */
public class ChatServer {

    private ConcurrentHashMap<String,ClientThread> connectedClients = new ConcurrentHashMap<>();
    private LinkedBlockingQueue<PDU> queuedEvents = new LinkedBlockingQueue<>();
    private short id;
    private int port;
    private String serverName;
    private InetAddress address;
    private Thread messageHelperThread;

    public ChatServer(String args[]) throws IOException, InterruptedException {

        address = InetAddress.getByName(args[0]);
        serverName = args[1];
        port = Integer.parseInt(args[2]);

        //Initialize all threads
        SendHeartBeatThread sendHb = new SendHeartBeatThread(this);
        ConnectionListener connectionListener = new ConnectionListener(this);
        MessageHelper messageHelper = new MessageHelper(this);

        Thread recieveConnections = new Thread(connectionListener);
        Thread sendHeartbeat = new Thread(sendHb);
        this.messageHelperThread = new Thread(messageHelper);

        sendHeartbeat.start();
        recieveConnections.start();
        messageHelperThread.start();

        //Håll koll på kön om något finns i kön skicka det

        sendHeartbeat.join();
        recieveConnections.join();
        messageHelperThread.join();
        System.out.println("finnish");
    }

    public void registerNewClient(ClientThread ct) {
        if(!connectedClients.containsKey(ct.getNickname())) {
            Date timeStamp = new Date();
            try {
                queueEvent(new UJoinPDU(ct.getNickname(), timeStamp));

                //TODO: Make a nicer implementation than thread sleep
                Thread.sleep(100);
                connectedClients.put(ct.getNickname(), ct);
                ct.sendPDU(new NicksPDU(connectedClients.keySet()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void changeNick(String oldNick, String newNick){
        ClientThread temp = connectedClients.get(oldNick);
        connectedClients.remove(oldNick);
        connectedClients.putIfAbsent(newNick, temp);
        try {
            queuedEvents.put(new UCNickPDU(new Date(), oldNick, newNick));
        } catch (InterruptedException e) { e.printStackTrace(); }
    }
    public void disconnectClient(ClientThread ct) {
        connectedClients.remove(ct.getNickname());
        try {
            queuedEvents.put(new ULeavePDU(ct.getNickname(), new Date()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void queueEvent(PDU event) throws Exception{
        queuedEvents.put(event);
    }
    public void setId(short s) { this.id = s; }
    public short getId(){ return id; }
    public String getServerName() { return serverName; }
    public InetAddress getInetAddress() { return address; }
    public int getPort() { return port; }
    public int getClientCount() { return connectedClients.size(); }
    public LinkedBlockingQueue<PDU> getQueuedEvents() { return queuedEvents; }
    public ConcurrentHashMap<String,ClientThread> getConnectedClients() { return connectedClients; }
    public void dequeueEvent(PDU pdu) {
        queuedEvents.remove(pdu);
    }

    public static void main(String[] args) {
        try {
            String[] strings = {"localhost", "E d fest?", "8000"};
            ChatServer server = new ChatServer(strings);
        }
        catch (Exception ignore) {}
    }

}



