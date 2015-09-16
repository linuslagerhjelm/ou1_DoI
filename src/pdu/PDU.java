package pdu;

import pdu.pduTypes.JoinPDU;
import pdu.pduTypes.NicksPDU;
import pdu.pduTypes.RegPDU;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Super class of all PDUs with methods for reading PDUs from InputStreams.
 */
public abstract class PDU {
    private static byte[] inputStream;

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

        byte[] inputBytes = inStreamToByteArray(inStream);
        inputStream = inputBytes;

        switch(inputBytes[0]){
            case 12:
                byte[] dataSubstring = Arrays.copyOfRange(inputBytes, 4, inputBytes.length);
                return new JoinPDU(dataSubstring.toString());
            case 19:
                Set<String> nicknames = getNicknamesFromByteArray(inputBytes);
                return new NicksPDU(nicknames);
        }

        return null;
    }
    @Override
    public boolean equals( Object obj){
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        PDU other = (PDU) obj;
        if ( inputStream == null )
        {
            if ( other.inputStream != null )
                return false;
        }
        else if ( !inputStream.equals( other.inputStream ) )
            return false;
        return true;
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

    //TODO: Put methods below here in separate class
    private static byte[] inStreamToByteArray(InputStream in) throws IOException {

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = in.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();

        return buffer.toByteArray();

    }

    private static Set<String> getNicknamesFromByteArray(byte[] stream){
        Set<String> returnSet = new HashSet<>();
        StringBuilder temp = new StringBuilder();
        for(int i = 3; i < stream.length; ++i){
            temp.append(stream[i]);

            if(stream[i] == '\0'){
                returnSet.add(temp.toString());
                temp = new StringBuilder();
            }
        }
        return returnSet;
    }
}
