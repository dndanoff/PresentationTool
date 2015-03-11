/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.danov.common.pointer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author x
 */
public class ImagePanel extends JPanel {

    private Image img;
    private int pointX;
    private int pointY;
    
    private int dotSize;
    private int dotColor;

    public ImagePanel(String img) {
        this(new ImageIcon(img).getImage(), -1, -1);
    }

    public ImagePanel(Image img) {
        this(img, -1, -1);
    }

    public ImagePanel() {
        this(null, -1, -1);
    }

    public ImagePanel(Image img, int x, int y) {
        this.img = img;
        if (img != null) {
            Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
            setPreferredSize(size);
            setMinimumSize(size);
            setMaximumSize(size);
            setSize(size);
        }
        setLayout(null);
        this.pointX = x;
        this.pointY = y;
		this.dotSize = 50;
        this.dotColor = 16711680;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Color color = new Color(dotColor);
        if (img != null) {
            g.drawImage(img, 0, 0, null);
        }
        if (pointX != -1 && pointY != -1) {
            g.setColor(color);
            g.fillOval(pointX, pointY, dotSize, dotSize);
        }
    }
    
    public void removePointCoordinates(){
        setPointX(-1);
        setPointY(-1);
    }
    
    public int getDotSize() {
        return dotSize;
    }

    public void setDotSize(int dotSize) {
        this.dotSize = dotSize;
    }

    public int getDotColor() {
        return dotColor;
    }

    public void setDotColor(int dotColor) {
        this.dotColor = dotColor;
    }

    public Image getImg() {
        return img;
    }

    public void setImg(Image img) {
        this.img = img;
    }

    public int getPointX() {
        return pointX;
    }

    public void setPointX(int pointX) {
        this.pointX = pointX;
    }

    public int getPointY() {
        return pointY;
    }

    public void setPointY(int pointY) {
        this.pointY = pointY;
    }

}
