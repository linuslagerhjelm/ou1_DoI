package server;

import pdu.PDU;
import pdu.pduTypes.NicksPDU;

import java.io.*;
import java.net.Socket;


/**
 * Created by id14llm on 2015-09-30.
 */
public class ClientThread {
    ChatServer server;
    String nickname;

    public ClientThread(Socket socket, ChatServer server) {
        this.server = server;
        try {
            DataInputStream dIn = new DataInputStream(socket.getInputStream());
            OutputStream out = socket.getOutputStream();
            byte[] message = PDU.readExactly(dIn, 8);

            PDU pdu = null;
            try{
                InputStream is = new ByteArrayInputStream(message);
                pdu = PDU.fromInputStream(is);
                if(pdu.toByteArray()[0] == 12){
                    server.registerNewClient(this);
                    int toread = pdu.toByteArray()[1];
                    PDU.readExactly(is, 4);
                    this.nickname = new String(PDU.readExactly(is, toread), "UTF-8");
                    out.write(new NicksPDU(server.getNicknames()).toByteArray());
                }
            } catch(Exception ignore) {}


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNickname() {
        return this.nickname;
    }

}
