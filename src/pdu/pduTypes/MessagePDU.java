package pdu.pduTypes;

import pdu.Checksum;
import pdu.OpCode;
import pdu.PDU;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        byte[] input = this.message.getBytes();
        byte[] pad = new byte[1];

        //Write Op-code (1 byte)
        outputStream.write(OpCode.MESSAGE.value);

        //Add padding (1 byte)
        try {
            outputStream.write(pad);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Try to add nick length. Else pad (1 byte)
        try {
            outputStream.write(this.nickname.getBytes().length);
        } catch (NullPointerException e){
            try {
                outputStream.write(pad);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        //Add placeholder for checksum (calculated at the end) (1 byte)
        try {
            outputStream.write(new byte[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }


        //Write message length. Adds padding if necessary (2 bytes)
        try {
            outputStream.write(message.getBytes().length);
            if(message.getBytes().length < 256){
                outputStream.write(pad);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //Add padding of 2 bytes
        try {
            outputStream.write(new byte[2]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Try to write timestamp. Else write 0 (4 bytes)
        try{
            try {
                byte[] time = timestamp.toString().getBytes();
                outputStream.write(time);
                outputStream.write(
                        new byte[PDU.padLengths(time.length)-time.length]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (NullPointerException e) {
            try {
                outputStream.write(new byte[4]);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        //Write message
        try {
            outputStream.write(input);
            outputStream.write(
                    new byte[PDU.padLengths(input.length)-input.length]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //write nickname
        try{
            try {
                outputStream.write(this.nickname.getBytes());
                outputStream.write(new byte[PDU.padLengths(
                        this.nickname.getBytes().length)-
                        this.nickname.getBytes().length]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch (NullPointerException e){
            try {
                outputStream.write(new byte[4]);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }



        byte[] byteArray = outputStream.toByteArray();
        byteArray[3] = Checksum.computeChecksum(byteArray);
        return byteArray;
    }
}
