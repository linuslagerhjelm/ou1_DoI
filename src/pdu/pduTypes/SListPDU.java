package pdu.pduTypes;

import com.sun.corba.se.spi.activation.Server;
import pdu.ByteSequenceBuilder;
import pdu.OpCode;
import pdu.PDU;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SListPDU extends PDU {
    byte seqNo;
    Set<ServerEntry> entries;

    public SListPDU(byte seqNo, ServerEntry... entries) {
        this.seqNo = seqNo;
        this.entries = new HashSet<>(Arrays.asList(entries));
    }

    @Override
    public byte[] toByteArray() {
        ByteSequenceBuilder outputByteStream = new ByteSequenceBuilder();

        outputByteStream.append(OpCode.SLIST.value);
        outputByteStream.append(seqNo);


        return outputByteStream.toByteArray();
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
