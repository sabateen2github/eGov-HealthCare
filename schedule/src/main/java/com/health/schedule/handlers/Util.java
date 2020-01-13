/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.schedule.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.joda.time.DateTime;
import org.joda.time.Interval;

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

    public static RandomAccessFile getFile(String mode, String... paths) {

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < paths.length; i++) {

            if (paths[i].startsWith("/")) {
                paths[i] = paths[i].substring(1);
            }
            if (i != paths.length - 1 && !paths[i].endsWith("/")) {
                builder.append('/');
            }

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
                Logger.getLogger(Util.class
                        .getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        try {
            RandomAccessFile file = new RandomAccessFile(path, mode);
            return file;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Util.class
                    .getName()).log(Level.SEVERE, null, ex);
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
        Date d1 = new Date(date1);
        Date d2 = new Date(date2);
        return 0;

    }

    public static class Period {

        public int pyear;
        public int pmonth;
        public int pday;
        public int phour;
        public int pminute;

        public int amount;

        public boolean isPast;

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

}
