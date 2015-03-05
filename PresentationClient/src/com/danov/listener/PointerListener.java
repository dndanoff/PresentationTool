/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.danov.listener;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import com.danov.IPresentClient;

/**
 *
 * @author x
 */
public class PointerListener implements OnTouchListener {

    private final IPresentClient client;

    public PointerListener(IPresentClient client) {
        this.client = client;
    }

    public boolean onTouch(View view, MotionEvent event) {
        int widht = view.getMeasuredWidth();
        int height = view.getMeasuredHeight();
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                client.sendPointCoordinates(x, y,widht,height);
                break;
            case MotionEvent.ACTION_MOVE:
                client.sendPointCoordinates(x, y,widht,height);
                break;
            case MotionEvent.ACTION_UP:
                client.clearPointer();
                break;
        }

        return true;
    }

    public IPresentClient getClient() {
        return client;
    }

}
