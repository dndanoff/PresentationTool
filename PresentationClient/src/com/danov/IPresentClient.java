/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.danov;

import java.io.Serializable;

/**
 *
 * @author x
 */
public interface IPresentClient extends Serializable {

    public boolean connect();

    public void sendPointCoordinates(int x, int y, int width, int heigth);

    public void clearPointer();

    public void sendPointerMessage();

    public boolean sendNextMessage();

    public boolean sendPrevMessage();
    
    public void reconnect();

    public void disconnect();
}
