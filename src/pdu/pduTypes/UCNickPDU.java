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

public class UCNickPDU extends PDU {
    Date timestamp;
    String oldNick;
    String newNick;

    public UCNickPDU(InputStream inStream){


        try {
            int oldNickLength = Byte.valueOf(readExactly(inStream, 1)[0]);
            int newNickLength = Byte.valueOf(readExactly(inStream, 1)[0]);
            readExactly(inStream, 1);
            this.timestamp = new Date(byteArrayToLong(readExactly(inStream, 4))*1000);
            this.oldNick = new String(readExactly(inStream, oldNickLength), "UTF-8");
            readExactly(inStream, padLengths(oldNickLength)-oldNickLength);
            this.newNick = new String(readExactly(inStream, newNickLength), "UTF-8");
            readExactly(inStream, padLengths(newNickLength)-newNickLength);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UCNickPDU(Date timestamp, String oldNick, String newNick) {
        this.timestamp = new Date((timestamp.getTime()/1000)*1000);
        this.oldNick = oldNick;
        this.newNick = newNick;
    }

    @Override
    public byte[] toByteArray() {
        ByteSequenceBuilder outputByteStream = new ByteSequenceBuilder();
        outputByteStream.append(OpCode.UCNICK.value);
        outputByteStream.append((byte)oldNick.getBytes(UTF_8).length);
        outputByteStream.append((byte)newNick.getBytes(UTF_8).length);
        outputByteStream.pad();
        outputByteStream.appendInt((int)(timestamp.getTime()/1000));
        outputByteStream.append(oldNick.getBytes(UTF_8));
        outputByteStream.pad();
        outputByteStream.append(newNick.getBytes(UTF_8));
        outputByteStream.pad();

        return outputByteStream.toByteArray();
    }
}
