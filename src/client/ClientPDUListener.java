package client;

import gui.ChatModule;
import pdu.PDU;
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
        System.out.println(nickname+" = "+Thread.currentThread().getId());
        try {
            //OutputStream out = socket.getOutputStream();
            while(true){
                PDU pdu = PDU.fromInputStream(in);

                switch (pdu.toByteArray()[0]){
                    case 16:
                        System.out.println("Registered new client on "+Thread.currentThread().getId());
                        String s = ((UJoinPDU)pdu).getNickname();
                        module.notifyJoinListeners(s);
                        break;
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
