package pdu.pduTypes;


import pdu.ByteSequenceBuilder;
import pdu.OpCode;
import pdu.PDU;

import java.util.Arrays;

public class AckPDU extends PDU {

    private int id;

    public AckPDU(byte[] inStream){
        ByteSequenceBuilder temp = new ByteSequenceBuilder();
        temp.append(Arrays.copyOfRange(inStream, 3, inStream.length));
        temp.pad();
        this.id = byteArrayToInt(temp.toByteArray());
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
