package server;

import pdu.PDU;
import pdu.pduTypes.ChNickPDU;
import pdu.pduTypes.JoinPDU;
import pdu.pduTypes.NicksPDU;
import pdu.pduTypes.QuitPDU;

import java.io.*;
import java.net.Socket;


/**
 * Created by id14llm on 2015-09-30.
 */
public class ClientThread implements Runnable{
    ChatServer server;
    Socket socket;
    String nickname;
    OutputStream out;

    public ClientThread(Socket socket, ChatServer server) {
        this.server = server;
        this.socket = socket;
        try {
            this.out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNickname() {
        return this.nickname;
    }

    @Override
    public void run() {
            try {
                PDU pdu = PDU.fromInputStream(socket.getInputStream());
                switch(pdu.toByteArray()[0]){
                    case 12:
                        handleJoin((JoinPDU) pdu);
                        break;

                    case 13:
                        handleChNick((ChNickPDU)pdu);
                        break;

                    default:
                        errorHandler();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    private void handleJoin(JoinPDU pdu) throws IOException{
        this.nickname = pdu.getNickname();
        server.registerNewClient(this);
        //out.write(new NicksPDU(server.getNicknames()).toByteArray());
    }

    private void handleChNick(ChNickPDU pdu) throws IOException{

    }

    private void errorHandler() throws IOException {
        out.write(new QuitPDU().toByteArray());
        server.disconnectClient(this);
    }
    public void sendPDU(PDU pdu) throws IOException{
        out.write(pdu.toByteArray());
    }
}
