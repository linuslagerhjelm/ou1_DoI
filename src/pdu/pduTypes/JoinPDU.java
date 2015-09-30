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
            int nickLength = Byte.valueOf(readExactly(inStream,1)[0]);
            this.nickname = new String(readExactly(inStream, nickLength), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JoinPDU(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public byte[] toByteArray() {

        byte[] nicknameBytes = nickname.getBytes(UTF_8);
        ByteSequenceBuilder outputByteStream = new ByteSequenceBuilder();

        outputByteStream.append(OpCode.JOIN.value);
        outputByteStream.append((byte)nicknameBytes.length);
        outputByteStream.pad();
        outputByteStream.append(nicknameBytes);
        outputByteStream.pad();

        return outputByteStream.toByteArray();
    }
}
