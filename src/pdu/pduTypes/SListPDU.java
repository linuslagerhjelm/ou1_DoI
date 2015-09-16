package pdu.pduTypes;

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

        if(entries.size() < 256)
            outputByteStream.append(new byte[1]);

        outputByteStream.append((byte)entries.size());
        for(ServerEntry server: entries) {
            outputByteStream.append(server.toByteArray());
        }

        return outputByteStream.toByteArray();
    }

    public static class ServerEntry {

        public final String serverName;
        private InetAddress address;
        private short port;
        private byte clientCount;

        public ServerEntry(
                InetAddress address,
                short port,
                byte clientCount,
                String serverName) {
            this.serverName = serverName;
            this.address = address;
            this.port = port;
            this.clientCount = clientCount;
        }

        public byte[] toByteArray(){
            ByteSequenceBuilder byteArray = new ByteSequenceBuilder();

            byteArray.append(address.getAddress());
            byteArray.append((byte)port);
            byteArray.append(clientCount);
            byteArray.append((byte)serverName.getBytes().length);
            byteArray.append(serverName.getBytes());
            byteArray.pad();

            return byteArray.toByteArray();
        }
    }
}
