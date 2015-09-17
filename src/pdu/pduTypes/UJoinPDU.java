package pdu.pduTypes;


import pdu.ByteSequenceBuilder;
import pdu.OpCode;
import pdu.PDU;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Date;

public class UJoinPDU extends PDU {

    private String nickname;
    private Date timestamp;

    public UJoinPDU(byte[] inStream){
        byte[] timestamp = Arrays.copyOfRange(inStream, 4, 8);
        this.timestamp = new Date(unsignedIntToLong(timestamp));

        byte[] nickBytes = Arrays.copyOfRange(inStream, 8, inStream.length);
        this.nickname = nickBytes.toString();
    }

    public UJoinPDU(String nickname, Date timestamp) {
        this.nickname = nickname;
        this.timestamp = timestamp;
    }

    @Override
    public byte[] toByteArray() {
        ByteSequenceBuilder outputByteStream = new ByteSequenceBuilder();

        outputByteStream.append(OpCode.UJOIN.value);
        outputByteStream.append((byte)nickname.getBytes().length);
        outputByteStream.append(new byte[2]);
        outputByteStream.append(getTimestampFromDate(timestamp));
        outputByteStream.append(nickname.getBytes());

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
