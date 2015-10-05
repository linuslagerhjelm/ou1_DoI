package pdu.pduTypes;


import pdu.ByteSequenceBuilder;
import pdu.OpCode;
import pdu.PDU;

import java.io.IOException;
import java.io.InputStream;

public class GetListPDU extends PDU {

    public GetListPDU() {}
    public GetListPDU(InputStream inStream) {
        try {
            readExactly(inStream,3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] toByteArray() {
        ByteSequenceBuilder outputByteStream = new ByteSequenceBuilder();
        outputByteStream.append(OpCode.GETLIST.value);
        outputByteStream.pad();
        return outputByteStream.toByteArray();
    }
}
