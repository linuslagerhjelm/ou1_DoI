package pdu.pduTypes;

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
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        byte[] nickBytes = nicknames.toString().getBytes(UTF_8);

        //write OpCode
        outputByteArray.write(OpCode.NICKS.value);

        //Write number of nicknames
        outputByteArray.write(nicknames.size());

        //write nicknames
        try {
            for (String nickname: nicknames) {
                outputByteArray.write(nickname.getBytes(UTF_8));
                outputByteArray.write(new byte[1]);
            }
            outputByteArray.write(new byte[PDU.padLengths(nickBytes.length)-
                    nickBytes.length]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] returnArray = outputByteArray.toByteArray();

        return returnArray;
    }

    public Set<String> getNicknames() {
        return this.nicknames;
    }
}
