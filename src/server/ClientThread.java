package server;

import pdu.ByteSequenceBuilder;
import pdu.PDU;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by id14llm on 2015-09-30.
 */
public class ClientThread {
    ChatServer server;
    public ClientThread(Socket socket, ChatServer server) {
        this.server = server;

        InputStream inputStream = null;
        try {
            inputStream = socket.getInputStream();

            //Scanner scanner = new Scanner(inputStream);

            DataInputStream dIn = new DataInputStream(socket.getInputStream());
            System.out.println("Line 28");
                byte[] message = new byte[100];
                System.out.println("line 32");
                dIn.readFully(message); // read the message
                System.out.println("34");
            System.out.println("Line 36");
            PDU pdu = null;
            try{
                pdu = PDU.fromInputStream(dIn);
                if(pdu.toByteArray()[0] == 12){
                    System.out.println("Recieved the pdu");
                    server.registerNewClient(this);
                }
            } catch(Exception ignore) {
                System.out.println("In catch block");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
