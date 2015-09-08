package pdu.pduTypes;

import se.umu.cs.datakom.ht15.chatServer.given.pdu.PDU;

import java.util.Date;

/**
 * Subclass of PDU representing a PDU with a time stamp, a message and a
 * sender nickname.
 */
public class MessagePDU extends PDU {

    public MessagePDU(String message) {
    }

    public MessagePDU(
            String message,
            String nickname,
            Date timestamp) {
    }

    @Override
    public byte[] toByteArray() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
