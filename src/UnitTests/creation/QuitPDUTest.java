package UnitTests.creation;

import UnitTests.AllPDUTests;
import org.junit.Test;
import pdu.OpCode;
import pdu.PDU;
import pdu.pduTypes.QuitPDU;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by linuslagerhjelm on 15-09-24.
 */
public class QuitPDUTest {
    private PDU pdu = new QuitPDU();

    @Test
    public void ShouldHaveCorrectOpCode() {
        Assert.assertEquals(OpCode.QUIT.value, pdu.toByteArray()[0]);
    }

    @Test
    public void ShouldHaveCorrectLength() {
        Assert.assertEquals(4, pdu.toByteArray().length);
    }

    @Test
    public void ShouldBePadded() {
        Assert.assertEquals(0,pdu.toByteArray()[1]);
        Assert.assertEquals(0,pdu.toByteArray()[2]);
        Assert.assertEquals(0,pdu.toByteArray()[3]);
    }

    @Test
    public void shouldBeEqualWhenReadFromStream() throws Exception {
        AllPDUTests.assertThroughStreamEquals(pdu);
    }
}
