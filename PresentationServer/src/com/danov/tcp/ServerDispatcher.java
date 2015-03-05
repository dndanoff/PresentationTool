/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.danov.tcp;

import com.danov.pointer.PointerFrame;
import java.awt.AWTException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.swing.JTextArea;

/**
 *
 * @author x
 */
public class ServerDispatcher extends Thread {

    private static final String NEXT = "next";
    private static final String PREV = "prev";
    private static final String POINT_PREFIX = "point:";
    private static final String START_POINTER = "pointer";
    private static final String CLEAR_POINTER = "clear";

    private JTextArea messagesTextArea;
    private final PointerFrame pointerFrame;
    private static InputRobot robot;

    private final List<String> messageQueue;
    private final List<PresentationClient> clients;

    public ServerDispatcher(JTextArea messagesTextArea) throws AWTException {
        this.messagesTextArea = messagesTextArea;
        this.pointerFrame = new PointerFrame();
        robot = new InputRobot();

        messageQueue = Collections.synchronizedList(new ArrayList<String>());
        clients = Collections.synchronizedList(new ArrayList<PresentationClient>());
    }

    public ServerDispatcher() throws AWTException {
        this(null);
    }

    /**
     * Infinitely reads messages from the queue and dispatches them to all
     * clients connected to the server.
     */
    @Override
    public void run() {
        try {
            while (true) {
                String message = getNextMessageFromQueue();
                if (NEXT.equalsIgnoreCase(message)) {
                    boolean pointerWasFrameVisible = pointerFrame.isVisible();
                    printMessage(message);
                    pointerFrame.setVisible(false);
                    robot.right();
                    pointerFrame.getNewScreenCondition();
                    if (pointerWasFrameVisible) {
                        pointerFrame.setVisible(true);
                    }
                } else if (PREV.equalsIgnoreCase(message)) {
                    boolean pointerWasFrameVisible = pointerFrame.isVisible();
                    printMessage(message);
                    pointerFrame.setVisible(false);
                    robot.left();
                    pointerFrame.getNewScreenCondition();
                    if (pointerWasFrameVisible) {
                        pointerFrame.setVisible(true);
                    }
                } else if (message.startsWith(POINT_PREFIX)) {
                    String valueMessage = message.replace(POINT_PREFIX, "");
                    String[] coordinates = valueMessage.split(",");
                    pointerFrame.paintDot(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]),Integer.parseInt(coordinates[2]),Integer.parseInt(coordinates[3]));
//                    printMessage("Client " + incoming.getAddress() + " drew point[" + coordinates[0] + "," + coordinates[1] + "]!");
                } else if (START_POINTER.equals(message)) {
                    pointerFrame.getNewScreenCondition();
                    pointerFrame.setVisible(!pointerFrame.isVisible());
//                    printMessage("Client " + incoming.getAddress() + " sent pointer command!");
                } else if (CLEAR_POINTER.equals(message)) {
                    pointerFrame.clearPointer();
//                    printMessage("Client " + incoming.getAddress() + " sent clear pointer command!");
                } else {
                    if (messagesTextArea != null) {
                        messagesTextArea.append(message + "\n");
                    } else {
                        printMessage(message);
                    }

                }
            }
        } catch (InterruptedException ie) {
            // Thread interrupted. Stop its execution 
        }
    }

    public void printMessage(String message) {
        if (messagesTextArea != null) {
            messagesTextArea.append(new Date().toString() + ":" + message + "\n");
        } else {
            System.out.println(new Date().toString() + ":" + message);
        }
    }

    public synchronized int getClientsCount() {
        return clients.size();
    }

    /**
     * Adds given client to the server's client list.
     *
     * @param aClient
     */
    public synchronized void addClient(PresentationClient aClient) {
        clients.add(aClient);
    }

    /**
     * Deletes given client from the server's client list if the client is in
     * the list
     *
     * @param aClient.
     */
    public synchronized void deleteClient(PresentationClient aClient) {
        int clientIndex = clients.indexOf(aClient);
        if (clientIndex != -1) {
            clients.remove(clientIndex);
            printMessage(aClient.getConnection().getInetAddress().getHostAddress() + " disconnected");
        }
    }

    /**
     * Adds given message to the dispatcher's message queue and notifies this
     * thread to wake up the message queue reader (getNextMessageFromQueue
     * method). dispatchMessage method is called by other threads
     * (ClientListener) when a message is arrived.
     *
     * @param aClient
     * @param message
     */
    public synchronized void dispatchMessage(PresentationClient aClient, String message) {
        Socket socket = aClient.getConnection();
        String senderIP
                = socket.getInetAddress().getHostAddress();
        String senderPort = "" + socket.getPort();
        String aMessage = senderIP + ":" + senderPort
                + " : " + message;
        messageQueue.add(aMessage);
        messageQueue.add(message);
        notify();
    }

    /**
     * @return and deletes the next message from the message queue. If there is
     * no messages in the queue, falls in sleep until notified by
     * dispatchMessage method.
     */
    private synchronized String getNextMessageFromQueue()
            throws InterruptedException {
        while (messageQueue.isEmpty()) {
            wait();
        }
        String message = (String) messageQueue.get(0);
        messageQueue.remove(0);
        return message;
    }
}
