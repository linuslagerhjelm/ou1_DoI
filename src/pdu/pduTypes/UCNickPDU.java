package pdu.pduTypes;


import pdu.PDU;

import java.util.Date;

public class UCNickPDU extends PDU {

    public UCNickPDU(Date timestamp, String oldNick, String newNick) {
    }

    @Override
    public byte[] toByteArray() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
