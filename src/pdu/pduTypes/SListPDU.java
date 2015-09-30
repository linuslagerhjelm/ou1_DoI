package pdu.pduTypes;

import pdu.ByteSequenceBuilder;
import pdu.OpCode;
import pdu.PDU;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

public class SListPDU extends PDU {
    byte seqNo;
    Set<ServerEntry> entries;

    public SListPDU(InputStream inStream){
        try {
            this.seqNo = (byte)byteArrayToLong(readExactly(inStream, 1));
            this.entries = priv_getServerEntries(inStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SListPDU(byte seqNo, ServerEntry... entries) {
        this.seqNo = seqNo;
        this.entries = new HashSet<>(Arrays.asList(entries));
    }

    @Override
    public byte[] toByteArray() {
        ByteSequenceBuilder outputByteStream = new ByteSequenceBuilder();
        outputByteStream.append(OpCode.SLIST.value);
        outputByteStream.append(seqNo);

        outputByteStream.appendShort((short)entries.size());
        for(ServerEntry server: entries) {
            outputByteStream.append(server.toByteArray());
        }

        return outputByteStream.toByteArray();
    }
    public Set<ServerEntry> getServerEntries(){ return entries; }


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
            byteArray.appendShort(port);
            byteArray.append(clientCount);
            byteArray.append((byte)serverName.getBytes(UTF_8).length);
            byteArray.append(serverName.getBytes(UTF_8));
            byteArray.pad();

            return byteArray.toByteArray();
        }
        public String[] toStringArray(){
            return new String[]{address.toString(), Short.toString(port), serverName, Byte.toString(clientCount)};
        }
    }

    private Set<ServerEntry> priv_getServerEntries(InputStream inputStream){
        Set<ServerEntry> servers = new HashSet<>();

        long nrOfServers = 0;
        try {
            nrOfServers = byteArrayToLong(readExactly(inputStream, 2));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < nrOfServers; ++i){
            InetAddress address = null;
            short port;
            long clientCount;
            String serverName;

            try {
                address = InetAddress.getByAddress(readExactly(inputStream, 4));
                port = (short)byteArrayToLong(readExactly(inputStream, 2));
                clientCount = byteArrayToLong(readExactly(inputStream, 1));

                Byte b = (readExactly(inputStream, 1)[0]);
                int serverNameLength = b.intValue();

                serverName = new String(readExactly(inputStream, serverNameLength), "UTF-8");
                servers.add(new ServerEntry(address, port, (byte)clientCount, serverName));
                readExactly(inputStream,padLengths(serverNameLength)-serverNameLength);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return servers;
    }
}
