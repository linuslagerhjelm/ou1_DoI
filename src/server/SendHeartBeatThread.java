package server;

import pdu.pduTypes.AlivePDU;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by linuslagerhjelm on 15-09-28.
 */
public class SendHeartBeatThread implements Runnable{
    InetAddress address;
    int port;
    ChatServer chatServer;

    public SendHeartBeatThread(InetAddress address, int port, ChatServer cs){
        this.address = address;
        this.port = port;
        this.chatServer = cs;
    }

    @Override
    public void run() {
        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = new DatagramSocket();
            sendPackage(datagramSocket);
        } catch (SocketException ignore) { }

        while (true) {
            try {
                Thread.sleep(8000);
                sendPackage(datagramSocket);
            } catch (InterruptedException e) { }

        }
    }

    private void sendPackage(DatagramSocket dgs) {
        byte clientCount = (byte)chatServer.nrOfConnectedClients();
        short id = chatServer.getId();
        byte[] pdu =  new AlivePDU(clientCount, id).toByteArray();
        DatagramPacket p = new DatagramPacket(pdu, pdu.length, address, port);
        try {
            dgs.send(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
