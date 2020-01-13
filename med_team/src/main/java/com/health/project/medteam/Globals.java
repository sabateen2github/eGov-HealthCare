/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.project.medteam;

import com.health.objects.GetActiveCondtions;
import com.health.objects.GetActiveFeedbackList;
import com.health.objects.GetActiveProcedures;
import com.health.objects.GetAvailibleCondtions;
import com.health.objects.GetAvailibleMedications;
import com.health.objects.GetAvailibleProcedure;
import com.health.objects.GetAvailibleRooms;
import com.health.objects.GetAvailibleRooms.Room;
import com.health.objects.GetAvailibleServices;
import com.health.objects.GetAvailibleServices.Service;
import com.health.objects.GetCondtion;
import com.health.objects.GetCondtion.Medication;
import com.health.objects.GetPersonalData;
import com.health.objects.GetProcedure;
import com.health.widgets.Msg;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import javax.swing.JFrame;

/**
 *
 * @author Inspiron
 */
public class Globals {

    public static java.util.concurrent.Executor executor;

    public static long med_id;
    public static String pass;

    public static List<Medication> medications = new ArrayList<>();
    public static List<Service> services = new ArrayList<>();
    public static List<Room> rooms = new ArrayList<>();
    public static List<GetCondtion.Condtion> templatesCondtions;
    public static List<GetProcedure.Procedure> templatesProcedures;
    public static List<GetActiveFeedbackList.Patient> activeFeedBacks;

    public static GetPersonalData.PersonalData patient_current;
    public static long patien_id;
    public static List<GetCondtion.Condtion> activeCondtions;
    public static GetProcedure.Procedure current_procedure;
    public static List<GetProcedure.Procedure> activeProc;

    public static WeakReference<Updater> ActiveCondtions;

    public static WeakReference<JFrame> updateProc;
    public static WeakReference<JFrame> startProc;
    public static WeakReference<JFrame> startCondtion;
    public static WeakReference<JFrame> signin;
    public static WeakReference<JFrame> Msg;
    public static WeakReference<JFrame> SendMsg;
    public static WeakReference<JFrame> PatientProfile;

    public static interface Updater {

        public void update();

    }

    public static boolean init() {

        executor = java.util.concurrent.Executors.newCachedThreadPool();
        templatesCondtions = GetAvailibleCondtions.getAvailibleCondtions();
        templatesProcedures = GetAvailibleProcedure.getAvailibleProcedures(false);
        medications = GetAvailibleMedications.getAvailibleMedications();
        rooms = GetAvailibleRooms.getAvailibleRooms();
        services = GetAvailibleServices.getAvailibleServices();
        activeFeedBacks = GetActiveFeedbackList.getActiveFeedBack(med_id);

        return true;
    }

    public static boolean setPatient(long id, long procedure_id) {
        patien_id = id;
        patient_current = GetPersonalData.GetPersonalData(id);
        activeCondtions = GetActiveCondtions.GetActiveCondtions(id);

        if (procedure_id != 0) {
            activeProc = GetActiveProcedures.getActiveProcedures(id);
            for (GetProcedure.Procedure p : activeProc) {
                if (p.proc_id == procedure_id) {

                    current_procedure = p;
                    break;
                }
            }
        } else {
            current_procedure = null;
        }
        return true;
    }

    public static void showMsg(String msg) {

        final JFrame frame = new JFrame("Msg");
        Globals.Msg = new WeakReference<>(frame);

        Msg container = new Msg();
        container.setData(msg);

        frame.getContentPane().add(container, BorderLayout.CENTER);

        Dimension d = new Dimension(1200, 800);
        frame.setSize(d);

        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.pack();
    }

    public static void showSendMsg(String msg) {

        final JFrame frame = new JFrame("Send Message");
        Globals.SendMsg = new WeakReference<>(frame);
        com.health.widgets.SendMsg container = new com.health.widgets.SendMsg();
        frame.getContentPane().add(container, BorderLayout.CENTER);
        Dimension d = new Dimension(1200, 800);
        frame.setSize(d);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.pack();
    }

    public static void openPatientProfile() {

        final JFrame frame = new JFrame("Open Pateint Profile");
        Globals.PatientProfile = new WeakReference<>(frame);
        com.health.widgets.OpenHealthRecord container = new com.health.widgets.OpenHealthRecord();
        frame.getContentPane().add(container, BorderLayout.CENTER);
        Dimension d = new Dimension(1200, 800);
        frame.setSize(d);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.pack();
    }

    public static Timer timer = new Timer();

    public static long generateRequestId(GetProcedure.Procedure proc) {

        ArrayList<Long> ids = new ArrayList<>();

        for (GetProcedure.FeedBack f : proc.feedbacks) {

            ids.add(f.req_id);
        }

        for (GetProcedure.SmartFeedBack f : proc.smart_feedbacks) {
            ids.add(f.req_id);

        }

        for (GetProcedure.ServicesGroup f : proc.services) {
            ids.add(f.req_id);

        }

        long id = (long) (Math.random() * Long.MAX_VALUE);

        while (ids.contains(id)) {
            id = (long) (Math.random() * Long.MAX_VALUE);
        }

        return id;
    }

    public static long generateActivityId(GetCondtion.Condtion proc) {

        ArrayList<Long> ids = new ArrayList<>();

        for (GetCondtion.Activity f : proc.activities) {

            ids.add(f.activity_id);
        }

        long id = (long) (Math.random() * Long.MAX_VALUE);

        while (ids.contains(id)) {
            id = (long) (Math.random() * Long.MAX_VALUE);
        }

        return id;
    }

    private static List<WeakReference<Updater>> updaters = new ArrayList<>();

    public static void register(Updater updater) {

        for (int i = 0; i < updaters.size(); i++) {
            WeakReference<Updater> d = updaters.get(i);
            if (d.get() == null) {
                updaters.remove(i);
                i--;
            }
        }
        updaters.add(new WeakReference<>(updater));
    }

    public static void update() {

        for (int i = 0; i < updaters.size(); i++) {
            WeakReference<Updater> d = updaters.get(i);
            Updater up = d.get();
            if (up == null) {
                updaters.remove(i);
                i--;
            } else {
                up.update();
            }
        }

    }

}
