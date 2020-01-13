package com.health.schedule.main;

import com.health.objects.GetAvailibleRooms;
import com.health.objects.GetAvailibleServices;
import com.health.schedule.layouts.mainLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Inspiron
 */
public class Main {

    public static List<GetAvailibleRooms.Room> rooms;
    public static List<GetAvailibleServices.Service> services;

    static {
        rooms = new ArrayList<>();
        services = new ArrayList<>();
    }

    public static void init() {

        rooms = GetAvailibleRooms.getAvailibleRooms();
        services = GetAvailibleServices.getAvailibleServices();
    }

    public static void main(String[] args) {
        init();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        JFrame frame = Util.showPanel(new mainLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}
