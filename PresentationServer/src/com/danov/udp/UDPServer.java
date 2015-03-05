/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.danov.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Denis
 */
public abstract class UDPServer implements Runnable {

    private int bufferSize; // in bytes
    private int port;
    private InetAddress address;
    private DatagramSocket socket;
    
    public UDPServer(String address, int port, int bufferSize) throws SocketException{
        this.bufferSize = bufferSize;
        this.port = port;
        if(address != null){
            try {
                this.address = InetAddress.getByName(address);
            } catch (UnknownHostException ex) {
                Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.socket = new DatagramSocket(port);
        }else{
            this.socket = new DatagramSocket(port);
            this.address = socket.getInetAddress();
        }
    }

    public UDPServer(int port, int bufferSize) throws SocketException {
        this(null, port, bufferSize);
    }

    public UDPServer(int port) throws SocketException {
        this(null, port, 256);
    }

    public UDPServer() throws SocketException {
        this(null, 1991, 256);
    }

    public void run() {
        byte[] buffer = new byte[bufferSize];
        try {
            while (true) {
                DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
                this.handleCommunication(incoming);

            } // end while
        } catch (IOException ex) {
            System.err.println(ex);
        } finally {
            socket.close();
        }
    }  // end run

    public abstract void handleCommunication(DatagramPacket request) throws IOException;

    public int getBufferSize() {
        return bufferSize;
    }

    public int getPort() {
        return port;
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public InetAddress getAddress() {
        return address;
    }
}
