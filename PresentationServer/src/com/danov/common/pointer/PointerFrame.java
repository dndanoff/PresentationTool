/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.danov.common.pointer;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

/**
 *
 * @author Denis
 */
public class PointerFrame extends JFrame {

    private ImagePanel imagePanel;
    private boolean dynamicContent;

    public PointerFrame(boolean dynamicContent) {
        this.dynamicContent = dynamicContent;
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);

        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        Action escapeAction = new EscapeAction();
        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
        this.getRootPane().getActionMap().put("ESCAPE", escapeAction);

        if (PointerUtils.uniformTranslucencySupported() && dynamicContent) {
            this.setOpacity(0.30f);
            imagePanel = new ImagePanel();
        } else {
            BufferedImage image = PointerUtils.captureScreen();
            imagePanel = new ImagePanel(image);
        }
        this.getContentPane().add(imagePanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(false);
    }

    public void getNewScreenCondition() {
        if (!PointerUtils.uniformTranslucencySupported() || !dynamicContent) {
            BufferedImage image = PointerUtils.captureScreen();
            imagePanel.setImg(image);
        }
        imagePanel.removePointCoordinates();
        imagePanel.repaint();
    }

    public void paintDot(int x, int y, int width, int height) {
        Dimension dimenstion = PointerUtils.getScreenDimenstions();
        int realX = x*dimenstion.width/width;
        int realY = y*dimenstion.height/height;
        
        imagePanel.setPointX(realX);
        imagePanel.setPointY(realY);
        imagePanel.repaint();
    }
    
    public void changeDotSize(int dotSize){
        imagePanel.setDotSize(dotSize);
    }
    
    public void clearPointer(){
        imagePanel.removePointCoordinates();
        imagePanel.repaint();
    }

    public ImagePanel getImagepanel() {
        return imagePanel;
    }

    public void setImagepanel(ImagePanel imagepanel) {
        this.imagePanel = imagepanel;
    }

    private class EscapeAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            PointerFrame.this.dispose();
        }

    }
}
