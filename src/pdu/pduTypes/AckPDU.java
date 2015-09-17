package pdu.pduTypes;


import pdu.ByteSequenceBuilder;
import pdu.OpCode;
import pdu.PDU;

import java.util.Arrays;

public class AckPDU extends PDU {

    private int id;

    public AckPDU(byte[] inStream){
        byte[] temp = Arrays.copyOfRange(inStream, 3, inStream.length);
        this.id = byteArrayToInt(temp);
    }

    public AckPDU(short id) {
        this.id = (int) id;
    }

    @Override
    public byte[] toByteArray() {
        ByteSequenceBuilder outputByteStream = new ByteSequenceBuilder();

        outputByteStream.append(OpCode.ACK.value);
        outputByteStream.pad();

        if(this.id < 256)
            outputByteStream.append(new byte[1]);

        outputByteStream.append((byte) this.id);

        return outputByteStream.toByteArray();
    }
}
