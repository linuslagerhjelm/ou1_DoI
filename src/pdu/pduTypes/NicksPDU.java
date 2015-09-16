package pdu.pduTypes;

import pdu.ByteSequenceBuilder;
import pdu.OpCode;
import pdu.PDU;

import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

public class NicksPDU extends PDU {

    private Set<String> nicknames = new HashSet<>();

    public NicksPDU(Collection<String> nicknames) {
        this.nicknames.addAll(nicknames);
    }

    public NicksPDU(String... nicknames) {
        this.nicknames = new HashSet<>(Arrays.asList(nicknames));
    }

    @Override
    public byte[] toByteArray() {
        ByteSequenceBuilder outputByteStream = new ByteSequenceBuilder();
        ByteSequenceBuilder nicknames = new ByteSequenceBuilder();

        //get padded nicknames as bytes
        for(String s: this.nicknames){
            nicknames.append(s.getBytes(UTF_8));
            nicknames.append(new byte[1]);
        }

        byte[] nickBytes = nicknames.toByteArray();

        outputByteStream.append(OpCode.NICKS.value);
        outputByteStream.append((byte)this.nicknames.size());

        //add padding if necessary
        if(nickBytes.length < 256)
            outputByteStream.append(new byte[1]);

        outputByteStream.append((byte)nickBytes.length);
        outputByteStream.append(nickBytes);
        outputByteStream.pad();

        return outputByteStream.toByteArray();
    }

    public Set<String> getNicknames() {
        return this.nicknames;
    }
}
