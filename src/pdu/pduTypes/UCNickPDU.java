package pdu.pduTypes;

import se.umu.cs.datakom.ht15.chatServer.given.pdu.PDU;

import java.util.Date;

public class UCNickPDU extends PDU {

    public UCNickPDU(Date timestamp, String oldNick, String newNick) {
    }

    @Override
    public byte[] toByteArray() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
