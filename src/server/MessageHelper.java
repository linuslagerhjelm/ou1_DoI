package server;

import pdu.PDU;

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
            if(server.getQueuedEvents().size() > 0)
                treatQueue();
        }
    }

    private void treatQueue() {
        try {
            for (PDU pdu : server.getQueuedEvents()) {
                for (ClientThread client : server.getConnectedClients().values()) {
                    client.sendPDU(pdu);
                }
                server.dequeueEvent(pdu);
            }
        } catch(Exception e) { e.printStackTrace(); }
    }
}
