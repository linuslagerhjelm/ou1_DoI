package pdu.pduTypes;

import pdu.ByteSequenceBuilder;
import pdu.Checksum;
import pdu.OpCode;
import pdu.PDU;


import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Date;

/**
 * Subclass of PDU representing a PDU with a time stamp, a message and a
 * sender nickname.
 */
public class MessagePDU extends PDU {
    String message;
    String nickname;
    Date timestamp;

    public MessagePDU(InputStream inStream){
        try {
            readExactly(inStream, 1);
            int nickLength = Byte.valueOf(readExactly(inStream, 1)[0]);
            readExactly(inStream, 1);
            int messageLength = byteArrayToShort(readExactly(inStream, 2));
            readExactly(inStream, 2);
            this.timestamp = new Date(byteArrayToLong(readExactly(inStream, 4))*1000);
            this.message = new String(readExactly(inStream, messageLength), "UTF-8");
            readExactly(inStream, (padLengths(messageLength)-messageLength));
            this.nickname = new String(readExactly(inStream, nickLength), "UTF-8");
            readExactly(inStream, padLengths(nickLength)-nickLength);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MessagePDU(String message) {
        this.message = message;
    }

    public MessagePDU(
            String message,
            String nickname,
            Date timestamp) {
        this.message = message;
        this.nickname = nickname;
        this.timestamp = new Date((timestamp.getTime()/1000)*1000);
    }

    public String getMessage(){
        return message;
    }

    @Override
    public byte[] toByteArray() {
        ByteSequenceBuilder outputByteStream = new ByteSequenceBuilder();
        byte[] messageBytes = this.message.getBytes();

        outputByteStream.append(OpCode.MESSAGE.value);
        outputByteStream.append(new byte[1]);

        try{
            outputByteStream.append((byte) this.nickname.getBytes().length);
        } catch (NullPointerException e){
            outputByteStream.append(new byte[1]);
        }

        outputByteStream.append(new byte[1]);
        outputByteStream.appendShort((byte)message.getBytes().length);
        outputByteStream.append(new byte[2]);

        try{
            outputByteStream.append(getTimestampFromDate(timestamp));
        } catch (NullPointerException e){
            outputByteStream.append(new byte[4]);
        }

        outputByteStream.append(messageBytes);

        try{
            outputByteStream.append(this.nickname.getBytes());
            outputByteStream.pad();
        } catch (NullPointerException e){

        }

        byte[] byteArray = outputByteStream.toByteArray();
        byteArray[3] = Checksum.computeChecksum(byteArray);
        return byteArray;

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
