/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.schedule.handlers;

import com.health.objects.GetAffectedAppointments;
import com.health.objects.GetAvailibleRooms;
import com.health.objects.GetRoomsAppointments;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Inspiron
 */
public class ScheduleHandler {

    public static final HashMap<Integer, String> Days;
    public static final HashMap<String, Integer> DaysInv;

    private static Schedule schedule;

    static {
        schedule = new Schedule();
        Days = new HashMap<>();
        DaysInv = new HashMap<>();

        Days.put(1, "Monday");
        Days.put(2, "Tuesday");
        Days.put(3, "Wednesday");
        Days.put(4, "Thursday");
        Days.put(5, "Friday");
        Days.put(6, "Saturday");
        Days.put(7, "Sunday");

        DaysInv.put("Monday", 1);
        DaysInv.put("Tuesday", 2);
        DaysInv.put("Wednesday", 3);
        DaysInv.put("Thursday", 4);
        DaysInv.put("Friday", 5);
        DaysInv.put("Saturday", 6);
        DaysInv.put("Sunday", 7);
    }

    public static List<GetAffectedAppointments.AffectedAppointment> getAffected(List<GetAvailibleRooms.Room> rooms) {

        List<GetAffectedAppointments.AffectedAppointment> app = new ArrayList<>();
        for (int i = 0; i < rooms.size(); i++) {
            app.addAll(GetAffectedAppointments.getAffected(translateSchedule(rooms.get(i)), Arrays.asList(rooms.get(i).room_id)));
        }
        return app;
    }

    public static Integer[] getUnusedDaysI() {
        List<Integer> ds = new ArrayList<>();
        for (int i = 1; i < 8; i++) {
            if (!schedule.days.containsKey(i)) {
                ds.add(i);
            }
        }
        Integer[] l = new Integer[ds.size()];
        return ds.toArray(l);

    }

    public static String[] getUnusedDaysS() {
        List<String> ds = new ArrayList<>();
        for (int i = 1; i < 8; i++) {
            if (!schedule.days.containsKey(i)) {
                ds.add(Days.get(i));
            }
        }
        String[] l = new String[ds.size()];
        return ds.toArray(l);
    }

    public static void setDay(int day, int capacity, String from, String to) {
        if (!schedule.days.containsKey(day)) {
            schedule.days.put(day, new Day());
        }
        Day d = schedule.days.get(day);
        d.capacity = capacity;
        d.from = from;
        d.to = to;
    }

    public static Day getDay(int day) {

        return schedule.days.get(day);
    }

    public static void setFrom(String from) {
        schedule.from_date = from;
    }

    public static void setTo(String t) {
        schedule.to_date = t;
    }

    public static void removeDay(int day) {
        schedule.days.remove(day);
    }

    public static List<GetRoomsAppointments.Appointment> translateSchedule(GetAvailibleRooms.Room room) {

        List<GetRoomsAppointments.Appointment> apps = new ArrayList<>();
        Object[] keys = schedule.days.keySet().toArray();
        for (int i = 0; i < keys.length; i++) {
            String first = getFirstOccur(schedule.from_date, schedule.to_date, (Integer) keys[i]);
            int count = getOccrCount(schedule.from_date, schedule.to_date, (Integer) keys[i]);
            if (count > 0) {
                Util.Date d = new Util.Date(first + "-0-0");
                while (count > 0) {
                    GetRoomsAppointments.Appointment app = new GetRoomsAppointments.Appointment();
                    app.from = schedule.days.get((Integer) keys[i]).from;
                    app.to = schedule.days.get((Integer) keys[i]).to;
                    app.room_id = room.room_id;
                    app.room_path = room.room_path;
                    app.capacity = schedule.days.get((Integer) keys[i]).capacity;
                    app.date = d.withDays(7 * (count - 1));
                    apps.add(app);
                    count--;
                }
            }
        }
        return apps;
    }

    private static int getOccrCount(String from, String to, int day) {

        String first = getFirstOccur(from + "-0-0", to + "-0-0", day);
        if (first == null) {
            return 0;
        }
        Util.Period firstP = new Util.Period(first + "-0-0", to + "-0-0");
        int count = (firstP.getPeriodInDays() / 7) + 1;
        return count;
    }

    private static String getFirstOccur(String from, String to, int day) {

        Util.Period per = new Util.Period(from + "-0-0", to + "-0-0");
        Util.Date fr = new Util.Date(from + "-0-0");
        int frm_day = fr.time.getDayOfWeek();
        int days;
        if (frm_day > day) {
            days = 7 - (frm_day - day);
        } else {
            days = day -frm_day;
        }
        if (days > per.getPeriodInDays()) {
            return null;
        }
        String[] h = new String[3];
        String[] split = fr.withDays(days).split("-");
        h[0] = split[0];
        h[1] = split[1];
        h[2] = split[2];
        return h[0] + "-" + h[1] + "-" + h[2];
    }

    private static class Schedule {

        public String from_date;
        public String to_date;
        private HashMap<Integer, Day> days;

        public Schedule() {
            days = new HashMap<>();
        }
    }

    public static class Day {

        public int capacity;
        public String from;
        public String to;
    }

}
