package pdu.pduTypes;


import pdu.ByteSequenceBuilder;
import pdu.OpCode;
import pdu.PDU;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Date;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ULeavePDU extends PDU {
    private String nickname;
    private Date timestamp;

    public ULeavePDU(InputStream inStream){
        try {
            int nickLength = (int)byteArrayToLong(readExactly(inStream, 1));
            readExactly(inStream, 2);
            this.timestamp = new Date(byteArrayToLong(readExactly(inStream, 4))*1000);
            this.nickname = new String(readExactly(inStream, nickLength), "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ULeavePDU(String nickname, Date timestamp) {
        this.nickname = nickname;
        this.timestamp = new Date((timestamp.getTime()/1000)*1000);
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
