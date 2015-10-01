package gui;


import pdu.PDU;
import pdu.pduTypes.GetListPDU;
import pdu.pduTypes.SListPDU;

import java.io.ByteArrayInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.*;

/**
 * Class for requesting and receiving server lists.
 * <br/>
 */
public class NameServerModule {

    private Collection<Listener<Collection<String[]>>> listeners =
            new ArrayList<>();

    private ArrayList<SListPDU.ServerEntry> servers = new ArrayList();
    private ArrayList<Integer> seqNo = new ArrayList();

    /**
     * Requests connected servers from the name server
     *
     * @param nameServerAddress the address to the name server
     * @param nameServerPort the port where the name server accepts connections
     */
    public void getServerList(String nameServerAddress, int nameServerPort) {
        try{

            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(1000);
            InetAddress address = InetAddress.getByName(nameServerAddress);
            byte[] getListPDU = new GetListPDU().toByteArray();
            DatagramPacket getListPacket = new DatagramPacket(getListPDU, getListPDU.length, address, nameServerPort);

            servers = new ArrayList<>();
            seqNo = new ArrayList<>();

            socket.send(getListPacket);
            try{
                boolean running = true;
                while(running){
                    byte[] buffer = new byte[65507];
                    DatagramPacket recievePacket = new DatagramPacket(buffer, buffer.length);
                    socket.receive(recievePacket);

                    SListPDU pdu = (SListPDU)PDU.fromInputStream(new ByteArrayInputStream(recievePacket.getData()));
                    Integer currentSeqNo = new Integer(pdu.toByteArray()[1]);

                    if( !seqNo.contains(currentSeqNo) ){
                        List<SListPDU.ServerEntry> temp = pdu.getServerEntries();
                        for(SListPDU.ServerEntry se: temp){
                            servers.add(se);
                        }
                        if(pdu.toByteArray().length < 65507)
                            running = false;
                    } else{
                        running = false;
                    }

                }
            } catch (SocketTimeoutException e) { getServerList(nameServerAddress, nameServerPort); }
        } catch (Exception e) {e.printStackTrace();}

        //Converts server entries to String[]
        ArrayList<String[]> setStrings = new ArrayList<>();
        for(SListPDU.ServerEntry se: servers){
            setStrings.add(se.toStringArray());
        }
        notifyListeners(setStrings);
    }

    /**
     * Adds a listener to be notified when a new server lit has been created.
     *
     * @param listener The listener.
     */
    public void addListener(Listener<Collection<String[]>> listener) {
        listeners.add(listener);
    }

    /**
     * Call this when a server list has been received. The entire list in
     * the GUI will be replaced.
     *
     * @param servers The new server list.
     */
    private void notifyListeners(Collection<String[]> servers) {
        listeners.forEach(l -> l.update(servers));
    }
}
