package pdu.pduTypes;


import pdu.ByteSequenceBuilder;
import pdu.OpCode;
import pdu.PDU;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class AckPDU extends PDU {

    private int id;

    public AckPDU(InputStream inStream){
        try {
            readExactly(inStream, 1);
            this.id = byteArrayToShort(readExactly(inStream, 2));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AckPDU(short id) {
        this.id = (int) id;
    }

    @Override
    public byte[] toByteArray() {
        ByteSequenceBuilder outputByteStream = new ByteSequenceBuilder();

        outputByteStream.append(OpCode.ACK.value);
        outputByteStream.append(new byte[1]);
        outputByteStream.appendShort((short) id);

        return outputByteStream.toByteArray();
    }
}
