import pdu.PDU;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by id14llm on 2015-09-08.
 */
public class ChatServer {


    Integer port;

    byte[] buffer = new byte[65536];


    public ChatServer(String args[]) throws IOException {

        InetAddress address = InetAddress.getByName(args[0]);

        DatagramSocket dataSocket = new DatagramSocket(port);

        DatagramPacket dataPacket = new DatagramPacket(buffer, buffer.length);

        //PDU.fromInputStream(InputStream.)

        while (true) {
            dataSocket.receive(dataPacket);
            DatagramPacket dataOut = new DatagramPacket(buffer, buffer.length, dataPacket.getAddress(), dataPacket.getPort());
            dataSocket.send(dataOut);
        }


    }

    public void sendHeartBeat() {


    }
}


/*
        /*ServerSocket serverSocket = new ServerSocket(IP, port);
        Socket socket = serverSocket.accept();

        InputStream in = socket.getInputStream();

        Scanner scanner = new Scanner(in);
*/




