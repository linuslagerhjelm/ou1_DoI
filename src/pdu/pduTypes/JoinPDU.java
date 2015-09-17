package pdu.pduTypes;

import pdu.ByteSequenceBuilder;
import pdu.OpCode;
import pdu.PDU;

import java.util.Arrays;

import static java.nio.charset.StandardCharsets.UTF_8;

public class JoinPDU extends PDU {

    String nickname;

    public JoinPDU(byte[] inStream){
        nickname = Arrays.copyOfRange(inStream, 4, inStream.length).toString();
    }

    public JoinPDU(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public byte[] toByteArray() {

        byte[] nicknameBytes = nickname.getBytes(UTF_8);
        ByteSequenceBuilder outputByteStream = new ByteSequenceBuilder();

        outputByteStream.append(OpCode.JOIN.value);
        outputByteStream.append((byte)nickname.getBytes().length);
        outputByteStream.append(new byte[2]);
        outputByteStream.append(nicknameBytes);
        outputByteStream.pad();

        return outputByteStream.toByteArray();
    }
}
