package pdu.pduTypes;

import pdu.ByteSequenceBuilder;
import pdu.OpCode;
import pdu.PDU;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static java.nio.charset.StandardCharsets.UTF_8;

public class RegPDU extends PDU {

    private short TCPPort;
    private byte[] serverName;

    public RegPDU(InputStream inStream){

        try {
            int serverNameLength = byteArrayToShort(readExactly(inStream, 1));
            this.TCPPort = bytesToShort(readExactly(inStream,2));
            this.serverName = readExactly(inStream, serverNameLength);

        } catch (IOException e) {
            e.printStackTrace();
        }
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
