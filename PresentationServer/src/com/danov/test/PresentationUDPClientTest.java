/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.danov.test;

import com.danov.udp.impl.PresentationUDPClient;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 *
 * @author Denis
 */
public class PresentationUDPClientTest {

    public static void main(String[] args) throws UnknownHostException, SocketException {
        PresentationUDPClient client = new PresentationUDPClient("192.168.200.181", 1991);
        Thread thread = new Thread(client);
        thread.start();
    }
}
