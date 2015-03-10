/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.danov.tcp;

import com.danov.IPresentClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author x
 */
public class PresentationClient implements IPresentClient {

    private static final String EXIT = "close";

    private final String serverAddress;
    private final Integer serverPort;
    private transient Socket connection;

    public PresentationClient(String serverAddress, Integer serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        System.out.println("Client server and port are set to " + serverAddress + ":" + serverPort);
    }

    public boolean connect() {
        String timeStamp;
        System.out.println("SocketClient initializing...");
        try {
            InetAddress address = InetAddress.getByName(serverAddress);
//            InetAddress address = InetAddress.getByName("192.168.1.101");
            connection = new Socket(address, serverPort);
            PrintWriter out = new PrintWriter(connection.getOutputStream(), true);
            timeStamp = new java.util.Date().toString();
            String process = "Calling the Socket Server on " + serverAddress + " port " + serverPort + " at " + timeStamp + (char) 13;
            out.println(process);
            out.flush();
        } catch (UnknownHostException ex) {
            Logger.getLogger(PresentationClient.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IOException ex) {
            Logger.getLogger(PresentationClient.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public void reconnect() {
        try {
            InetAddress address = InetAddress.getByName("192.168.1.100");
            try {
                connection = new Socket(serverAddress, serverPort);
            } catch (IOException ex) {
                Logger.getLogger(PresentationClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(PresentationClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void disconnect() {
        try {
            PrintWriter out = new PrintWriter(connection.getOutputStream(), true);
            out.println(EXIT);
            out.flush();
            connection.close();
        } catch (IOException ex) {
            Logger.getLogger(PresentationClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendPreferenceMessage(int dotSize, int dotColor){
        String message = PREF_PREFIX + dotSize + "," + dotColor;
        sendTextMessage(message);
        System.out.println("Message " + message + " successfully sent");
    }

    public boolean sendNextMessage() {
        System.out.println("Calling next...");
        try {
            PrintWriter out = new PrintWriter(connection.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            out.println(NEXT);
            out.flush();
        } catch (UnknownHostException ex) {
            Logger.getLogger(PresentationClient.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IOException ex) {
            Logger.getLogger(PresentationClient.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public boolean sendPrevMessage() {
        System.out.println("Calling prev...");
        try {
            PrintWriter out = new PrintWriter(connection.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            out.println(PREVIOUS);
            out.flush();
        } catch (UnknownHostException ex) {
            Logger.getLogger(PresentationClient.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IOException ex) {
            Logger.getLogger(PresentationClient.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public void sendPointCoordinates(int x, int y, int width, int height) {
        String message = POINT_PREFIX + x + "," + y + "," + width + "," + height;
        sendTextMessage(message);
        System.out.println("Message " + message + " successfully sent");
    }

    public void clearPointer() {
        sendTextMessage(CLEAR_POINTER);
        System.out.println("Message " + CLEAR_POINTER + " successfully sent");
    }

    public void sendPointerMessage() {
        sendTextMessage(START_POINTER);
        System.out.println("Message " + START_POINTER + " successfully sent");
    }

    private void sendTextMessage(String message) {
        try {
            PrintWriter out = new PrintWriter(connection.getOutputStream(), true);
            out.println(message);
            out.flush();
        } catch (UnknownHostException ex) {
            Logger.getLogger(PresentationClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PresentationClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public Integer getServerPort() {
        return serverPort;
    }
}
