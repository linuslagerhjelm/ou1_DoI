package pdu.pduTypes;

import pdu.ByteSequenceBuilder;
import pdu.OpCode;
import pdu.PDU;

import java.util.Arrays;

import static java.nio.charset.StandardCharsets.UTF_8;

public class RegPDU extends PDU {

    private short TCPPort;
    private byte[] serverName;

    public RegPDU(byte[] inStream){
        int serverNameLength = inStream[1];
        this.TCPPort = Short.parseShort(""+inStream[2]+inStream[3]);
        this.serverName = Arrays.copyOfRange(inStream, 4, 4+serverNameLength);
    }

    public RegPDU(String serverName, short port) {
        this.serverName = serverName.getBytes(UTF_8);
        this.TCPPort = port;
    }

    @Override
    public byte[] toByteArray() {
        ByteSequenceBuilder outputByteStream = new ByteSequenceBuilder();
        outputByteStream.append(OpCode.REG.value);
        outputByteStream.append((byte) serverName.length);

        outputByteStream.appendShort(TCPPort);
        outputByteStream.append(serverName);
        outputByteStream.pad();

        return outputByteStream.toByteArray();
    }
}
