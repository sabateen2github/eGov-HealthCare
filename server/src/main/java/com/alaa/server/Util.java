/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alaa.server;

import com.health.objects.GetAvailibleInsurances;
import com.health.objects.GetAvailibleRooms;
import com.health.objects.GetAvailibleServices;
import com.health.objects.GetCondtion;
import com.health.objects.GetProcedure;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.Minutes;

/**
 *
 * @author Inspiron
 */
public class Util {

    public static long generateID(List<Long> ids) {

        long id = (long) (Math.random() * Long.MAX_VALUE);

        while (ids.contains(id)) {
            id = (long) (Math.random() * Long.MAX_VALUE);
        }

        return id;
    }

    public static Object getObj(String mode, Object obj) {

        try {
            if (obj instanceof GetAvailibleInsurances.Insurance) {
                GetAvailibleInsurances.Insurance in = (GetAvailibleInsurances.Insurance) obj;

                RandomAccessFile file = getFile(mode, PathsMine.InstalledInsurances, "" + in.insurance_id);
                int size = file.readInt();
                byte[] date = new byte[size];
                file.read(date);
                String g = new String(date);
                return server.gson.fromJson(g, GetAvailibleInsurances.Insurance.class);
            }

            if (obj instanceof GetCondtion.Medication) {

                GetCondtion.Medication med = (GetCondtion.Medication) obj;
                RandomAccessFile file = getFile(mode, PathsMine.InstalledMedications, "" + med.medication_id);
                int size = file.readInt();
                byte[] date = new byte[size];
                file.read(date);
                String g = new String(date);
                return server.gson.fromJson(g, GetCondtion.Medication.class);
            }

            if (obj instanceof GetAvailibleRooms.Room) {

                GetAvailibleRooms.Room room = (GetAvailibleRooms.Room) obj;
                RandomAccessFile file = getFile(mode, PathsMine.InstalledRooms, "" + room.room_id);
                int size = file.readInt();
                byte[] date = new byte[size];
                file.read(date);
                String g = new String(date);
                return server.gson.fromJson(g, GetAvailibleRooms.Room.class);
            }
            if (obj instanceof GetAvailibleServices.Service) {

                GetAvailibleServices.Service service = (GetAvailibleServices.Service) obj;
                RandomAccessFile file = getFile(mode, PathsMine.InstalledServicePath, "" + service.service_id);
                int size = file.readInt();
                byte[] data = new byte[size];
                file.read(data);
                String g = new String(data);
                return server.gson.fromJson(g, GetAvailibleServices.Service.class);
            }

        } catch (Exception e) {
        }

        return null;
    }

