package com.danov.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Objects;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author x
 */
public class ClientWorker extends Thread {
    private static final String EXIT = "close";

    private final ServerDispatcher serverDispatcher;
    private final Socket connection;
    private BufferedReader socketReader;
    private final String ID;

    public ClientWorker(Socket connection, ServerDispatcher serverDispatcher) {
        this.ID = connection.getInetAddress().toString();
        this.connection = connection;
        this.serverDispatcher = serverDispatcher;
        try {
            this.socketReader = new BufferedReader(
                    new InputStreamReader(this.connection.getInputStream()));
        } catch (IOException e) {
            serverDispatcher.dispatchMessage(this,"Opening streams failed", true);
            System.exit(-1);
        }
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                try {
                    String line = socketReader.readLine();
                    if (line == null) {
                        System.out.println("No messages from client");
                    }else if(line.equals(EXIT)){
                        serverDispatcher.deleteClient(this);
                        break;
                    }else{
                        serverDispatcher.dispatchMessage(this, line, false);
                        serverDispatcher.dispatchMessage(this,line + " added to queue",true);
                    }
                } catch (SocketTimeoutException ste) {
                    break;
                }
            }
        } catch (IOException ioex) {
            // Problem reading from socket (broken connection)
            serverDispatcher.dispatchMessage(this,"Read failed",true);
        } finally{
            // Communication is broken. Interrupt both listener and 
            // sender threads 
            serverDispatcher.deleteClient(this);
        }
    }

    public BufferedReader getSocketReader() {
        return socketReader;
    }

    public void setSocketReader(BufferedReader socketReader) {
        this.socketReader = socketReader;
    }

    public Socket getConnection() {
        return connection;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.ID);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ClientWorker other = (ClientWorker) obj;
        if (!Objects.equals(this.ID, other.ID)) {
            return false;
        }
        return true;
    }
    
    

}
