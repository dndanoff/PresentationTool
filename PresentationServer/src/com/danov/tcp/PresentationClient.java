/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.danov.tcp;

import java.net.Socket;

/**
 *
 * @author x
 */
public class PresentationClient {

    private Socket connection = null;
    private ClientWorker worker = null;
    
    public PresentationClient(Socket connection) {
        this.connection = connection;
    }

    public PresentationClient(Socket connection, ClientWorker worker) {
        this.connection = connection;
        this.worker = worker;
    }

    public Socket getConnection() {
        return connection;
    }

    public ClientWorker getWorker() {
        return worker;
    }

    public void setWorker(ClientWorker worker) {
        this.worker = worker;
    }

}
