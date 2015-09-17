package pdu.pduTypes;

import pdu.ByteSequenceBuilder;
import pdu.OpCode;
import pdu.PDU;

public class QuitPDU extends PDU {

    public QuitPDU(){}

    @Override
    public byte[] toByteArray() {
        ByteSequenceBuilder outputStream = new ByteSequenceBuilder();

        outputStream.append(OpCode.QUIT.value);
        outputStream.pad();

        return outputStream.toByteArray();
    }
}
