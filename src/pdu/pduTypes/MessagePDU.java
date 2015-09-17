package pdu.pduTypes;

import pdu.ByteSequenceBuilder;
import pdu.Checksum;
import pdu.OpCode;
import pdu.PDU;


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

    public MessagePDU(byte[] inStream){
        int messageLength = Integer.parseInt(""+inStream[4]+inStream[5]);
        byte[] messByte = Arrays.copyOfRange(inStream, 12, messageLength);
        this.message = messByte.toString();

        //determines if call comes from server or client
        if(inStream[2] != 0){
            byte[] timestamp = Arrays.copyOfRange(inStream, 4, 8);
            this.timestamp = new Date(unsignedIntToLong(timestamp));
            this.nickname = Arrays.copyOfRange(inStream,
                    inStream.length-messageLength, inStream.length).toString();
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
