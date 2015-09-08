package pdu.pduTypes;


import pdu.PDU;

import java.util.Date;

public class UJoinPDU extends PDU {

    public UJoinPDU(String nickname, Date timestamp) {
    }

    @Override
    public byte[] toByteArray() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
