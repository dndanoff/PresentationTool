/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.danov.udp;

import java.net.DatagramPacket;

/**
 *
 * @author x
 */
public class UDPUtils {

    public static byte[] getActualData(DatagramPacket incoming) {
        byte[] data = new byte[incoming.getLength()];
        System.arraycopy(incoming.getData(), incoming.getOffset(), data, 0, incoming.getLength());
        return data;
    }
}
