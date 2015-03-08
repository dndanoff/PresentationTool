/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.danov.tcp;

import java.awt.AWTException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

/**
 *
 * @author x
 */
public class PresentationServer implements Runnable{

    private Integer port;
    private final String address;
    private final JTextArea textArea;
    private final boolean dynamic;
    private static ServerSocket server;
    private static ServerDispatcher serverDispatcher;
    private static final int MAX_CLIENTS_COUNT = 5;

    private PresentationServer(Builder builder) {
        this.port = builder.port;
        this.textArea = builder.messagesTextArea;
        this.address = builder.address;
        this.dynamic = builder.dynamic;
    }
    
    @Override
    public void run() {
        // Start listening on the server socket 
        bindServerSocket();
        try {
            serverDispatcher = new ServerDispatcher.Builder().messagesTextArea(textArea).dynamic(dynamic).build();
            serverDispatcher.start();
            
            // Infinitely accept and handle client connections
            if(textArea != null){
                textArea.append(new Date().toString()+":MultipleSocketServer starts to accpet connections"+"\n");
            }else{
                System.out.println(new Date().toString()+":MultipleSocketServer starts to accpet connections");
            }
            handleClientConnections();
        } catch (AWTException ex) {
            Logger.getLogger(PresentationServer.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
    }
    
    public void stop(){
        if(server != null){
            try {
                server.close();
            } catch (IOException ex) {
                Logger.getLogger(PresentationServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void bindServerSocket() {
        try {
            server = new ServerSocket(port);
            if(textArea != null){
                textArea.append(new Date().toString()+":PresentationServer started on " + "port " + port+"\n");
            }else{
                System.out.println(new Date().toString()+":PresentationServer started on " + "port " + port);
            }
        } catch (IOException ioe) {
            if(textArea != null){
                textArea.append(new Date().toString()+":Cannot start listening on " + "port " + port+"\n");
            }else{
                System.err.println(new Date().toString()+":Cannot start listening on " + "port " + port);
            }
            ioe.printStackTrace();
            System.exit(-1);
        }
    }

    private void handleClientConnections() {
        while (true) {
            if (serverDispatcher.getClientsCount() < MAX_CLIENTS_COUNT) {
                try {
                    Socket socket = server.accept();
                    ClientWorker clientWorker = new ClientWorker(socket, serverDispatcher);
                    serverDispatcher.addClient(clientWorker);
                    clientWorker.start();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
    
    public static class Builder {
// Required parameters
        private int port;
// Optional parameters - initialized to default values
        private String address = "localhost";
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

        public Builder messagesTextArea(JTextArea messagesTextArea) {
            this.messagesTextArea = messagesTextArea;
            return this;
        }

        public PresentationServer build() {
            return new PresentationServer(this);
        }
    }

}
