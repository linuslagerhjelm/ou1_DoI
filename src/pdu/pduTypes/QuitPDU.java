package pdu.pduTypes;

import pdu.ByteSequenceBuilder;
import pdu.OpCode;
import pdu.PDU;

import java.io.IOException;
import java.io.InputStream;

public class QuitPDU extends PDU {

    public QuitPDU(InputStream inputStream){
        try {
            readExactly(inputStream, 3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public QuitPDU(){}

    @Override
    public byte[] toByteArray() {
        ByteSequenceBuilder outputStream = new ByteSequenceBuilder();

        outputStream.append(OpCode.QUIT.value);
        outputStream.pad();

        return outputStream.toByteArray();
    }
}
