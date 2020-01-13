/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.installer.fund.main;

import com.health.installer.fund.layouts.main;
import com.health.installer.fund.utils.Globals;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 *
 * @author Inspiron
 */
public class Main {

    public static void main(String[] args) throws Exception {

        Globals.init();
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        JFrame frame = new JFrame();
        main m = new main();
        frame.setContentPane(m);
        frame.setResizable(true);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

}
