package pdu.pduTypes;

import pdu.OpCode;
import pdu.PDU;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class JoinPDU extends PDU {

    String nickname;

    public JoinPDU(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public byte[] toByteArray() {
        byte[] nicknameBytes = nickname.getBytes(UTF_8);
        byte[] padding = new byte[2];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );

        outputStream.write( OpCode.JOIN.value );
        outputStream.write( nickname.getBytes(UTF_8).length );
        try {
            outputStream.write( padding );
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream.write( nicknameBytes );
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream.write(new byte[PDU.padLengths(
                    nicknameBytes.length)-nicknameBytes.length]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte byteArray[] = outputStream.toByteArray( );

        return byteArray;
    }
}
