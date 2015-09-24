package pdu.pduTypes;


import pdu.ByteSequenceBuilder;
import pdu.OpCode;
import pdu.PDU;

import java.util.Arrays;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ChNickPDU extends PDU {

    public String nickname;

    public ChNickPDU(byte[] inStream){
        nickname = Arrays.copyOfRange(inStream, 4, inStream.length).toString();
    }

    public ChNickPDU(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public byte[] toByteArray() {
        ByteSequenceBuilder outputByteStream = new ByteSequenceBuilder();

        outputByteStream.append(OpCode.CHNICK.value);
        outputByteStream.append((byte)nickname.getBytes(UTF_8).length);
        outputByteStream.append(new byte[4]);
        outputByteStream.append(nickname.getBytes(UTF_8));
        outputByteStream.pad();

        return outputByteStream.toByteArray();
    }
}
