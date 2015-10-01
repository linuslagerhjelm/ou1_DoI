package pdu.pduTypes;


import pdu.ByteSequenceBuilder;
import pdu.OpCode;
import pdu.PDU;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Date;

public class UJoinPDU extends PDU {

    private String nickname;
    private Date timestamp;

    public UJoinPDU(InputStream inStream){
        try {
            int nickLength = Byte.valueOf(readExactly(inStream, 1)[0]);
            readExactly(inStream, 2);
            timestamp = new Date(byteArrayToLong(readExactly(inStream, 4))*1000);
            nickname = new String(readExactly(inStream, nickLength), "UTF-8");
            readExactly(inStream, padLengths(nickLength)-nickLength);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UJoinPDU(String nickname, Date timestamp) {
        this.nickname = nickname;
        this.timestamp = new Date((timestamp.getTime()/1000)*1000);
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
    public String getNickname(){ return this.nickname; }
    private byte[] getTimestampFromDate(Date date){
        if(null == date){
            throw new NullPointerException();
        }

        int dateInSec = (int) (date.getTime() / 1000);
        byte[] bytes = ByteBuffer.allocate(4).putInt(dateInSec).array();

        return bytes;
    }
}
