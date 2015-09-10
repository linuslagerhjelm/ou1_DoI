package pdu.pduTypes;

import pdu.PDU;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
public class NicksPDU extends PDU {

    private Set<String> nicknames = new HashSet<String>();

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
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Set<String> getNicknames() {
        return this.nicknames;
    }
}
