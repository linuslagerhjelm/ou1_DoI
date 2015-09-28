package UnitTests.creation;

import UnitTests.AllPDUTests;
import org.junit.Assert;
import org.junit.Test;
import pdu.OpCode;
import pdu.PDU;
import pdu.pduTypes.UCNickPDU;

import java.util.Arrays;
import java.util.Date;

import static java.nio.charset.StandardCharsets.UTF_8;


/**
 * Created by linuslagerhjelm on 15-09-21.
 */
public class UCHNickPDUTest {
    private final String OLDNICK = "Linus";
    private final String NEWNICK = "Lagerhjelm";
    private final Date TIME = new Date();
    private final PDU pdu = new UCNickPDU(TIME, OLDNICK, NEWNICK);

    @Test
    public void shouldHaveCorrectOpCode() throws Exception {
        Assert.assertEquals(OpCode.UCNICK.value, pdu.toByteArray()[0]);
    }

    @Test
    public void shouldHaveCorrectNameLength1() throws Exception {
        byte[] oldNick = OLDNICK.getBytes(UTF_8);
        Assert.assertEquals(oldNick.length, pdu.toByteArray()[1]);
    }

    @Test
    public void shouldHaveCorrectNameLength2() throws Exception {
        byte[] newNick = NEWNICK.getBytes(UTF_8);
        Assert.assertEquals(newNick.length, pdu.toByteArray()[2]);
    }

    @Test
    public void shouldHavePaddedHead() throws Exception {
        Assert.assertEquals(0, pdu.toByteArray()[3]);
    }

    @Test
    public void shouldHaveCorrectDate() throws Exception {
        long timestamp = this.TIME.getTime()/1000;
        byte[] pduTimeStampBytes = Arrays.copyOfRange(pdu.toByteArray(), 4, 8);
        long pduTimeStampLong = PDU.byteArrayToLong(pduTimeStampBytes);
        Assert.assertEquals(timestamp, pduTimeStampLong);
    }

    @Test
    public void shouldHaveCorrectOldNick() throws Exception {
        byte[] ba = pdu.toByteArray();
        byte[] nick = Arrays.copyOfRange(ba, 7, 9 + OLDNICK.length());

        Assert.assertEquals(OLDNICK.getBytes(), nick);
    }

    @Test
    public void shouldHaveCorrectNewNick() throws Exception {
        int pad1 = PDU.padLengths(OLDNICK.length())-OLDNICK.length();
        int pad2 = PDU.padLengths(NEWNICK.length())-NEWNICK.length();
        int jump = 9+OLDNICK.length()+pad1;
        byte[] ba = pdu.toByteArray();
        byte[] nick = Arrays.copyOfRange(ba, jump, ba.length-pad2);

        Assert.assertEquals(NEWNICK.getBytes(), nick);
    }

    @Test
    public void shouldHaveCorrectLength() throws Exception {
        int pad1 = PDU.padLengths(OLDNICK.length());
        int pad2 = PDU.padLengths(NEWNICK.length());
        int length = 8+pad1+pad2;
        Assert.assertEquals(length, pdu.toByteArray().length);
    }

    @Test
    public void shouldBeEqualWhenReadFromStream() throws Exception {
        AllPDUTests.assertThroughStreamEquals(pdu);
    }
}
