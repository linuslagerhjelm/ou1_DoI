package pdu;

import com.sun.corba.se.spi.activation.Server;
import pdu.pduTypes.*;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Super class of all PDUs with methods for reading PDUs from InputStreams.
 */
public abstract class PDU {
    //private InputStream inputStream;


    /**
     * Reads the OpCode from the InputStream and determines the type of
     * the PDU. Then a PDU of the correct subclass is read from the stream.
     *
     * @param inStream The InputStream to read the PDU from.
     * @return The read PDU.
     * @throws java.io.IOException If an IOException was thrown when reading from the
     *          stream.
     * @throws IllegalArgumentException If the first byte of the stream
     *          doesn't represent a correct OpCode.
     */
    public static PDU fromInputStream(InputStream inStream) throws IOException {

        int pduChooser = (int)byteArrayToLong(readExactly(inStream,1));
        switch(pduChooser){
            case 0:
                return new RegPDU(inStream);
            case 1:
                return new AckPDU(inStream);
            case 2:
                return new AlivePDU(inStream);
            case 3:
                return new GetListPDU(inStream);
            case 4:
                return new SListPDU(inStream);
            case 10:
                return new MessagePDU(inStream);
            case 11:
                return new QuitPDU(inStream);
            case 12:
                return new JoinPDU(inStream);
            case 13:
                return new ChNickPDU(inStream);
            case 16:
                return new UJoinPDU(inStream);
            case 17:
                return new ULeavePDU(inStream);
            case 18:
                return new UCNickPDU(inStream);
            case 19:
                return new NicksPDU(inStream);
            case 100:
                return new NotRegPDU(inStream);
            default:
                return null;
        }
    }

    /**
     * Equals generated by Intellij to compare with toByteArray
     * @param o Object to compare current object with
     * @return whether this object and the given object is the same
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PDU pdu = (PDU) o;

        return !(toByteArray() != null ?
                !Arrays.equals(toByteArray(), pdu.toByteArray()) :
                pdu.toByteArray() != null);
    }

    /**
     * Hashcode generated by Intellij to generate hashcode based on toByteArray
     * @return The hashcode
     */
    @Override
    public int hashCode() {
        return toByteArray() != null ? Arrays.hashCode(toByteArray()) : 0;
    }

    /**
     * @return A byte array representation of the PDU with a length divisible
     * by 4.
     */
    public abstract byte[] toByteArray();

    /**
     * Computes the sum of the specified lengths, padding each
     * individually to be divisible by 4.
     *
     * @param lengths The lengths to pad.
     * @return The padded sum.
     */
    public static int padLengths(int... lengths) {
        int result = 0;
        for (int length : lengths) {
            if (length % 4 != 0) {
                result += length + (4 - (length % 4));
            } else {
                result += length;
            }
        }
        return result;
    }

    /**
     * Reads exactly the specified amount of bytes from the stream, blocking
     * until they are available even though some bytes are.
     *
     * @param is The InputStream to read from.
     * @param len The number of bytes to read.
     * @return A byte array containing the read bytes.
     * @throws IllegalArgumentException If the number of bytes to read is
     *                      negative.
     * @throws java.io.IOException If an IOException was thrown when reading from the
     *                      stream.
     */
    public static byte[] readExactly(InputStream is, int len)
            throws IOException {

        if (len < 0) {
            throw new IllegalArgumentException("Negative length to read");
        }
        byte[] buffer = new byte[len];

        int readCount = 0;
        while (readCount < len) {
            int readBytes = is.read(buffer, readCount, len - readCount);
            readCount += readBytes;
        }

        return buffer;
    }

    public static long byteArrayToLong(byte[] bytes) {
        return byteArrayToLong(bytes, 0, bytes.length);
    }

    public static long byteArrayToLong(byte[] bytes, int start, int stop) {
        if (stop - start > 8) {
            throw new IllegalArgumentException(
                    "Byte array can't have more than 8 bytes");
        }
        long result = 0;
        for (int i = start; i < stop; i++) {
            result <<= 8;
            result += ((long) bytes[i]) & 0xff;
        }
        return result;
    }

    public static byte[] inStreamToByteArray(InputStream in) {

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];
        try {
            while ((nRead = in.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
        } catch (IOException e){
            e.getMessage();
        } catch (Exception e){}
        return buffer.toByteArray();
    }
    /**
     * Converts a 4 byte array of unsigned bytes to an long
     * @param in an array of 4 unsigned bytes
     * @return a long representing the unsigned int
     */
    public static long unsignedIntToLong(byte[] in)
    {
        byte[] b = in;
        long l = 0;
        l |= b[0] & 0xFF;
        l <<= 8;
        l |= b[1] & 0xFF;
        l <<= 8;
        l |= b[2] & 0xFF;
        l <<= 8;
        l |= b[3] & 0xFF;
        return l;
    }

    public static int byteArrayToInt(byte[] b)
    {
        return   b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    public static short byteArrayToShort(byte[] b)
    {
        return   (short) (b[1] & 0xFF |
                (b[0] & 0xFF) << 8);
    }
    public short bytesToShort(byte[] bytes) {
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }
}
