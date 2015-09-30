package pdu.pduTypes;


import pdu.ByteSequenceBuilder;
import pdu.OpCode;
import pdu.PDU;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ChNickPDU extends PDU {

    public String nickname;

    public ChNickPDU(InputStream inStream){
        try {
            int nickLength = Byte.valueOf(readExactly(inStream,1)[0]);
            readExactly(inStream, 2);
            this.nickname = new String(readExactly(inStream, nickLength), "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }
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
