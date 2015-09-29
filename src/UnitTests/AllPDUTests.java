
package UnitTests;


import UnitTests.creation.*;
import UnitTests.streaming.StreamTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import pdu.PDU;
import pdu.pduTypes.GetListPDU;
import pdu.pduTypes.QuitPDU;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;


@SuppressWarnings("OverlyCoupledClass")
@RunWith(Suite.class)
@Suite.SuiteClasses({
        /*AckPDUTest.class,
        AlivePDUTest.class,
        ChNickPDUTest.class,
        GetListPDUTest.class,
        NotRegPDUTest.class,
        QuitPDUTest.class,
        RegPDUTest.class,*/
        UCHNickPDUTest.class,
        JoinPDUTest.class,
        MessagePDUTest.class,
        NicksPDUTest.class,
        SListPDUTest.class,
        UJoinPDUTest.class,
        StreamTest.class
})
public class AllPDUTests {

    public static void assertThroughStreamEquals(PDU pdu) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(pdu.toByteArray());
        PDU inPdu = PDU.fromInputStream(
                new ByteArrayInputStream(
                        outputStream.toByteArray()));
        assertEquals(pdu, inPdu);
    }
}
