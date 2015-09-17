package pdu.pduTypes;


import pdu.ByteSequenceBuilder;
import pdu.OpCode;
import pdu.PDU;

public class GetListPDU extends PDU {

    public GetListPDU() {}

    @Override
    public byte[] toByteArray() {
        ByteSequenceBuilder outputByteStream = new ByteSequenceBuilder();
        outputByteStream.append(OpCode.GETLIST.value);
        outputByteStream.pad();
        return outputByteStream.toByteArray();
    }
}
