/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.danov.udp.impl;

import com.danov.udp.UDPClient;
import com.danov.udp.UDPUtils;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Denis
 */
public class PresentationUDPClient extends UDPClient {

    private static final String ACK_REQUEST = "ack";
    private static final String ACK_RESPONSE = "rack";
    private static final String NEXT = "next";
    private static final String PREVIOUS = "prev";
    private static final String POINT_PREFIX = "point:";
    private static final String START_POINTER = "pointer";

    public PresentationUDPClient() throws UnknownHostException, SocketException {
        super();
    }

    public PresentationUDPClient(String serverAddress, int port, int bufferSize, int timeOut, int attemps) throws UnknownHostException, SocketException {
        super(serverAddress,port,bufferSize,timeOut,attemps);
    }
    
    public PresentationUDPClient(String serverAddress, int port) throws UnknownHostException, SocketException {
        super(serverAddress, port);
    }
    
    @Override
    public void handleCommunication() throws IOException {
        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();
        while (input != -1) {
            switch (input) {
                case 0:
                    if(startAckProtocol()){
                        System.out.println("Success");
                    }else{
                        System.out.println("Fail");
                    }
                    break;
                case 1:
                    sendNextMessage();
                    break;
                case 2:
                    sendPrevMessage();
                    break;
                case 3:
                    sendPointerMessage();
                    break;
                case 4:
                    sendPointCoordinates(50, 50);
                    break;
                default:
                    break;
            }
            input = scanner.nextInt();
        }
    }

    private boolean startAckProtocol() {
        try {
            sendTextMessage(ACK_REQUEST);
        } catch (IOException ex) {
            Logger.getLogger(PresentationUDPClient.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        for (int i = 0; i < getAttemps(); i++) {
            DatagramPacket incoming = new DatagramPacket(getBuf(), getBufferSize());
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

    private void sendPointCoordinates(int x, int y) {
        String message = POINT_PREFIX + x + "," + y;
        try {
            sendTextMessage(message);
            System.out.println("Message " + message + " successfully sent");
        } catch (IOException ex) {
            Logger.getLogger(PresentationUDPClient.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Message " + message + " did not sent");
        }
    }

    private void sendPointerMessage() {
        try {
            sendTextMessage(START_POINTER);
            System.out.println("Message " + START_POINTER + " successfully sent");
        } catch (IOException ex) {
            Logger.getLogger(PresentationUDPClient.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Message " + START_POINTER + " did not sent");
        }
    }

    private void sendNextMessage() {
        try {
            sendTextMessage(NEXT);
            System.out.println("Message " + NEXT + " successfully sent");
        } catch (IOException ex) {
            Logger.getLogger(PresentationUDPClient.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Message " + NEXT + " did not sent");
        }
    }

    private void sendPrevMessage() {
        try {
            sendTextMessage(PREVIOUS);
            System.out.println("Message " + PREVIOUS + " successfully sent");
        } catch (IOException ex) {
            Logger.getLogger(PresentationUDPClient.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Message " + PREVIOUS + " did not sent");
        }
    }

    private void sendTextMessage(String message) throws IOException {
        getSendPacket().setData(message.getBytes());
        getSocket().send(getSendPacket());
    }

}
