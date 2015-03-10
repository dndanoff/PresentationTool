/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.danov.udp;

import com.danov.IPresentClient;
import com.danov.udp.utils.UDPUtils;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Denis
 */
public class PresentationUDPClient extends UDPClient implements IPresentClient {

    private static final String ACK_REQUEST = "ack";
    private static final String ACK_RESPONSE = "rack";

    public PresentationUDPClient() throws UnknownHostException, SocketException {
        super();
    }

    public PresentationUDPClient(String serverAddress, int port, int bufferSize, int timeOut, int attemps) throws UnknownHostException, SocketException {
        super(serverAddress, port, bufferSize, timeOut, attemps);
    }

    public PresentationUDPClient(String serverAddress, int port) throws UnknownHostException, SocketException {
        super(serverAddress, port);
    }

    public boolean connect() {
        byte[] recieveData = new byte[getBufferSize()];
        try {
            sendTextMessage(ACK_REQUEST);
        } catch (IOException ex) {
            Logger.getLogger(PresentationUDPClient.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        for (int i = 0; i < getAttemps(); i++) {
            DatagramPacket incoming = new DatagramPacket(recieveData, recieveData.length);
            try {
                getSocket().receive(incoming);
                if (incoming.getData() != null) {
                    String message = new String(UDPUtils.getActualData(incoming));
                    if (ACK_RESPONSE.equalsIgnoreCase(message)) {
                        return true;
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(PresentationUDPClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    public void sendPreferenceMessage(int dotSize, int dotColor) {
        String message = PREF_PREFIX + dotSize + "," + dotColor;
        try {
            sendTextMessage(message);
            System.out.println("Message " + message + " successfully sent");
        } catch (IOException ex) {
            Logger.getLogger(PresentationUDPClient.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Message " + message + " did not sent");
        }
    }

    public void sendPointCoordinates(int x, int y, int width, int height) {
        String message = POINT_PREFIX + x + "," + y + "," + width + "," + height;
        try {
            sendTextMessage(message);
            System.out.println("Message " + message + " successfully sent");
        } catch (IOException ex) {
            Logger.getLogger(PresentationUDPClient.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Message " + message + " did not sent");
        }
    }

    public void clearPointer() {
        try {
            sendTextMessage(CLEAR_POINTER);
            System.out.println("Message " + CLEAR_POINTER + " successfully sent");
        } catch (IOException ex) {
            Logger.getLogger(PresentationUDPClient.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Message " + CLEAR_POINTER + " did not sent");
        }
    }

    public void sendPointerMessage() {
        try {
            sendTextMessage(START_POINTER);
            System.out.println("Message " + START_POINTER + " successfully sent");
        } catch (IOException ex) {
            Logger.getLogger(PresentationUDPClient.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Message " + START_POINTER + " did not sent");
        }
    }

    public boolean sendNextMessage() {
        try {
            sendTextMessage(NEXT);
            System.out.println("Message " + NEXT + " successfully sent");
        } catch (IOException ex) {
            Logger.getLogger(PresentationUDPClient.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Message " + NEXT + " did not sent");
            return false;
        }
        return true;
    }

    public boolean sendPrevMessage() {
        try {
            sendTextMessage(PREVIOUS);
            System.out.println("Message " + PREVIOUS + " successfully sent");
        } catch (IOException ex) {
            Logger.getLogger(PresentationUDPClient.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Message " + PREVIOUS + " did not sent");
            return false;
        }
        return true;
    }

    private void sendTextMessage(String message) throws IOException {
        getSendPacket().setData(message.getBytes());
        getSocket().send(getSendPacket());
    }

    public void reconnect() {
        System.out.println("No need to reconect");
    }

    public void disconnect() {
        System.out.println("No need to disconenct");
    }

}
