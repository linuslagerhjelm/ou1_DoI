package pdu.pduTypes;

import pdu.ByteSequenceBuilder;
import pdu.OpCode;
import pdu.PDU;

public class NotRegPDU extends PDU {

    @Override
    public byte[] toByteArray() {
        ByteSequenceBuilder outputByteStream = new ByteSequenceBuilder();
        outputByteStream.append(OpCode.NOTREG.value);
        outputByteStream.pad();
        return outputByteStream.toByteArray();
    }
}
