package server;

import pdu.PDU;
import pdu.pduTypes.ChNickPDU;
import pdu.pduTypes.JoinPDU;
import pdu.pduTypes.NicksPDU;

import java.io.*;
import java.net.Socket;


/**
 * Created by id14llm on 2015-09-30.
 */
public class ClientThread implements Runnable{
    ChatServer server;
    Socket socket;
    String nickname;

    public ClientThread(Socket socket, ChatServer server) {
        this.server = server;
        this.socket = socket;

    }

    public String getNickname() {
        return this.nickname;
    }

    @Override
    public void run() {
            try {
                OutputStream out = socket.getOutputStream();
                PDU pdu = PDU.fromInputStream(socket.getInputStream());
                switch(pdu.toByteArray()[0]){
                    case 12:
                        handleJoin((JoinPDU) pdu, out);
                        break;

                    case 13:
                        handleChNick((ChNickPDU)pdu, out);
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    private void handleJoin(JoinPDU pdu,OutputStream out) throws IOException{
        this.nickname = pdu.getNickname();
        out.write(new NicksPDU(server.getNicknames()).toByteArray());
    }

    private void handleChNick(ChNickPDU pdu,OutputStream out) throws IOException{

    }
}
