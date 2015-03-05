/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.danov.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 *
 * @author Denis
 */
public abstract class UDPClient {

    private int attemps;
    private int bufferSize; // in bytes
    private byte[] buf;
    private DatagramPacket sendPacket;
    private DatagramSocket socket;

    public UDPClient(String serverAddress, int port, int bufferSize, int timeOut, int attemps) throws UnknownHostException, SocketException {
        this.bufferSize = bufferSize;
        this.buf = new byte[bufferSize];
        this.attemps = attemps;
        this.socket = new DatagramSocket();
        this.socket.setSoTimeout(timeOut);
        InetAddress address = InetAddress.getByName(serverAddress);
        this.sendPacket = new DatagramPacket(buf, bufferSize, address, port);
    }
    
    public UDPClient(String serverAddress, int port) throws UnknownHostException, SocketException {
        this(serverAddress, port , 256, 6000, 4);
    }

    public UDPClient() throws UnknownHostException, SocketException {
        this("localhost", 1991 , 256, 6000, 4);
    }

    public int getAttemps() {
        return attemps;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public byte[] getBuf() {
        return buf;
    }

    public DatagramPacket getSendPacket() {
        return sendPacket;
    }

    public void setSendPacket(DatagramPacket sendPacket) {
        this.sendPacket = sendPacket;
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public void setSocket(DatagramSocket socket) {
        this.socket = socket;
    }

}
