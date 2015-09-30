package UnitTests.creation;

import UnitTests.AllPDUTests;
import org.junit.Assert;
import org.junit.Test;
import pdu.OpCode;
import pdu.PDU;
import pdu.pduTypes.NotRegPDU;


/**
 * Created by linuslagerhjelm on 15-09-21.
 */
public class NotRegPDUTest {
    private static final PDU pdu = new NotRegPDU();

    @Test
    public void shouldHaveCorrectOpCode() throws Exception {
        Assert.assertEquals(OpCode.NOTREG.value, pdu.toByteArray()[0]);
    }

    @Test
    public void shouldBePaddedCorrectly() throws Exception {
        Assert.assertEquals(0, pdu.toByteArray()[1]);
        Assert.assertEquals(0, pdu.toByteArray()[2]);
        Assert.assertEquals(0, pdu.toByteArray()[3]);
    }

    @Test
    public void shouldHaveCorrectLength() throws Exception {
        Assert.assertEquals(4, pdu.toByteArray().length);
    }

    @Test
    public void shouldBeEqualWhenReadFromStream() throws Exception {
        AllPDUTests.assertThroughStreamEquals(pdu);
    }
}
