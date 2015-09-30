package gui;

import pdu.PDU;
import pdu.pduTypes.JoinPDU;
import pdu.pduTypes.MessagePDU;
import pdu.pduTypes.SListPDU;
import pdu.pduTypes.ULeavePDU;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Model for chat module in clients.
 * <br/>
 * TODO replace code where indicated.
 */
public class ChatModule {

    private final List<Listener<String>> joinListeners = new ArrayList<>();
    private final List<Listener<String>> leaveListeners = new ArrayList<>();
    private final List<Listener<String>> messageListeners = new ArrayList<>();
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
            PDU pdu = new JoinPDU(nickname);
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(pdu.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
        }

        // TODO replace with other stuff, threads for example
        /*System.err.println(
                "Attempting to join server at " + address + ":" + port +
                " with nickname \"" + nickname + "\"");*/
    }

    /**
     * Call this to send a message to the server.
     *
     * @param message The message to send.
     */
    public void sendMessage(String message) {
        // TODO actually send the message
        PDU pdu = new MessagePDU(message);
        try {
            OutputStream dataOut = socket.getOutputStream();
            dataOut.write(pdu.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.err.println("Sending message: \"" + message + "\"");
    }

    /**
     * Call this to change nickname.
     *
     * @param newNick The desired nickname.
     */
    public void changeNick(String newNick) {
        // TODO do smth here
        System.err.println("Changing nickname to: \"" + newNick + "\"");
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
    private void notifyJoinListeners(String nickname) {
        joinListeners.forEach(l -> l.update(nickname));
    }

    /**
     * Call this whenever a member leaves the chat.
     *
     * @param nickname The nickname of the leaving member.
     */
    private void notifyLeaveListeners(String nickname) {
        leaveListeners.forEach(l -> l.update(nickname));
    }

    /**
     * Call this whenever a message is received.
     *
     * @param message The message.
     */
    private void notifyMessageListeners(String message) {
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
