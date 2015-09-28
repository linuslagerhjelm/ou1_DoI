package pdu.pduTypes;

import pdu.ByteSequenceBuilder;
import pdu.OpCode;
import pdu.PDU;

import java.util.Arrays;

public class AlivePDU extends PDU {
    private short id;
    private byte clientCount;

    public AlivePDU(byte[] inStream){
        this.clientCount = inStream[1];
        if(inStream[2] == 0)
            this.id = (short)inStream[3];
        else
            this.id =(byte)PDU.byteArrayToInt(Arrays.copyOfRange(inStream,2,4));
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
