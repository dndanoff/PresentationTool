package com.danov.common.robot;


import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author x
 */
public class InputRobot {

    private Robot robot;

    public InputRobot() throws AWTException {
        robot = new Robot();
    }

    public void leftClick() {
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.delay(200);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.delay(200);
    }
    
    public void rightClick() {
        robot.mousePress(InputEvent.BUTTON3_MASK);
        robot.delay(200);
        robot.mouseRelease(InputEvent.BUTTON3_MASK);
        robot.delay(200);
    }
    
    public void left(){
        robot.keyPress(KeyEvent.VK_LEFT);
        robot.delay(100);
        robot.keyRelease(KeyEvent.VK_LEFT);
    }
    
    public void right(){
        robot.keyPress(KeyEvent.VK_RIGHT);
        robot.delay(100);
        robot.keyRelease(KeyEvent.VK_RIGHT);
    }

    private void type(int i) {
        robot.delay(40);
        robot.keyPress(i);
        robot.keyRelease(i);
    }

    public void type(String s) {
        byte[] bytes = s.getBytes();
        for (byte b : bytes) {
            int code = b;
            // keycode only handles [A-Z] (which is ASCII decimal [65-90])
            if (code > 96 && code < 123) {
                code = code - 32;
            }
            robot.delay(40);
            robot.keyPress(code);
            robot.keyRelease(code);
        }
    }

}
