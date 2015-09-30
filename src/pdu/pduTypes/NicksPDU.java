package pdu.pduTypes;

import pdu.ByteSequenceBuilder;
import pdu.OpCode;
import pdu.PDU;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

public class NicksPDU extends PDU {

    private Set<String> nicknames = new HashSet<>();

    public NicksPDU(InputStream inStream){
        try {
            readExactly(inStream,1);
            int totalLength = byteArrayToShort(readExactly(inStream, 2));
            this.nicknames = getNicknamesFromByteArray(readExactly(inStream,totalLength));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

        for(String s: this.nicknames){
            s += '\0';
            nicknames.append(s.getBytes(UTF_8));
        }

        byte[] nickBytes = nicknames.toByteArray();

        outputByteStream.append(OpCode.NICKS.value);
        outputByteStream.append((byte)this.nicknames.size());

        outputByteStream.appendShort((short)nickBytes.length);
        outputByteStream.append(nickBytes);
        outputByteStream.pad();

        return outputByteStream.toByteArray();
    }

    public Set<String> getNicknames() {
        return this.nicknames;
    }

    private static Set<String> getNicknamesFromByteArray(byte[] stream){
        Set<String> returnSet = new HashSet<>();
        ByteSequenceBuilder temp = new ByteSequenceBuilder();

        for(int i = 0; i < stream.length; ++i){
            temp.append(stream[i]);

            if(stream[i] == '\0'){
                String myString = null;
                try {
                    myString = new String(temp.toByteArray(), "UTF-8");
                    returnSet.add(myString);
                    temp = new ByteSequenceBuilder();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                ++i;
            }
        }
        return returnSet;
    }
}
