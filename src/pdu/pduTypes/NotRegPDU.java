package pdu.pduTypes;

import pdu.ByteSequenceBuilder;
import pdu.OpCode;
import pdu.PDU;

import java.io.IOException;
import java.io.InputStream;

public class NotRegPDU extends PDU {

    public NotRegPDU(InputStream inputStream){
        try {
            readExactly(inputStream, 3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NotRegPDU(){}

    @Override
    public byte[] toByteArray() {
        ByteSequenceBuilder outputByteStream = new ByteSequenceBuilder();
        outputByteStream.append(OpCode.NOTREG.value);
        outputByteStream.pad();
        return outputByteStream.toByteArray();
    }
}
