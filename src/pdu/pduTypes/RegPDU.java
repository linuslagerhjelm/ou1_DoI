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
    private String serverName;

    public RegPDU(InputStream inStream){

        try {
            int snLength = (short)byteArrayToLong(readExactly(inStream, 1));
            this.TCPPort = (short)byteArrayToLong(readExactly(inStream,2));
            serverName = new String(readExactly(inStream, snLength), "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RegPDU(String serverName, short port) {
        this.serverName = serverName;
        this.TCPPort = port;
    }

    @Override
    public byte[] toByteArray() {
        ByteSequenceBuilder outputByteStream = new ByteSequenceBuilder();
        outputByteStream.append(OpCode.REG.value);
        outputByteStream.append((byte) serverName.getBytes(UTF_8).length);

        outputByteStream.appendShort(TCPPort);
        outputByteStream.append(serverName.getBytes(UTF_8));
        outputByteStream.pad();

        return outputByteStream.toByteArray();
    }
}
