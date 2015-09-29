package pdu.pduTypes;

import pdu.ByteSequenceBuilder;
import pdu.OpCode;
import pdu.PDU;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.*;

public class SListPDU extends PDU {
    byte seqNo;
    Set<ServerEntry> entries;

    public SListPDU(byte[] inStream){
        byte seqNo = inStream[1];
        SListPDU.ServerEntry[] servers = getServerEntries(inStream);
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

    private ServerEntry[] getServerEntries(byte[] stream){
        List<ServerEntry> servers = new ArrayList<>();
        int serversRead = 0;

        for(int i = 4; i < stream.length; ++i){
            InetAddress address = null;
            short port;
            byte clientCount;
            String serverName;

            try {
                address = InetAddress.getByAddress( Arrays.copyOfRange(stream, i, i+4));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            i += 4;

            port = (short) PDU.byteArrayToLong(stream, i, i+2);
            i += 2;
            clientCount = stream[i];
            ++i;
            int serverNamLength = stream[i];
            ++i;
            serverName = new String(Arrays.copyOfRange(
                    stream, i, i+serverNamLength), Charset.forName("utf-8"));

            i += serverNamLength;
            servers.add(new ServerEntry(
                    address, port, clientCount, serverName));
            ++serversRead;
            if(serversRead == stream[4])
                break;
        }

        ServerEntry[] returnArray = new ServerEntry[servers.size()];

        for(int i = 0; i < servers.size(); ++i){
            returnArray[i] = servers.get(i);
        }

        return returnArray;
    }
}
