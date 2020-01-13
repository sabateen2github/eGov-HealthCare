package com.health.project.entry;

import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.view.ViewGroup;

import com.health.objects.Strings;
import com.health.objects.Types;

import org.joda.time.DateTime;

public class Util {


    public static void safeBeginDelayedTranstion(ViewGroup v) {

        if (v == null) return;


        TransitionManager.beginDelayedTransition(v);
    }

    public static void safeBeginDelayedTranstion(ViewGroup v, Transition transition) {

        if (v == null) return;

        TransitionManager.beginDelayedTransition(v, transition);
    }

    public static String generateFloat(float x, int y) {
        return String.format(Strings.F, x);
    }


    static char[] arabicChars = {'٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};

    static char checkforarabic(final char ch) {

        if (Character.isDigit(ch)) {


            try {
                return arabicChars[ch - 48];
            } catch (Exception e) {
            }
        }

        return ch;
    }


    public static String generateDateAr(String d) {

        if (d.split("-").length > 3) {

            String[] split = d.split("-");
            String gh = split[0] + "/" + split[1] + "/" + split[2] + "    " + ((split[3].length() == 1) ? "0" + split[3] : split[3]) + ":" + ((split[4].length() == 1) ? "0" + split[4] : split[4]);
            return convertStringToAr(gh);

        } else {

            return convertStringToAr(d).replace("-", "/");
        }
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

    public static String convertStringToAr(String d) {
        if (Application.lang == Types.Lang_Arabic) {
            char[] chars = d.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                chars[i] = checkforarabic(chars[i]);
            }
            return new String(chars);
        }
        return d;
    }


    public static String convertStringintoAr(String d) {
        if (Application.lang == Types.Lang_Arabic) {
            char[] chars = d.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                chars[i] = arabicChars[chars[i] - 48];
            }
            return new String(chars);
        }
        return d;
    }
}
