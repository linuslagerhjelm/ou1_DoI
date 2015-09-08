package pdu.pduTypes;

import se.umu.cs.datakom.ht15.chatServer.given.pdu.PDU;

import java.net.InetAddress;

public class SListPDU extends PDU {

    public SListPDU(byte seqNo, ServerEntry... entries) {
    }

    @Override
    public byte[] toByteArray() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static class ServerEntry {

        public final String serverName;

        public ServerEntry(
                InetAddress address,
                short port,
                byte clientCount,
                String serverName) {
            this.serverName = serverName;
        }
    }
}
