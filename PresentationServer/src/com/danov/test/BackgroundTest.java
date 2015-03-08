package com.danov.test;

import com.danov.common.pointer.PointerFrame;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author x
 */
public class BackgroundTest {

    public static void main(String[] args) throws InterruptedException {
        PointerFrame frame = new PointerFrame(true);
        frame.setVisible(true);
        
        Thread.sleep(1000);
        frame.setVisible(false);
//        frame.paintDot(200, 200);
        Thread.sleep(1000);
        frame.setVisible(true);
//        frame.paintDot(300, 300);
        Thread.sleep(1000);
        frame.setVisible(false);
//        frame.paintDot(400, 400);
    }
}
