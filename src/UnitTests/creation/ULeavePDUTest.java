package UnitTests.creation;

import UnitTests.AllPDUTests;
import org.junit.Test;
import pdu.OpCode;
import pdu.PDU;
import pdu.pduTypes.ULeavePDU;

import java.util.Date;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;

/**
 * Created by linuslagerhjelm on 15-09-30.
 */
public class ULeavePDUTest {

    private final String NICKNAME = "Linus";
    private Date TIMESTAMP = new Date();
    private PDU pdu = new ULeavePDU(NICKNAME, TIMESTAMP);

    @Test
    public void shouldHaveCorrectOpCOde() throws Exception {
        assertEquals(OpCode.UJOIN.value, pdu.toByteArray()[0]);
    }

    @Test
    public void shouldHaveCorrectNicknameLength() throws Exception {
        assertEquals(NICKNAME.getBytes(UTF_8).length, pdu.toByteArray()[1]);
    }

    @Test
    public void shouldHavePaddedHeader() throws Exception {
        assertEquals(0, pdu.toByteArray()[2]);
        assertEquals(0, pdu.toByteArray()[3]);
    }

    @Test
    public void shouldHaveCorrectTimeStamp() throws Exception {
        byte[] timeBytes = new byte[4];
        System.arraycopy(pdu.toByteArray(), 4, timeBytes, 0, 4);
        assertEquals((int) (TIMESTAMP.getTime() / 1000), PDU.byteArrayToLong(
                timeBytes));
    }

    @Test
    public void shouldHaveCorrectNickname() throws Exception {
        byte[] pduBytes = pdu.toByteArray();
        byte[] nickBytes = new byte[pduBytes[1]];
        System.arraycopy(pduBytes, 8, nickBytes, 0, nickBytes.length);
        assertEquals(NICKNAME, new String(nickBytes, UTF_8));
    }

    @Test
    public void shouldHaveCorrectLength() throws Exception {
        assertEquals(
                8 + PDU.padLengths(NICKNAME.getBytes(UTF_8).length),
                pdu.toByteArray().length);
    }

    @Test
    public void shouldHavePaddedEnd() throws Exception {
        byte[] pduBytes = pdu.toByteArray();
        for (int i = 8 + pduBytes[1]; i < pduBytes.length; i++) {
            assertEquals(0, pduBytes[i]);
        }
    }

    @Test
    public void shouldBeEqualWhenReadFromStream() throws Exception {
        AllPDUTests.assertThroughStreamEquals(pdu);
    }
}
