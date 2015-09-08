package pdu.pduTypes;

import se.umu.cs.datakom.ht15.chatServer.given.pdu.PDU;

import java.util.Collection;
import java.util.Set;

public class NicksPDU extends PDU {

    public NicksPDU(Collection<String> nicknames) {
    }

    public NicksPDU(String... nicknames) {
    }

    @Override
    public byte[] toByteArray() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Set<String> getNicknames() {
        return null;
    }
}
