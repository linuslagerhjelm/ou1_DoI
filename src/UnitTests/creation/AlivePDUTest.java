package UnitTests.creation;

import UnitTests.AllPDUTests;
import org.junit.Assert;
import org.junit.Test;
import pdu.OpCode;
import pdu.PDU;
import pdu.pduTypes.AlivePDU;

/**
 * Created by linuslagerhjelm on 15-09-21.
 */
public class AlivePDUTest {
    private final byte clientCount = 1;
    private final short ID = 20;
    private final PDU pdu = new AlivePDU(clientCount, ID);

    @Test
    public void shouldHaveCorrectOpCode() throws Exception{
        Assert.assertEquals(OpCode.ALIVE.value, pdu.toByteArray()[0]);
    }

    @Test
    public void shouldHaveCorrectClientCount() throws Exception{
        Assert.assertEquals(clientCount, pdu.toByteArray()[1]);
    }

    @Test
    public void shouldHaveCorrectID() throws Exception{
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
