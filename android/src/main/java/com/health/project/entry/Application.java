package com.health.project.entry;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import com.health.Data.Notifications;
import com.health.Data.Procedures;
import com.health.objects.GetActiveCondtions;
import com.health.objects.GetActiveProcedures;
import com.health.objects.GetAvailibleProcedure;
import com.health.objects.GetCondtion;
import com.health.objects.GetNotifications;
import com.health.objects.GetPersonalData;
import com.health.objects.GetProcedure;
import com.health.objects.GetTime;
import com.health.objects.Strings;
import com.health.objects.Types;
import com.health.objects.setLang;

import org.jetbrains.annotations.NonNls;

import java.util.ArrayList;
import java.util.List;


public class Application extends android.app.Application {


    private static volatile boolean force_updae = false;
    private static volatile boolean force_update_lang = false;


    public static volatile DataChangedListener LastOneCalled;

    public static int lang;

    @NonNls
    public static Context context;


    public static volatile long Patient_ID = 0;
    public static volatile String Pass;
    public static volatile boolean let_pass = false;

    public static List<GetNotifications.Notification> Notis = new ArrayList<>();
    public static List<GetCondtion.Condtion> ActiveCondtions = new ArrayList<>();
    public static List<GetProcedure.Procedure> ActiveProcedures = new ArrayList<>();
    public static List<GetProcedure.Procedure> AttachedProc = new ArrayList<>();

    public static GetTime.Time currentTime;

    public static List<Notifications.Notification> Notifications = new ArrayList<>();
    public static List<Procedures.Procedure> Procedures = new ArrayList<>();


    public static GetPersonalData.PersonalData personalData;


    private static List<DataChangedListener> listeners = new ArrayList<>();
    private static List<DataChangedListener> oneTimeLis = new ArrayList<>();

    public static boolean is_initialized = false;

    public static boolean registerListener(DataChangedListener listener) {
        listeners.add(listener);
        return is_initialized;
    }

    public static boolean registerOneTime(DataChangedListener listener) {
        oneTimeLis.add(listener);
        return is_initialized;
    }

    public static boolean unRegisterListener(DataChangedListener listener) {
        return listeners.remove(listener);
    }