    public static RandomAccessFile getFile(String mode, String... paths) {

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < paths.length; i++) {
            if (paths[i].startsWith("/")) {
                paths[i] = paths[i].substring(1);
            }
            if (i != paths.length - 1 && !paths[i].endsWith("/")) {
                paths[i] = paths[i] + "/";
            }
            builder.append(paths[i]);
        }
        String path = builder.toString();
        File f = new File(path);
        if (!f.exists()) {
            try {
                if (!f.getParentFile().exists()) {
                    f.getParentFile().mkdirs();
                }

                f.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        try {
            RandomAccessFile file = new RandomAccessFile(path, mode);
            return file;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public static String add(String date1, String date2) {

        String[] h1 = date1.split("-");
        String[] h2 = date2.split("-");
        String[] h3 = new String[h1.length];
        h3[0] = (int) (Integer.parseInt(h1[0]) + Integer.parseInt(h2[0])) + "";
        h3[1] = (int) (Integer.parseInt(h1[1]) + Integer.parseInt(h2[1])) + "";
        h3[2] = (int) (Integer.parseInt(h1[2]) + Integer.parseInt(h2[2])) + "";
        h3[3] = (int) (Integer.parseInt(h1[3]) + Integer.parseInt(h2[3])) + "";
        h3[4] = (int) (Integer.parseInt(h1[4]) + Integer.parseInt(h2[4])) + "";

        return h3[0] + "-" + h3[1] + "-" + h3[2] + "-" + h3[3] + "-" + h3[4];
    }

    public static long generateIDRoom(List<AppointmentRoom> ids) {

        long id = 0;
        boolean contain = true;
        while (contain) {
            id = (long) (Math.random() * Long.MAX_VALUE);
            boolean is = false;
            for (int i = 0; i < ids.size(); i++) {
                if (ids.get(i).id == id) {
                    is = true;
                    break;
                }
            }

            if (is) {
                contain = true;
            } else {
                contain = false;
            }

        }

        return id;
    }

    public static long generateIDProc(List<GetProcedure.Procedure> ids) {

        long id = 0;
        boolean contain = true;
        while (contain) {
            id = (long) (Math.random() * Long.MAX_VALUE);
            boolean is = false;
            for (int i = 0; i < ids.size(); i++) {
                if (ids.get(i).proc_id == id) {
                    is = true;
                    break;
                }
            }

            if (is) {
                contain = true;
            } else {
                contain = false;
            }

        }

        return id;
    }

    public static long generateIDNotification(List<com.health.objects.GetNotifications.Notification> ids) {

        long id = 0;
        boolean contain = true;
        while (contain) {
            id = (long) (Math.random() * Long.MAX_VALUE);
            boolean is = false;
            for (int i = 0; i < ids.size(); i++) {
                if (ids.get(i).notification_id == id) {
                    is = true;
                    break;
                }
            }

            if (is) {
                contain = true;
            } else {
                contain = false;
            }

        }

        return id;
    }

    public static GetCondtion.Condtion findCondtion(long id, Patient p) {

        for (int i = 0; i < p.activeCondtions.size(); i++) {

            if (p.activeCondtions.get(i).condtion_id == id) {
                return p.activeCondtions.get(i);
            }

        }

        for (int i = 0; i < p.historyCondtions.size(); i++) {

            if (p.historyCondtions.get(i).condtion_id == id) {
                return p.historyCondtions.get(i);
            }

        }

        return null;

    }

    public static GetProcedure.Procedure findProc(long id, Patient p) {

        for (int i = 0; i < p.activeProcedures.size(); i++) {

            if (p.activeProcedures.get(i).proc_id == id) {
                return p.activeProcedures.get(i);
            }

        }

        for (int i = 0; i < p.historyProcedures.size(); i++) {

            if (p.historyProcedures.get(i).proc_id == id) {
                return p.historyProcedures.get(i);
            }

        }

        return null;

    }

    public static long generateIDCondtion(List<com.health.objects.GetCondtion.Condtion> ids) {

        long id = 0;
        boolean contain = true;
        while (contain) {
            id = (long) (Math.random() * Long.MAX_VALUE);
            boolean is = false;
            for (int i = 0; i < ids.size(); i++) {
                if (ids.get(i).condtion_id == id) {
                    is = true;
                    break;
                }
            }

            if (is) {
                contain = true;
            } else {
                contain = false;
            }

        }

        return id;
    }

    public static class Date {

        DateTime time;

        public Date(String date) {
            String[] h = date.split("-");
            int year = Integer.parseInt(h[0]);
            int month = Integer.parseInt(h[1]);
            int day = Integer.parseInt(h[2]);
            int hour = Integer.parseInt(h[3]);
            int minute = Integer.parseInt(h[4]);
            time = new DateTime(year, month, day, hour, minute);
        }

        public String withDays(int days) {

            DateTime g = new DateTime(time);
            g = g.plusDays(days);

            return (g.getYear() + "-" + g.getMonthOfYear() + "-" + g.getDayOfMonth() + "-" + g.getHourOfDay() + "-" + g.getMinuteOfHour());
        }

        public String with(int days, int hours, int min) {

            DateTime g = new DateTime(time);
            g = g.plusDays(days).plusHours(hours).plusMinutes(min);

            return (g.getYear() + "-" + g.getMonthOfYear() + "-" + g.getDayOfMonth() + "-" + g.getHourOfDay() + "-" + g.getMinuteOfHour());
        }

    }

    public static boolean isAfter(String date1, String date2) {
        Date d1 = new Date(date1);
        Date d2 = new Date(date2);
        return d1.time.isAfter(d2.time);
    }

    public static String dateWith(String date, int months, int weeks, int days, int hours, int minutes) {

        Date d = new Date(date);
        return d.with(months * 365 + weeks * 7 + days, hours, minutes);

    }

    public static int differenceDate(String date1, String date2) {

        Period d = new Period(date1, date2);
        return d.getPeriodInMinutes() * (d.isPast ? 1 : -1);
    }

    public static class Period {

        public int pyear;
        public int pmonth;
        public int pday;
        public int phour;
        public int pminute;

        public int amount;

        public boolean isPast;

        private DateTime date1;
        private DateTime date2;

        public Period(String date) {

            String[] h = date.split("-");
            int year = Integer.parseInt(h[0]);
            int month = Integer.parseInt(h[1]);
            int day = Integer.parseInt(h[2]);
            int hour = Integer.parseInt(h[3]);
            int minute = Integer.parseInt(h[4]);
            DateTime dateo = new DateTime(year, month, day, hour, minute);
            date1 = dateo;
            h = PathsMine.getCurrentTime().split("-");
            year = Integer.parseInt(h[0]);
            month = Integer.parseInt(h[1]);
            day = Integer.parseInt(h[2]);
            hour = Integer.parseInt(h[3]);
            minute = Integer.parseInt(h[4]);
            DateTime ctime = new DateTime(year, month, day, hour, minute);
            date2 = ctime;

            Interval interval;
            org.joda.time.Period period;
            if (dateo.isBefore(ctime)) {
                interval = new Interval(dateo, ctime);
                isPast = true;
            } else {
                interval = new Interval(ctime, dateo);
                isPast = false;
            }
            period = interval.toPeriod();
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
            date1 = dateo;
            h = date2.split("-");
            year = Integer.parseInt(h[0]);
            month = Integer.parseInt(h[1]);
            day = Integer.parseInt(h[2]);
            hour = Integer.parseInt(h[3]);
            minute = Integer.parseInt(h[4]);
            DateTime ctime = new DateTime(year, month, day, hour, minute);
            this.date2 = ctime;
            Interval interval;
            org.joda.time.Period period;
            if (dateo.isBefore(ctime)) {
                interval = new Interval(dateo, ctime);
                isPast = true;
            } else {
                interval = new Interval(ctime, dateo);
                isPast = false;
            }
            period = interval.toPeriod();
            pyear = period.getYears();
            pmonth = period.getMonths();
            pday = period.getDays();
            phour = period.getHours();
            pminute = period.getMinutes();

        }

        public int getPeriodInDays() {

            return Days.daysBetween(date1, date2).getDays();
        }

        public int getPeriodInMinutes() {
            return Minutes.minutesBetween(date1, date2).getMinutes();
        }

        public boolean moreThanADay() {
            return getPeriodInDays() > 1;
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

}
