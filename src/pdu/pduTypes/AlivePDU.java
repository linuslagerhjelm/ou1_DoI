package pdu.pduTypes;

import pdu.ByteSequenceBuilder;
import pdu.OpCode;
import pdu.PDU;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class AlivePDU extends PDU {
    private short id;
    private byte clientCount;

    public AlivePDU(InputStream inStream){
        try {
            this.clientCount = readExactly(inStream, 1)[0];
            this.id = bytesToShort(readExactly(inStream, 2));

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

        if(this.id < 256)
            outputByteStream.append(new byte[1]);

        outputByteStream.append((byte) this.id);

        return outputByteStream.toByteArray();
    }
}
