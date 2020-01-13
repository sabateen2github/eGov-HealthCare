package com.health.Data;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.health.objects.GetCondtion;
import com.health.objects.GetNotifications;
import com.health.objects.GetProcedure;
import com.health.objects.Strings;
import com.health.objects.Types;
import com.health.project.entry.Application;
import com.health.project.entry.R;
import com.health.project.entry.Util;
import com.health.project.entry.onNotificationUpdated;

import org.jetbrains.annotations.NonNls;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Notifications {


    public static void FullUpdate(final long noti_id, final onNotificationUpdated run) {
        Iterator<Notifications.Notification> notis = Application.Notifications.iterator();

        Notifications.Notification noti = null;
        int pos = -1;

        while (notis.hasNext()) {

            pos++;
            noti = notis.next();
            if (noti.noti.notification_id == noti_id) {
                break;
            } else {

                noti = null;
            }
        }

        GetCondtion.Condtion cond_temp = null;
        GetCondtion.Activity activ_temp = null;
        GetCondtion.MedicationRoutine med_temp = null;
        GetProcedure.Procedure proc_temp = null;


        if (noti != null) {


            if (noti.noti.condtion_id != 0) {

                GetCondtion.Condtion condtion = GetCondtion.getCondtion(Application.Patient_ID, noti.noti.condtion_id);
                cond_temp = condtion;


                condtion.Date_Act = Util.generateDateAr(condtion.date_activation);
                if (condtion.state == Types.State_Cond_History) {
                    condtion.Date_deactive = Util.generateDateAr(condtion.date_deactivation);
                } else {
                    condtion.Remaining_days = Util.convertStringToAr(condtion.remaining_days + Strings.DAYS);
                }

                if (noti.noti.medication_id != 0 && cond_temp != null) {
                    Iterator<GetCondtion.MedicationRoutine> meds = condtion.medications_routine.iterator();

                    GetCondtion.MedicationRoutine med;

                    while (meds.hasNext()) {

                        med = meds.next();
                        if (med.medication.medication_id == noti.noti.medication_id) {
                            med_temp = med;
                            break;
                        }
                    }


                }


                if (noti.noti.activity_id != 0 && noti.LocalCondtion != null) {
                    Iterator<GetCondtion.Activity> acts = condtion.activities.iterator();

                    GetCondtion.Activity act;

                    while (acts.hasNext()) {

                        act = acts.next();
                        if (act.activity_id == noti.noti.activity_id) {
                            activ_temp = act;
                            break;
                        }
                    }

                }

            }


            if (noti.noti.procedure_id != 0) {

                GetProcedure.Procedure proc = GetProcedure.getProcedure(Application.Patient_ID, noti.noti.procedure_id);
                if (proc != null) {
                    proc_temp = proc;
                    proc.date_activation = Util.generateDateAr(proc.date_activation);
                    if (proc.state == Types.State_Proc_History) {
                        proc.date_deactivation = Util.generateDateAr(proc.date_deactivation);
                    }
                }
            }

            final GetCondtion.Condtion cond_f = cond_temp;
            final GetCondtion.Activity activ_f = activ_temp;
            final GetCondtion.MedicationRoutine med_f = med_temp;
            final GetProcedure.Procedure proc_f = proc_temp;
            final Notifications.Notification noti_f = noti;
            final int pos_f = pos;

            Application.main_handler.post(new Runnable() {
                @Override
                public void run() {
                    noti_f.LocalCondtion = cond_f;
                    noti_f.LocalMedication = med_f;
                    noti_f.LocalProc = proc_f;
                    noti_f.LocalActivity = activ_f;
                    noti_f.is_updating = false;
                    try {
                        run.onUpdated(pos_f, noti_id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }


    public static List<Notification> getNotifications(List<GetNotifications.Notification> notis, List<GetCondtion.Condtion> ActiveCondtions,
                                                      List<GetProcedure.Procedure> ActiveProcedures, Context context) {
        Assests asset = new Assests();
        asset.Activity = context.getDrawable(R.drawable.activity_noti);
        asset.Condtion = context.getDrawable(R.drawable.condtion_noti);
        asset.Medication = context.getDrawable(R.drawable.medication_noti);
        asset.Visit = context.getDrawable(R.drawable.visit_noti);


        List<Notification> nots = new ArrayList<>();
        for (int i = 0; i < notis.size(); i++) {

            Notification not = new Notification(notis.get(i), asset);

            if (not.noti.procedure_id != 0) {

                Iterator<GetProcedure.Procedure> proc = ActiveProcedures.iterator();
                while (proc.hasNext()) {
                    GetProcedure.Procedure p = proc.next();
                    if (p.proc_id == not.noti.procedure_id) {
                        not.LocalProc = p;
                        break;
                    }
                }
            }

            if (not.noti.condtion_id != 0) {
                Iterator<GetCondtion.Condtion> cond = ActiveCondtions.iterator();
                while (cond.hasNext()) {
                    GetCondtion.Condtion p = cond.next();
                    if (p.condtion_id == not.noti.condtion_id) {
                        not.LocalCondtion = p;

                        break;
                    }
                }
                if (not.LocalCondtion != null) {
                    if (not.noti.activity_id != 0) {
                        Iterator<GetCondtion.Activity> act = not.LocalCondtion.activities.iterator();

                        while (act.hasNext()) {

                            GetCondtion.Activity a = act.next();

                            if (not.noti.activity_id == a.activity_id) {
                                not.LocalActivity = a;
                                break;
                            }
                        }
                    }
                    if (not.noti.medication_id != 0) {

                        Iterator<GetCondtion.MedicationRoutine> med = not.LocalCondtion.medications_routine.iterator();

                        while (med.hasNext()) {

                            GetCondtion.MedicationRoutine a = med.next();

                            if (not.noti.medication_id == a.medication.medication_id) {
                                not.LocalMedication = a;
                                break;
                            }
                        }
                    }
                }

            }

            nots.add(not);
        }

        return nots;

    }

    public static class Assests {

        public Drawable Activity;
        public Drawable Visit;
        public Drawable Condtion;
        public Drawable Medication;

    }

    public static class Period {

        public int pyear;
        public int pmonth;
        public int pday;
        public int phour;
        public int pminute;

        public int amount;

        public boolean isPast;

        public Period(String date) {

            String[] h = date.split("-");
            int year = Integer.parseInt(h[0]);
            int month = Integer.parseInt(h[1]);
            int day = Integer.parseInt(h[2]);
            int hour = Integer.parseInt(h[3]);
            int minute = Integer.parseInt(h[4]);
            DateTime dateo = new DateTime(year, month, day, hour, minute);

            year = Application.currentTime.year;
            month = Application.currentTime.month;
            day = Application.currentTime.days;
            hour = Application.currentTime.hour;
            minute = Application.currentTime.min;
            DateTime ctime = new DateTime(year, month, day, hour, minute);

            org.joda.time.Period period;

            if (dateo.isBefore(ctime)) {
                period = new Interval(dateo, ctime).toPeriod();
                isPast = true;
            } else {
                period = new Interval(ctime, dateo).toPeriod();
                isPast = false;
            }


            pyear = period.getYears();
            pmonth = period.getMonths();
            pday = period.getDays();
            phour = period.getHours();
            pminute = period.getMinutes();

        }

        public Period(String date, String date2) {

            String[] h = date.split("-");
            int year = Integer.parseInt(h[0]);
            int month = Integer.parseInt(h[1]);
            int day = Integer.parseInt(h[2]);
            int hour = Integer.parseInt(h[3]);
            int minute = Integer.parseInt(h[4]);
            DateTime dateo = new DateTime(year, month, day, hour, minute);

            h = date2.split("-");
            year = Integer.parseInt(h[0]);
            month = Integer.parseInt(h[1]);
            day = Integer.parseInt(h[2]);
            hour = Integer.parseInt(h[3]);
            minute = Integer.parseInt(h[4]);
            DateTime ctime = new DateTime(year, month, day, hour, minute);

            org.joda.time.Period period;

            if (dateo.isBefore(ctime)) {
                period = new Interval(dateo, ctime).toPeriod();
                isPast = true;
            } else {
                period = new Interval(ctime, dateo).toPeriod();
                isPast = false;
            }

            pyear = period.getYears();
            pmonth = period.getMonths();
            pday = period.getDays();
            phour = period.getHours();
            pminute = period.getMinutes();

        }

        public int getPeriodInDays() {
            return pyear * 365 + pmonth * 30 + pday;
        }

        public int getPeriodInMinutes() {
            return (pyear * 365 + pmonth * 30 + pday) * 24 * 60 + phour * 60 + pminute;
        }

        public boolean moreThanADay() {
            return pday > 0 || pmonth > 0 || pyear > 0;
        }

        public boolean isOlder(Period p) {
            if (isPast && p.isPast) {
                if (pyear > p.pyear) {

                    return true;
                }
                if (pmonth > p.pmonth) {

                    return true;
                }
                if (pday > p.pday) {

                    return true;
                }
                if (phour > p.phour) {

                    return true;
                }
                return false;

            } else if (!isPast && p.isPast) {
                return false;

            } else if (isPast && !isPast) {
                return true;
            } else {
                if (pyear > p.pyear) {

                    return false;
                }
                if (pmonth > p.pmonth) {

                    return false;
                }
                if (pday > p.pday) {

                    return false;
                }
                if (phour > p.phour) {

                    return false;
                }
                return true;
            }

        }

    }

    public static class Notification {

        @NonNls
        public String Title;
        public String Date;
        public Drawable icon;
        public GetNotifications.Notification noti;
        public GetProcedure.Procedure LocalProc;
        public GetCondtion.Condtion LocalCondtion;
        public GetCondtion.Activity LocalActivity;
        public GetCondtion.MedicationRoutine LocalMedication;

        public transient boolean is_updating = false;


        public Notification(GetNotifications.Notification n, Assests asset) {
            noti = n;

            if (Application.personalData.lang == Types.Lang_English) {
                Date = getDateTitle(noti.date);
            }

            switch (n.type) {
                case GetNotifications.Notification_Activity:
                    doActivity(asset);
                    break;
                case GetNotifications.Notification_Medication:
                    doMedication(asset);
                    break;
                case GetNotifications.Notification_Msg:
                    doMsg(asset);
                    break;
                case GetNotifications.Notification_Visit_Appointment_Change:
                    doAppointment(asset);
                    break;
                case GetNotifications.Notification_Visit_Created:
                    doCreated(asset);
                    break;
                case GetNotifications.Notification_Visit_Done:
                    doDone(asset);
                    break;
                case GetNotifications.Notification_Visit_Failed:
                    doFailed(asset);
                    break;
                case GetNotifications.Notification_Condtion_Added:
                    doAdded(asset);
                    break;
                case GetNotifications.Notification_Condtion_Removed:
                    doRemoved(asset);
                    break;
                case GetNotifications.Notification_Appointment_Near:
                    doNear(asset);
                    break;

            }

        }


        public static String getDateTitle(String date) {
            Period p = new Period(date);
            @NonNls String title;

            if (p.pyear > 0) {

                title = p.pyear + Strings.A_Year_Ago;


            } else if (p.pmonth > 0) {
                title = p.pmonth + Strings.A_Month_Ago;


            } else if (p.pday > 0) {
                title = p.pday + Strings.A_Days_Ago;

            } else if (p.phour > 0) {
                title = p.phour + Strings.A_Hours_Ago;
            } else {
                title = p.pminute + Strings.A_Minutes_Ago;

            }

            return Util.convertStringToAr(title);
        }

        void doActivity(Assests asset) {

            icon = asset.Activity;

            if (Application.personalData.lang == Types.Lang_English) {

                Title = Strings.Please_Do_Act;
            }

        }

        void doMsg(Assests asset) {

            icon = asset.Visit;

            if (Application.personalData.lang == Types.Lang_English) {


                Title = Strings.You_recv_Msg;
            }
        }

        void doMedication(Assests asset) {
            icon = asset.Medication;

            if (Application.personalData.lang == Types.Lang_English) {


                Title = Strings.please_take_medicine;
            }
        }

        void doAppointment(Assests asset) {
            icon = asset.Visit;

            if (Application.personalData.lang == Types.Lang_English) {


                Title = Strings.YOUR_APPOINTMENT_HAD_BEEN_CANCELLED_PLEASE_BOOK_A_NEW_ONE;
            }

        }

        void doCreated(Assests asset) {
            icon = asset.Visit;

            if (Application.personalData.lang == Types.Lang_English) {


                Title = Strings.Visit_Created;
            }
        }

        void doDone(Assests asset) {
            icon = asset.Visit;

            if (Application.personalData.lang == Types.Lang_English) {


                Title = Strings.Suc_Visit;
            }

        }

        void doFailed(Assests asset) {
            icon = asset.Visit;

            if (Application.personalData.lang == Types.Lang_English) {


                Title = Strings.cancel_visit;
            }

        }


        void doAdded(Assests asset) {
            icon = asset.Condtion;
            if (Application.personalData.lang == Types.Lang_English) {

                Title = Strings.MEd_Cond_Created;
            }
        }

        void doRemoved(Assests asset) {
            icon = asset.Condtion;
            if (Application.personalData.lang == Types.Lang_English) {
                Title = Strings.Medical_Condtion_Deactive;
            }
        }

        void doNear(Assests asset) {
            icon = asset.Visit;

            if (Application.personalData.lang == Types.Lang_English) {

                Title = Strings.Prepare_Next_app;
            }

        }
    }


}
