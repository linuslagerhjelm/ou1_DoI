package pdu.pduTypes;

import pdu.ByteSequenceBuilder;
import pdu.OpCode;
import pdu.PDU;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class SListPDU extends PDU {
    byte seqNo;
    List<ServerEntry> entries = new LinkedList<>();

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

        for(ServerEntry s: entries){
            this.entries.add(s);
        }
    }

    @Override
    public byte[] toByteArray() {
        ByteSequenceBuilder outputByteStream = new ByteSequenceBuilder();
        outputByteStream.append(OpCode.SLIST.value);
        outputByteStream.append(seqNo);
        outputByteStream.appendShort((short) entries.size());

        for(ServerEntry server: entries) {
            outputByteStream.append(server.toByteArray());
        }

        return outputByteStream.toByteArray();
    }

    public List<ServerEntry> getServerEntries(){ return entries; }


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

    private List<ServerEntry> priv_getServerEntries(InputStream inputStream){
        List<ServerEntry> servers = new LinkedList<>();

        int nrOfServers = 0;
        try {
            nrOfServers = (int)byteArrayToLong(readExactly(inputStream, 2));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < nrOfServers; ++i){
            InetAddress address = null;
            short port;
            byte cCount;
            String serverName;

            try {
                address = InetAddress.getByAddress(readExactly(inputStream, 4));
                port = (short)byteArrayToLong(readExactly(inputStream, 2));
                cCount = (byte)byteArrayToLong(readExactly(inputStream, 1));

                Byte b = (readExactly(inputStream, 1)[0]);
                int serverNameLength = b.intValue();

                serverName = new String(readExactly(inputStream, serverNameLength), "UTF-8");
                servers.add(new ServerEntry(address, port, cCount, serverName));
                readExactly(inputStream,padLengths(serverNameLength)-serverNameLength);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return servers;
    }
}
