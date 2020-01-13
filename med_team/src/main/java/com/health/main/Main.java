/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.main;

import com.health.project.medteam.Globals;
import com.health.widgets.Signin;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.lang.ref.WeakReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Inspiron
 */
public class Main {

    public static JFrame jframe;

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        final JFrame frame = new JFrame("CheckBox Tree");
        Globals.signin = new WeakReference<>(frame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new Signin(), BorderLayout.CENTER);
        Dimension d = new Dimension(1200, 800);
        frame.setSize(d);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.pack();

    }

}
