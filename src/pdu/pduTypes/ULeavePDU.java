package pdu.pduTypes;


import pdu.ByteSequenceBuilder;
import pdu.OpCode;
import pdu.PDU;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Date;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ULeavePDU extends PDU {
    private String nickname;
    private Date timestamp;

    public ULeavePDU(byte[] inStream){
        int nickLength = (inStream[1] & 0xFF) << 8;
        long unixTime = unsignedIntToLong(Arrays.copyOfRange(inStream, 8, 12));
        this.timestamp = new Date(unixTime);

        nickname = Arrays.copyOfRange(
                inStream,inStream.length-nickLength,inStream.length).toString();
    }

    public ULeavePDU(String nickname, Date timestamp) {
        this.nickname = nickname;
        this.timestamp = timestamp;
    }

    @Override
    public byte[] toByteArray() {
        ByteSequenceBuilder outputByteStream = new ByteSequenceBuilder();

        outputByteStream.append(OpCode.ULEAVE.value);
        outputByteStream.append((byte)nickname.getBytes().length);
        outputByteStream.append(new byte[2]);
        outputByteStream.append(getTimestampFromDate(timestamp));
        outputByteStream.append(nickname.getBytes(UTF_8));
        outputByteStream.pad();

        return outputByteStream.toByteArray();
    }

    private byte[] getTimestampFromDate(Date date){
        if(null == date){
            throw new NullPointerException();
        }

        int dateInSec = (int) (date.getTime() / 1000);

        return ByteBuffer.allocate(4).putInt(dateInSec).array();
    }
}
