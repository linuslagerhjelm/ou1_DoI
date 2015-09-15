package pdu.pduTypes;

import pdu.ByteSequenceBuilder;
import pdu.OpCode;
import pdu.PDU;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;

public class NicksPDU extends PDU {

    private Set<String> nicknames = new HashSet<>();

    public NicksPDU(Collection<String> nicknames) {
        this.nicknames.addAll(nicknames);
    }

    public NicksPDU(String... nicknames) {
        for(String i: nicknames){
            this.nicknames.add(i);
        }
    }

    @Override
    public byte[] toByteArray() {
        ByteSequenceBuilder outputByteStream = new ByteSequenceBuilder();
        byte[] nickBytes = nicknames.toString().getBytes(UTF_8);

        //write OpCode
        outputByteStream.append(OpCode.NICKS.value);

        //Write number of nicknames
        outputByteStream.append((byte)nicknames.size());

        //write nickname array length
        outputByteStream.append((byte)nickBytes.length);
        if(nickBytes.length < 256)
            outputByteStream.append(new byte[1]);

        //write nicknames
        for(String nickname: nicknames){
            outputByteStream.append(nickname.getBytes());
            outputByteStream.append("\0".getBytes());
        }
        outputByteStream.pad();

        byte[] returnArray = outputByteStream.toByteArray();

        return returnArray;
    }

    public Set<String> getNicknames() {
        return this.nicknames;
    }
}
