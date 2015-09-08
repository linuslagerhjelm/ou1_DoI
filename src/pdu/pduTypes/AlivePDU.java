package pdu.pduTypes;

import pdu.PDU;

public class AlivePDU extends PDU {

    public AlivePDU(byte clientCount, short id) {
    }

    @Override
    public byte[] toByteArray() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
