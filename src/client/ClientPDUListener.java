package client;

import gui.ChatModule;
import pdu.PDU;
import pdu.pduTypes.MessagePDU;
import pdu.pduTypes.UCNickPDU;
import pdu.pduTypes.UJoinPDU;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by id14llm on 2015-10-01.
 */
public class ClientPDUListener implements Runnable{

    String nickname;
    ChatModule module;
    InputStream in;
    OutputStream out;

    public ClientPDUListener(InputStream in, OutputStream out, String nickname, ChatModule module){
        this.nickname = nickname;
        this.module = module;
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        try {
            while(true){
                PDU pdu = PDU.fromInputStream(in);

                if(pdu instanceof UJoinPDU){
                    String s = ((UJoinPDU)pdu).getNickname();
                    module.notifyJoinListeners(s);
                }
                else if (pdu instanceof UCNickPDU){
                    String old = ((UCNickPDU)pdu).getOldNick();
                    String newNick = ((UCNickPDU)pdu).getNewNick();
                    module.notifyLeaveListeners(old);
                    module.notifyJoinListeners(newNick);
                }
                else if (pdu instanceof MessagePDU){
                    String s = ((MessagePDU)pdu).getMessage();
                    String sender = ((MessagePDU)pdu).getNickname();
                    String time = ((MessagePDU)pdu).getDate();
                    s += "\n";
                    module.notifyMessageListeners("["+time+"] "+sender+" says: "+s);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getNickname(){
        return this.nickname;
    }
}
