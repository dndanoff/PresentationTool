package com.danov.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;

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
    private final PresentationClient client;
    private BufferedReader socketReader;
    private final String ID;

    public ClientWorker(PresentationClient client, ServerDispatcher serverDispatcher, String id) {
        this.ID = id;
        this.client = client;
        this.serverDispatcher = serverDispatcher;
        try {
            this.socketReader = new BufferedReader(
                    new InputStreamReader(client.getConnection().getInputStream()));
        } catch (IOException e) {
            serverDispatcher.printMessage("Opening streams failed");
            System.exit(-1);
        }
    }

    public void run() {
        try {
            while (!isInterrupted()) {
                try {
                    String line = socketReader.readLine();
                    if (line == null) {
                        System.out.println("No messages from client");
                    }else if(line.equals(EXIT)){
                        serverDispatcher.deleteClient(client);
                        break;
                    }else{
                        serverDispatcher.dispatchMessage(client, line);
                        serverDispatcher.printMessage(line + " added to queue");
                    }
                } catch (SocketTimeoutException ste) {
                    break;
                }
            }
        } catch (IOException ioex) {
            // Problem reading from socket (broken connection)
            serverDispatcher.printMessage("Read failed");
        }

        // Communication is broken. Interrupt both listener and 
        // sender threads 
        serverDispatcher.deleteClient(client);
    }

//    public void run() {
//        BufferedReader in = null;
//        PrintWriter out = null;
//        String line = null;
////        try {
//
//        try {
//            in = new BufferedReader(new InputStreamReader(
//                    client.getConnection().getInputStream()));
//            out = new PrintWriter(client.getConnection().getOutputStream(),
//                    true);
//        } catch (IOException e) {
//            System.out.println("Opening streams failed");
//            System.exit(-1);
//        }
//
//        while (true) {
//            try {
//                line = in.readLine();
//                if (NEXT.equalsIgnoreCase(line)) {
//                    robot.right();
//                } else if (PREV.equalsIgnoreCase(line)) {
//                    robot.left();
//                } else {
//                    messagesTextArea.append(line + "\n");
//                }
//                //Send data back to client
//                out.println(line + "executed");
//            } catch (IOException e) {
//                System.out.println("Read failed");
//                System.exit(-1);
//            }
//        }
//
//
//            BufferedInputStream is = new BufferedInputStream(connection.getInputStream());
//            InputStreamReader isr = new InputStreamReader(is);
//            int character;
//            StringBuffer process = new StringBuffer();
//            while ((character = isr.read()) != 13) {
//                process.append((char) character);
//            }
//            System.out.println(process);
//            //need to wait 10 seconds to pretend that we're processing something
//            try {
//                Thread.sleep(10000);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            TimeStamp = new java.util.Date().toString();
//            String returnCode = "MultipleSocketServer repsonded at " + TimeStamp + (char) 13;
//            BufferedOutputStream os = new BufferedOutputStream(connection.getOutputStream());
//            OutputStreamWriter osw = new OutputStreamWriter(os, "US-ASCII");
//            osw.write(returnCode);
//            osw.flush();
//        } catch (Exception e) {
//            System.out.println(e);
//        } finally {
//            try {
//                connection.close();
//            } catch (IOException e) {
//            }
//        }
//    }
//
//    protected void finalize() {
////Objects created in run method are finalized when
////program terminates and thread exits
//        try {
//            client.getConnection().close();
//        } catch (IOException e) {
//            System.out.println("Could not close socket");
//            System.exit(-1);
//        }
//    }

}
