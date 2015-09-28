package pdu.pduTypes;


import pdu.ByteSequenceBuilder;
import pdu.OpCode;
import pdu.PDU;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Date;

import static java.nio.charset.StandardCharsets.UTF_8;

public class UCNickPDU extends PDU {
    Date timestamp;
    byte[] oldNick;
    byte[] newNick;

    public UCNickPDU(byte[] inStream){
        int oldNickLength = inStream[1];
        int newNickLength = inStream[2];
        byte[] timestamp = Arrays.copyOfRange(inStream, 4, 8);
        this.timestamp = new Date(unsignedIntToLong(timestamp));
        oldNick = Arrays.copyOfRange(inStream, 8, 8+oldNickLength);
        newNick = Arrays.copyOfRange(
                inStream, 8+oldNickLength, (8+oldNickLength+newNickLength));

    }

    public UCNickPDU(Date timestamp, String oldNick, String newNick) {
        this.timestamp = timestamp;
        this.oldNick = oldNick.getBytes(UTF_8);
        this.newNick = newNick.getBytes(UTF_8);
    }

    @Override
    public byte[] toByteArray() {
        ByteSequenceBuilder outputByteStream = new ByteSequenceBuilder();
        outputByteStream.append(OpCode.UCNICK.value);
        outputByteStream.append((byte)oldNick.length);
        outputByteStream.append((byte)newNick.length);
        outputByteStream.pad();
        outputByteStream.append(getTimestampFromDate(timestamp));
        outputByteStream.append(oldNick);
        outputByteStream.pad();
        outputByteStream.append(newNick);
        outputByteStream.pad();

        return outputByteStream.toByteArray();
    }

    private byte[] getTimestampFromDate(Date date){
        if(null == date){
            throw new NullPointerException();
        }

        int dateInSec = (int) (date.getTime() / 1000);
        byte[] bytes = ByteBuffer.allocate(4).putInt(dateInSec).array();

        return bytes;
    }
}
