package pdu.pduTypes;

import pdu.PDU;

public class RegPDU extends PDU {

    public RegPDU(String serverName, short port) {
    }

    @Override
    public byte[] toByteArray() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