    public static volatile Handler main_handler;
    public static Thread th;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        listeners = new ArrayList<>();
        main_handler = new Handler();
        force_update_lang = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            lang = Application.context.getResources().getConfiguration().getLocales().get(0).toString().contains(Strings.EN) ? Types.Lang_English : Types.Lang_Arabic;
        else
            lang = Application.context.getResources().getConfiguration().locale.toString().contains(Strings.EN) ? Types.Lang_English : Types.Lang_Arabic;
        Strings.init();
        th = new Thread((new updater()));
        th.start();
    }

    private static class updater implements Runnable {

        @Override
        public void run() {


            while (true) {
                if (Application.Patient_ID == 0) {

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                } else {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                AsyncTask.SERIAL_EXECUTOR.execute(new Runnable() {
                    @Override
                    public void run() {


                        try {

                            final List<GetNotifications.Notification> Notis;
                            final List<Notifications.Notification> Notifications;
                            final List<GetCondtion.Condtion> ActiveCondtions;
                            final List<GetProcedure.Procedure> ActiveProcedures;
                            final List<GetProcedure.Procedure> AttachedProc;
                            final List<Procedures.Procedure> Procedures;
                            final GetPersonalData.PersonalData data;
                            GetTime.Time t = com.health.objects.GetTime.getTime();
                            if (t == null) return;
                            currentTime = t;

                            if (force_update_lang) {
                                force_update_lang = false;
                                setLang.setLang(lang, Application.Patient_ID);
                            }

                            data = GetPersonalData.GetPersonalData(Application.Patient_ID);
                            if (data == null) {
                                return;
                            }

                            if (Application.personalData == null || Application.personalData.last_time_modified != data.last_time_modified || Application.personalData.last_time_server_modified != data.last_time_server_modified) {
                                force_updae = true;
                            }

                            Application.personalData = data;
                            personalData.date_of_birth = Util.generateDateAr(personalData.date_of_birth);
                            personalData.phone = Util.convertStringToAr(personalData.phone);
                            personalData.insurance_path = Util.convertStringToAr(personalData.insurance_path);

                            if (Application.personalData == null || Application.personalData.last_time_modified != data.last_time_modified) {
                                force_updae = true;
                            }

                            if (force_updae) {
                                force_updae = false;

                                ActiveCondtions = GetActiveCondtions.GetActiveCondtions(Application.Patient_ID);
                                ActiveProcedures = GetActiveProcedures.getActiveProcedures(Application.Patient_ID);
                                AttachedProc = GetAvailibleProcedure.getAvailibleProcedures(true);
                                Procedures = com.health.Data.Procedures.getProcedures(ActiveProcedures, ActiveCondtions);

                                List<GetNotifications.Notification> Notis1;

                                Notis1 = GetNotifications.getNotifications(Application.Patient_ID);
                                int size = Notis1.size();
                                Notis = new ArrayList<>(size);
                                while (!Notis1.isEmpty()) {
                                    Notis.add(Notis1.remove(Notis1.size() - 1));
                                }


                                Notifications = com.health.Data.Notifications.getNotifications(Notis, ActiveCondtions, ActiveProcedures, context);


                                Log.e("Alaa", ActiveCondtions.size() + " condtions");

                                for (GetCondtion.Condtion c : ActiveCondtions) {
                                    AttachedProc.addAll(c.attached_procedures);
                                    c.Date_Act = Util.generateDateAr(c.date_activation);
                                    if (c.state == Types.State_Cond_History) {
                                        c.Date_deactive = Util.generateDateAr(c.date_deactivation);
                                    } else {

                                        c.Remaining_days = Util.convertStringToAr(c.remaining_days + Strings.DAYS);

                                    }

                                }


                                if (Application.is_initialized) {

                                    main_handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Application.Notis = Notis;
                                            Application.ActiveCondtions = ActiveCondtions;
                                            Application.ActiveProcedures = ActiveProcedures;
                                            Application.personalData = data;
                                            Application.personalData.lang = Application.lang;

                                            Application.AttachedProc = AttachedProc;
                                            Application.Notifications = Notifications;
                                            Application.Procedures = Procedures;


                                            for (int i = 0; i < Application.listeners.size(); i++) {
                                                Application.listeners.get(i).onNotificationsChanged();
                                                Application.listeners.get(i).onAttachedProcedureChanged();
                                                Application.listeners.get(i).onCondtionsChanged();
                                                Application.listeners.get(i).onPersonalDataChanged();
                                                Application.listeners.get(i).onProceduresChanged();
                                                Application.listeners.get(i).onBillsChanged();
                                            }

                                            for (int i = 0; i < Application.oneTimeLis.size(); i++) {
                                                Application.oneTimeLis.get(i).onNotificationsChanged();
                                                Application.oneTimeLis.get(i).onAttachedProcedureChanged();
                                                Application.oneTimeLis.get(i).onCondtionsChanged();
                                                Application.oneTimeLis.get(i).onPersonalDataChanged();
                                                Application.oneTimeLis.get(i).onProceduresChanged();
                                                Application.oneTimeLis.get(i).onBillsChanged();
                                            }
                                            Application.oneTimeLis.clear();
                                            if (LastOneCalled != null) {
                                                LastOneCalled.onNotificationsChanged();
                                                LastOneCalled.onAttachedProcedureChanged();
                                                LastOneCalled.onCondtionsChanged();
                                                LastOneCalled.onPersonalDataChanged();
                                                LastOneCalled.onProceduresChanged();
                                                LastOneCalled.onBillsChanged();

                                            }
                                        }
                                    });
                                } else {

                                    Application.is_initialized = true;

                                    main_handler.post(new Runnable() {
                                        @Override
                                        public void run() {

                                            Application.Notis = Notis;
                                            Application.ActiveCondtions = ActiveCondtions;
                                            Application.ActiveProcedures = ActiveProcedures;
                                            Application.personalData = data;
                                            Application.personalData.lang = Application.lang;
                                            Application.AttachedProc = AttachedProc;
                                            Application.Notifications = Notifications;
                                            Application.Procedures = Procedures;


                                            for (int i = 0; i < Application.listeners.size(); i++) {
                                                Application.listeners.get(i).onNotificationInit();
                                                Application.listeners.get(i).onAttachedProcedureInit();
                                                Application.listeners.get(i).onCondtionsInit();
                                                Application.listeners.get(i).onPersonalDataInit();
                                                Application.listeners.get(i).onProceduresInit();
                                                Application.listeners.get(i).onBillsInit();
                                            }

                                            for (int i = 0; i < Application.oneTimeLis.size(); i++) {
                                                Application.oneTimeLis.get(i).onNotificationInit();
                                                Application.oneTimeLis.get(i).onAttachedProcedureInit();
                                                Application.oneTimeLis.get(i).onCondtionsInit();
                                                Application.oneTimeLis.get(i).onPersonalDataInit();
                                                Application.oneTimeLis.get(i).onProceduresInit();
                                                Application.oneTimeLis.get(i).onBillsInit();
                                            }
                                            Application.oneTimeLis.clear();
                                            if (LastOneCalled != null) {
                                                LastOneCalled.onNotificationInit();
                                                LastOneCalled.onAttachedProcedureInit();
                                                LastOneCalled.onCondtionsInit();
                                                LastOneCalled.onPersonalDataInit();
                                                LastOneCalled.onProceduresInit();
                                                LastOneCalled.onBillsInit();

                                            }

                                        }
                                    });


                                }

                            }


                        } catch (Exception e) {
                            Log.e("Alaa", Log.getStackTraceString(e));

                        }

                    }
                });


            }
        }
    }
}



