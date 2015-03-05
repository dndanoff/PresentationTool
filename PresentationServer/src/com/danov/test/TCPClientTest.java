package com.danov.test;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author x
 */
public class TCPClientTest {

    private static final String NEXT = "next";
    private static final String PREV = "prev";

    private final String serverAddress;
    private final Integer serverPort;
    private Socket connection;

    public TCPClientTest(String serverAddress, Integer serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        System.out.println("Client server and port are set to " + serverAddress + ":" + serverPort);
    }
    
    public static void main(String[] args) {
        TCPClientTest test = new TCPClientTest("localhost",1991);
        test.connect();
        test.next();
    }

    public boolean connect() {
        String timeStamp;
        System.out.println("SocketClient initializing...");
        try {
            InetAddress address = InetAddress.getByName(serverAddress);
            connection = new Socket(address, serverPort);
            PrintWriter out = new PrintWriter(connection.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            timeStamp = new java.util.Date().toString();
            String process = "Calling the Socket Server on " + serverAddress + " port " + serverPort + " at " + timeStamp;
            out.println(process);
        } catch (UnknownHostException ex) {
            Logger.getLogger(TCPClientTest.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IOException ex) {
            Logger.getLogger(TCPClientTest.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public boolean next() {
        System.out.println("Calling next...");
        try {
            PrintWriter out = new PrintWriter(connection.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            out.println(NEXT);
        } catch (UnknownHostException ex) {
            Logger.getLogger(TCPClientTest.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IOException ex) {
            Logger.getLogger(TCPClientTest.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public boolean prev() {
        System.out.println("Calling prev...");
        try {
            PrintWriter out = new PrintWriter(connection.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            out.println(PREV);
        } catch (UnknownHostException ex) {
            Logger.getLogger(TCPClientTest.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IOException ex) {
            Logger.getLogger(TCPClientTest.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public Integer getServerPort() {
        return serverPort;
    }
}
