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

public class SListPDU extends PDU {
    byte seqNo;
    Set<ServerEntry> entries;

    public SListPDU(byte[] inStream){
        this.seqNo = inStream[1];
        this.entries = priv_getServerEntries(inStream);
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

        if(entries.size() < 256)
            outputByteStream.append(new byte[1]);

        outputByteStream.append((byte)entries.size());
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
            byteArray.append((byte)port);
            byteArray.append(clientCount);
            byteArray.append((byte)serverName.getBytes().length);
            byteArray.append(serverName.getBytes());
            byteArray.pad();

            return byteArray.toByteArray();
        }
        public String[] toStringArray(){
            return new String[]{address.toString(), Short.toString(port), serverName, Byte.toString(clientCount)};
        }
    }

    private Set<ServerEntry> priv_getServerEntries(byte[] stream){
        Set<ServerEntry> servers = new HashSet<>();
        InputStream inputStream = new ByteArrayInputStream(stream);

        try {
            readExactly(inputStream, 4);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < stream[3]; ++i){
            InetAddress address = null;
            short port;
            byte clientCount;
            String serverName;

            try {
                address = InetAddress.getByAddress(readExactly(inputStream, 4));
                port = (short) byteArrayToShort(readExactly(inputStream, 2));
                clientCount = readExactly(inputStream, 1)[0];

                Byte b = new Byte(readExactly(inputStream, 1)[0]);
                int serverNameLength = b.intValue();

                serverName = new String(readExactly(inputStream, serverNameLength), "UTF-8");
                servers.add(new ServerEntry(address, port, clientCount, serverName));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        return servers;
    }
}
