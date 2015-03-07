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

    private final int bufferSize; // in bytes
    private final int port;
    private InetAddress address;
    private final DatagramSocket socket;
    
    public UDPServer(String address, int port, int bufferSize) throws SocketException{
        this.port = port;
        
        if(bufferSize > 0){
            this.bufferSize = bufferSize;
        }else{
           this.bufferSize = 256;
        }
        
        if(address != null){
            try {
                this.address = InetAddress.getByName(address);
            } catch (UnknownHostException ex) {
                Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.socket = new DatagramSocket(this.port);
        }else{
            this.socket = new DatagramSocket(this.port);
            this.address = socket.getInetAddress();
        }
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
