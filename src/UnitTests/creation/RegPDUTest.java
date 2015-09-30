package UnitTests.creation;

import UnitTests.AllPDUTests;
import org.junit.Assert;
import org.junit.Test;
import pdu.OpCode;
import pdu.PDU;
import pdu.pduTypes.NotRegPDU;
import pdu.pduTypes.RegPDU;

import java.util.Arrays;

import static java.nio.charset.StandardCharsets.UTF_8;


/**
 * Created by linuslagerhjelm on 15-09-21.
 */
public class RegPDUTest {
    private final String SERVERNAME = "localhost";
    private final short PORT = 1337;
    private final PDU pdu = new RegPDU(SERVERNAME,PORT);

    @Test
    public void shouldHaveCorrectOpCode() throws Exception {
        Assert.assertEquals(OpCode.REG.value, pdu.toByteArray()[0]);
    }

    @Test
    public void shouldHaveCorrectNameLength() throws Exception {
        byte[] servername = SERVERNAME.getBytes(UTF_8);
        Assert.assertEquals(servername.length, pdu.toByteArray()[1]);
    }

    @Test
    public void shouldHaveCorrectPort() throws Exception {
        byte[] ba = new byte[2];
        ba[0] = pdu.toByteArray()[2];
        ba[1] = pdu.toByteArray()[3];
        Assert.assertEquals(PORT, PDU.byteArrayToShort(ba));
    }

    @Test
    public void shouldHaveCorrectServerName() throws Exception {
        byte[] ba = pdu.toByteArray();
        byte[] nick = Arrays.copyOfRange(ba, 4, 4 + SERVERNAME.length());

        Assert.assertArrayEquals(SERVERNAME.getBytes(UTF_8), nick);
    }

    @Test
    public void shouldHaveCorrectLength() throws Exception {
        int pad = PDU.padLengths(SERVERNAME.length())-SERVERNAME.length();
        pad += 4;
        Assert.assertEquals(SERVERNAME.length()+pad, pdu.toByteArray().length);
    }

    @Test
    public void shouldBeEqualWhenReadFromStream() throws Exception {
        AllPDUTests.assertThroughStreamEquals(pdu);
    }
}
