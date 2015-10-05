package server;

import pdu.PDU;
import pdu.pduTypes.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;


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
                while(true) {
                    PDU pdu = PDU.fromInputStream(socket.getInputStream());
                    if(pdu instanceof JoinPDU)
                        handleJoin((JoinPDU) pdu);
                    else if(pdu instanceof ChNickPDU)
                        handleChNick((ChNickPDU) pdu);
                    else if(pdu instanceof MessagePDU){
                        handleMessage((MessagePDU) pdu);
                    }
                    else if(pdu instanceof QuitPDU)
                        handleQuit();

                    else errorHandler();
                }
            }catch (SocketException ex){
                try {
                    handleQuit();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
    }
    private void handleJoin(JoinPDU pdu) throws IOException{
        this.nickname = pdu.getNickname();
        server.registerNewClient(this);
    }
    private void handleChNick(ChNickPDU pdu) throws Exception{
        server.changeNick(nickname, pdu.getNickname());
        this.nickname = pdu.getNickname();
    }
    private void handleMessage(MessagePDU pdu) throws IOException{
        try {
            MessagePDU mesFromServer = new MessagePDU(pdu.getMessage(), this.nickname, new Date());
            server.queueEvent(mesFromServer);
        } catch (Exception e) { e.printStackTrace(); }
    }
    private void errorHandler() throws IOException {
        out.write(new QuitPDU().toByteArray());
        server.disconnectClient(this);
    }
    private void handleQuit() throws IOException {
        server.disconnectClient(this);
    }
    public void sendPDU(PDU pdu) throws IOException{
        out.write(pdu.toByteArray());
    }
}
