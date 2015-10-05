package server;

import pdu.PDU;

import java.util.Collection;

/**
 * Created by id14llm on 2015-10-05.
 */
public class MessageHelper implements Runnable{
    ChatServer server;

    public MessageHelper(ChatServer server){
        this.server = server;
    }

    @Override
    public void run() {
        while (true) {
            treatQueue();
        }
    }

    private void treatQueue() {
            try {
                PDU pdu = server.getQueuedEvents().take();
                if(pdu != null){
                    Collection<ClientThread> iter =  server.getConnectedClients().values();
                    for (ClientThread client : iter) {
                        client.sendPDU(pdu);
                    }
                }
            } catch(Exception e) { e.printStackTrace(); }
        }
    }

