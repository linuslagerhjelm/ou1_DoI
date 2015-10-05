package gui;

import client.ClientPDUListener;
import pdu.PDU;
import pdu.pduTypes.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Model for chat module in clients.
 * <br/>
 * TODO replace code where indicated.
 */
public class ChatModule {

    private final List<Listener<String>> joinListeners = new ArrayList<>();
    private final List<Listener<String>> leaveListeners = new ArrayList<>();
    private final List<Listener<String>> messageListeners = new ArrayList<>();
    private ConcurrentHashMap<String,ClientPDUListener> activeClients = new ConcurrentHashMap<>();
    private Socket socket;
    private String nickname;

    /**
     * Creates a new chat module and attempts to join the server at the
     * specified port and address with the given nickname.
     *
     * @param address The address of the server.
     * @param port The port of the server.
     * @param nickname The desired nickname.
     */
    public ChatModule(String address, int port, String nickname) {
        this.nickname = nickname;
        try {
            this.socket = new Socket(InetAddress.getByName(address),port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void startClient(){

        PDU join = new JoinPDU(nickname);
        OutputStream outputStream = null;
        InputStream inStream = null;

        try {
            outputStream = socket.getOutputStream();
            inStream = socket.getInputStream();
            outputStream.write(join.toByteArray());
            PDU pdu = PDU.fromInputStream(inStream);

            if(pdu instanceof NicksPDU){
                Set<String> nicknames = ((NicksPDU)pdu).getNicknames();
                for(String str: nicknames){
                    notifyJoinListeners(str);
                }
                ClientPDUListener cpdul = new ClientPDUListener(inStream,outputStream, nickname, this);
                activeClients.putIfAbsent(cpdul.getNickname(),cpdul);
                Thread t = new Thread(cpdul);
                t.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Call this to send a message to the server.
     *
     * @param message The message to send.
     */
    public void sendMessage(String message) {
        PDU pdu = new MessagePDU(message);
        try {
            OutputStream dataOut = socket.getOutputStream();
            dataOut.write(pdu.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Call this to change nickname.
     *
     * @param newNick The desired nickname.
     */
    public void changeNick(String newNick) {
        System.out.println("Registred chnick");
        PDU pdu = new ChNickPDU(newNick);
        try {
            OutputStream out = socket.getOutputStream();
            out.write(pdu.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Call this to leave the chat.
     */
    public void leave() {
        // TODO and here
        PDU pdu = new ULeavePDU(this.nickname, new Date());
        try {
            OutputStream dataOut = socket.getOutputStream();
            dataOut.write(pdu.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Call this whenever a member joins the chat.
     *
     * @param nickname The nickname of the new member.
     */
    public void notifyJoinListeners(String nickname) {
        joinListeners.forEach(l -> l.update(nickname));
    }

    /**
     * Call this whenever a member leaves the chat.
     *
     * @param nickname The nickname of the leaving member.
     */
    public void notifyLeaveListeners(String nickname) {
        leaveListeners.forEach(l -> l.update(nickname));
    }

    /**
     * Call this whenever a message is received.
     *
     * @param message The message.
     */
    public void notifyMessageListeners(String message) {
        messageListeners.forEach(l -> l.update(message));
    }

    /**
     * Adds a listener to be notified when a new member joins the chat.
     *
     * @param listener The listener whose update method will be called.
     */
    public void addJoinListener(Listener<String> listener) {
        joinListeners.add(listener);
    }

    /**
     * Adds a listener to be notified when a member leaves the chat.
     *
     * @param listener The listener whose update method will be called.
     */
    public void addLeaveListener(Listener<String> listener) {
        leaveListeners.add(listener);
    }

    /**
     * Adds a listener to be notified when a message is received.
     *
     * @param listener The listener whose update method will be called.
     */
    public void addMessageListener(Listener<String> listener) {
        messageListeners.add(listener);
    }

}
