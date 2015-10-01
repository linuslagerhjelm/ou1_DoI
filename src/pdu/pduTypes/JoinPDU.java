package pdu.pduTypes;

import pdu.ByteSequenceBuilder;
import pdu.OpCode;
import pdu.PDU;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static java.nio.charset.StandardCharsets.UTF_8;

public class JoinPDU extends PDU {

    String nickname;

    public JoinPDU(InputStream inStream){
        try {
            int nickLength = (int)byteArrayToLong(readExactly(inStream,1));
            readExactly(inStream, 2);
            nickname = new String(readExactly(inStream, nickLength), "UTF-8");
            readExactly(inStream, padLengths(nickLength)-nickLength);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JoinPDU(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public byte[] toByteArray() {
        ByteSequenceBuilder outputByteStream = new ByteSequenceBuilder();

        outputByteStream.append(OpCode.JOIN.value);
        outputByteStream.append((byte)nickname.getBytes(UTF_8).length);
        outputByteStream.pad();
        outputByteStream.append(nickname.getBytes(UTF_8));
        outputByteStream.pad();

        return outputByteStream.toByteArray();
    }

    public String getNickname(){
        return this.nickname;
    }
}
