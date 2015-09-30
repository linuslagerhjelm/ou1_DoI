package pdu.pduTypes;

import pdu.ByteSequenceBuilder;
import pdu.OpCode;
import pdu.PDU;

import java.io.IOException;
import java.io.InputStream;

public class AlivePDU extends PDU {
    private short id;
    private byte clientCount;

    public AlivePDU(InputStream inStream){
        try {
            this.clientCount = (byte) byteArrayToLong(readExactly(inStream, 1));
            this.id = (short) byteArrayToLong(readExactly(inStream, 2));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AlivePDU(byte clientCount, short id) {
        this.clientCount = clientCount;
        this.id = id;
    }

    @Override
    public byte[] toByteArray() {
        ByteSequenceBuilder outputByteStream = new ByteSequenceBuilder();

        outputByteStream.append(OpCode.ALIVE.value);
        outputByteStream.append(clientCount);
        outputByteStream.appendShort(this.id);

        return outputByteStream.toByteArray();
    }
}
