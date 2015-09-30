package UnitTests.creation;

import UnitTests.AllPDUTests;
import org.junit.Assert;
import org.junit.Test;
import pdu.OpCode;
import pdu.PDU;
import pdu.pduTypes.AckPDU;

/**
 * Created by linuslagerhjelm on 15-09-21.
 */
public class AckPDUTest {
    private static final short ID = 20;
    private static PDU pdu = new AckPDU(ID);

    @Test
    public void shouldHaveCorrectOpCode() throws Exception {
        Assert.assertEquals(OpCode.ACK.value, pdu.toByteArray()[0]);
    }
    @Test
    public void shouldIncludePadding() throws Exception {
        Assert.assertEquals(0, pdu.toByteArray()[1]);
    }
    @Test
    public void shouldHavePaddedIDNumber() throws Exception{
        Assert.assertEquals(0, pdu.toByteArray()[2]);
    }
    @Test
    public void shouldHaveCorrectIDNumber() throws Exception{
        Assert.assertEquals(ID, pdu.toByteArray()[3]);
    }
    @Test
    public void shouldHaveCorrectLength() throws Exception{
        Assert.assertEquals(4, pdu.toByteArray().length);
    }

    @Test
    public void shouldBeEqualWhenReadFromStream() throws Exception {
        AllPDUTests.assertThroughStreamEquals(pdu);
    }
}