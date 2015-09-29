package server;


import pdu.pduTypes.AlivePDU;
import pdu.pduTypes.RegPDU;

import java.net.*;

/**
 * Created by linuslagerhjelm on 15-09-28.
 */
public class SendHeartBeatThread implements Runnable{
    private int chatServerPort;
    private ChatServer chatServer;
    private InetAddress nameServerAddress;
    private int nameServerPort = 1337;
    byte[] buffer = new byte[65536];

    public SendHeartBeatThread(ChatServer cs){
        this.chatServerPort = cs.getPort();
        this.chatServer = cs;
        try {
            nameServerAddress = InetAddress.getByName("itchy.cs.umu.se");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.setSoTimeout(10000);
            sendRegPdu(datagramSocket);
            while (true){
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                try{
                    datagramSocket.receive(packet);
                } catch(SocketTimeoutException e){
                    sendRegPdu(datagramSocket);
                }
                byte[] packetByte = packet.getData();
                if(packetByte[0] == 1)
                    sendHeartBeat(datagramSocket);
                else if(packetByte[0] == 100)
                    sendRegPdu(datagramSocket);

                Thread.sleep(8000);
            }

        } catch (Exception ignore) {
            ignore.printStackTrace();
        }

    }

    private void sendRegPdu(DatagramSocket socket) throws Exception{
        byte[] pdu = new RegPDU(chatServer.getServerName(), (short)chatServerPort).toByteArray();
        DatagramPacket UDPpacket = new DatagramPacket(pdu, pdu.length,nameServerAddress, nameServerPort);

        socket.send(UDPpacket);
    }

    private void sendHeartBeat(DatagramSocket socket) throws Exception{
        byte[] pdu = new AlivePDU((byte)chatServer.getClientCount(), chatServer.getId()).toByteArray();
        DatagramPacket UDPpacket = new DatagramPacket(pdu, pdu.length,nameServerAddress, nameServerPort);

        socket.send(UDPpacket);
    }
}
