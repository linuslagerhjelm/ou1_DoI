package pdu.pduTypes;


import pdu.PDU;

import java.util.Date;

public class ULeavePDU extends PDU {

    public ULeavePDU(String nickname, Date timestamp) {
    }

    @Override
    public byte[] toByteArray() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
