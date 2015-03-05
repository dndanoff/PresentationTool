/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.danov.pointer;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

/**
 *
 * @author x
 */
public class PointerUtils {

    public static BufferedImage captureScreen() {
        // Determine current screen size
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension oScreenSize = toolkit.getScreenSize();
        Rectangle oScreen = new Rectangle(oScreenSize);

        // Create screen shot
        try {
            Robot robot = new Robot();
            BufferedImage oImage = robot.createScreenCapture(oScreen);
            return oImage;
        } catch (AWTException ex) {
            Logger.getLogger(PointerUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static Dimension getScreenDimenstions() {
        // Determine current screen size
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension oScreenSize = toolkit.getScreenSize();
        return oScreenSize;
    }

    public static JFrame createScreenFrame() {
        final JFrame frame = new JFrame();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);

        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        Action escapeAction = new AbstractAction() {
            // close the frame when the user presses escape
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        };
        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
        frame.getRootPane().getActionMap().put("ESCAPE", escapeAction);

        if (uniformTranslucencySupported()) {
            frame.setOpacity(0.30f);
            ImagePanel panel = new ImagePanel();
            frame.getContentPane().add(panel);
        } else {
            BufferedImage image = captureScreen();
            ImagePanel panel = new ImagePanel(image);
            frame.getContentPane().add(panel);

        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        return frame;
    }

    public static boolean uniformTranslucencySupported() {
        // Determine what the default GraphicsDevice can support.
        return getGraphicDevice().isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT);
    }

    public static boolean perpixelTranslucencySupported() {
        // Determine what the default GraphicsDevice can support.
        return getGraphicDevice().isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSLUCENT);
    }

    public static boolean shapedWindowSupported() {
        // Determine what the default GraphicsDevice can support.
        return getGraphicDevice().isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSPARENT);
    }

    private static GraphicsDevice getGraphicDevice() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        return ge.getDefaultScreenDevice();
    }

    public static void createAndStartScreenFrame() {
        JFrame frame = createScreenFrame();
        frame.setVisible(true);
    }

    public static Dimension getScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }
}
