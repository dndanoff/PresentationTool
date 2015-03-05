/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.danov.tcp;

import com.danov.pointer.PointerFrame;
import java.awt.AWTException;
import java.io.IOException;
import java.net.InetAddress;
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
    private String address;
    private JTextArea textArea;
    private static ServerSocket server;
    private static ServerDispatcher serverDispatcher;
    private static final int MAX_CLIENTS_COUNT = 5;

    public PresentationServer(Integer port) {
        this(port,"localhost",null);
    }

    public PresentationServer(Integer port, String address, JTextArea textArea) {
        this.port = port;
        this.textArea = textArea;
    }
    
    @Override
    public void run() {
        // Start listening on the server socket 
        bindServerSocket();
        try {
            serverDispatcher = new ServerDispatcher(textArea);
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
                    PresentationClient client = new PresentationClient(socket);
                    ClientWorker clietnWorker = new ClientWorker(client, serverDispatcher,
                            client.getConnection().getInetAddress().toString());
                    client.setWorker(clietnWorker);
                    clietnWorker.start();
                    serverDispatcher.addClient(client);
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

}
