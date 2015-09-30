package UnitTests.creation;

import UnitTests.AllPDUTests;
import org.junit.Assert;
import org.junit.Test;
import pdu.OpCode;
import pdu.PDU;
import pdu.pduTypes.ChNickPDU;

import java.util.Arrays;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by linuslagerhjelm on 15-09-21.
 */
public class ChNickPDUTest {
    private static final String NICKNAME = "Linus";
    private static final PDU pdu = new ChNickPDU(NICKNAME);

    @Test
    public void shouldHaveCorrectOpCode() throws Exception {
        Assert.assertEquals(OpCode.CHNICK.value, pdu.toByteArray()[0]);
    }

    @Test
    public void shouldHaveCorrectNickLength() throws Exception {
        int nickLength = NICKNAME.getBytes(UTF_8).length;
        Assert.assertEquals(nickLength, pdu.toByteArray()[1]);
    }

    @Test
    public void shouldBePaddedCorrectly() throws Exception {
        Assert.assertEquals(0, pdu.toByteArray()[2]);
        Assert.assertEquals(0, pdu.toByteArray()[3]);
    }

    @Test
    public void shouldHaveCorrectNick() throws Exception {
        byte[] ba = pdu.toByteArray();
        byte[] nick = Arrays.copyOfRange(ba, 4, 4 + NICKNAME.length());

        Assert.assertArrayEquals(NICKNAME.getBytes(UTF_8), nick);
    }

    @Test
    public void shouldHaveCorrectLength() throws Exception {
        int nickLength = PDU.padLengths(NICKNAME.length());
        Assert.assertEquals(pdu.toByteArray().length, 4+nickLength);
    }

    @Test
    public void shouldBeEqualWhenReadFromStream() throws Exception {
        AllPDUTests.assertThroughStreamEquals(pdu);
    }
}
