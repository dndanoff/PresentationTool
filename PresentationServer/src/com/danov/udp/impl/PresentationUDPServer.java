/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.danov.udp.impl;

import com.danov.common.pointer.PointerFrame;
import com.danov.common.robot.InputRobot;
import com.danov.udp.UDPServer;
import com.danov.udp.UDPUtils;
import java.awt.AWTException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.Date;
import javax.swing.JTextArea;

/**
 *
 * @author Denis
 */
public class PresentationUDPServer extends UDPServer {

    private static final String ACK_REQUEST = "ack";
    private static final String ACK_RESPONSE = "rack";
    private static final String NEXT = "next";
    private static final String PREVIOUS = "prev";
    public static final String PREF_PREFIX = "pref:";
    private static final String POINT_PREFIX = "point:";
    private static final String START_POINTER = "pointer";
    private static final String CLEAR_POINTER = "clear";

    private final JTextArea messagesTextArea;
    private final PointerFrame pointerFrame;
    private final InputRobot robot;

    private PresentationUDPServer(Builder builder) throws SocketException, AWTException {
        super(builder.address, builder.port, builder.buffer);
        robot = new InputRobot();
        pointerFrame = new PointerFrame(builder.dynamic);
        this.messagesTextArea = builder.messagesTextArea;
        printMessage("Presentation Server started!");
    }

    public void stop() {
        getSocket().close();
    }

    @Override
    public void handleCommunication(DatagramPacket incoming) throws IOException {
        getSocket().receive(incoming);
        String message = new String(UDPUtils.getActualData(incoming));
        System.out.println("Message recieved");
        System.out.println(message);
        if (ACK_REQUEST.equals(message)) {
            System.out.println("Connection request");
            System.out.println("Client IP " + incoming.getAddress());
            System.out.println("Client port " + incoming.getPort());
            System.out.println("Client is reachable " + incoming.getAddress().isReachable(3000));

            DatagramPacket outgoing = new DatagramPacket(ACK_RESPONSE.getBytes(), ACK_RESPONSE.getBytes().length, incoming.getAddress(), incoming.getPort());
            getSocket().send(outgoing);
            printMessage("Client " + incoming.getAddress() + " wanted connection!");
        } else if (NEXT.equals(message)) {
            boolean pointerWasFrameVisible = pointerFrame.isVisible();
            pointerFrame.setVisible(false);
            robot.right();
            pointerFrame.getNewScreenCondition();
            if (pointerWasFrameVisible) {
                pointerFrame.setVisible(true);
            }
            printMessage("Client " + incoming.getAddress() + " sent next command!");
        } else if (PREVIOUS.equals(message)) {
            boolean pointerWasFrameVisible = pointerFrame.isVisible();
            pointerFrame.setVisible(false);
            robot.left();
            pointerFrame.getNewScreenCondition();
            if (pointerWasFrameVisible) {
                pointerFrame.setVisible(true);
            }
            printMessage("Client " + incoming.getAddress() + " sent previous command!");
        } else if (message.startsWith(POINT_PREFIX)) {
            String valueMessage = message.replace(POINT_PREFIX, "");
            String[] coordinates = valueMessage.split(",");
            pointerFrame.paintDot(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]), Integer.parseInt(coordinates[2]), Integer.parseInt(coordinates[3]));
            printMessage("Client " + incoming.getAddress() + " drew point[" + coordinates[0] + "," + coordinates[1] + "]!");
        } else if (message.startsWith(PREF_PREFIX)) {
            String valueMessage = message.replace(PREF_PREFIX, "");
            String[] preferences = valueMessage.split(",");
            pointerFrame.changeDotSize(Integer.parseInt(preferences[0]));
            pointerFrame.changeDotColor(Integer.parseInt(preferences[1]));
            printMessage("Client " + incoming.getAddress() + " set size " + preferences[0] + ",set color " + preferences[1] + "!");
        } else if (START_POINTER.equals(message)) {
            pointerFrame.getNewScreenCondition();
            pointerFrame.setVisible(!pointerFrame.isVisible());
            printMessage("Client " + incoming.getAddress() + " sent pointer command!");
        } else if (CLEAR_POINTER.equals(message)) {
            pointerFrame.clearPointer();
            printMessage("Client " + incoming.getAddress() + " sent clear pointer command!");
        } else {
            printMessage("Unknown message.");
        }
    }

    public final void printMessage(String message) {
        if (messagesTextArea != null) {
            messagesTextArea.append(new Date().toString() + ":" + message + "\n");
        } else {
            System.out.println(new Date().toString() + ":" + message);
        }
    }

    public PointerFrame getPointerFrame() {
        return pointerFrame;
    }

    public InputRobot getRobot() {
        return robot;
    }

    public static class Builder {
// Required parameters

        private int port;
// Optional parameters - initialized to default values
        private String address = null;
        private int buffer = -1;
        private JTextArea messagesTextArea = null;
        private boolean dynamic = false;

        public Builder(int port) {
            this.port = port;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder dynamic(boolean dynamic) {
            this.dynamic = dynamic;
            return this;
        }

        public Builder buffer(int buffer) {
            this.buffer = buffer;
            return this;
        }

        public Builder messagesTextArea(JTextArea messagesTextArea) {
            this.messagesTextArea = messagesTextArea;
            return this;
        }

        public PresentationUDPServer build() throws SocketException, AWTException {
            return new PresentationUDPServer(this);
        }
    }

}
