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

    public static final String NEXT = "next";
    public static final String PREVIOUS = "prev";
    public static final String PREF_PREFIX = "pref:";
    public static final String POINT_PREFIX = "point:";
    public static final String START_POINTER = "pointer";
    public static final String CLEAR_POINTER = "clear";

    public boolean connect();

    public void sendPointCoordinates(int x, int y, int width, int heigth);

    public void sendPreferenceMessage(int dotSize, int dotColor);

    public void clearPointer();

    public void sendPointerMessage();

    public boolean sendNextMessage();

    public boolean sendPrevMessage();

    public void reconnect();

    public void disconnect();
}
