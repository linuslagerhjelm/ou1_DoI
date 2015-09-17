package pdu.pduTypes;

import pdu.ByteSequenceBuilder;
import pdu.Checksum;
import pdu.OpCode;
import pdu.PDU;
import java.lang.Long;


import java.nio.ByteBuffer;
import java.util.Date;

/**
 * Subclass of PDU representing a PDU with a time stamp, a message and a
 * sender nickname.
 */
public class MessagePDU extends PDU {
    String message;
    String nickname;
    Date timestamp;

    public MessagePDU(String message) {
        this.message = message;
    }

    public MessagePDU(
            String message,
            String nickname,
            Date timestamp) {
        this.message = message;
        this.nickname = nickname;
        this.timestamp = timestamp;
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

        //Check sum
        outputByteStream.append(new byte[1]);

        if(message.getBytes().length < 256)
            outputByteStream.append(new byte[1]);

        outputByteStream.append((byte)message.getBytes().length);

        outputByteStream.append(new byte[2]);

        try{
            outputByteStream.append(getTimestampFromDate(timestamp));
            outputByteStream.pad();
        } catch (NullPointerException e){
            outputByteStream.append(new byte[4]);
        }

        outputByteStream.append(messageBytes);
        outputByteStream.pad();

        try{
            outputByteStream.append(this.nickname.getBytes());
            outputByteStream.pad();
        } catch (NullPointerException e){
            outputByteStream.append(new byte[4]);
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
