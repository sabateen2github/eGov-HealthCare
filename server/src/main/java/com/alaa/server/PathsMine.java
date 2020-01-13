/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alaa.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;

/**
 *
 * @author Inspiron
 */
public class PathsMine {

    public static final String InstalledServicePath = "./server/InstalledServices1/";
    public static final String InstalledServicePathList = "./server/InstalledServices1/ids.i";

    public static final String Time = "./server/server_time_log.i";

    public static final String InstalledMedications = "./server/InstalledMedications/";
    public static final String InstalledMedicationsList = "./server/InstalledMedications/ids.i";

    public static final String InstalledInsurances = "./server/InstalledInsurances/";
    public static final String InstalledInsurancesList = "./server/InstalledInsurances/ids.i";

    public static final String InstalledRooms = "./server/InstalledRooms/";
    public static final String InstalledRoomsList = "./server/InstalledRooms/ids.i";

    public static final String InstalledPatients = "./server/InstalledPatients/";
    public static final String InstalledPatientsList = "./server/InstalledPatients/ids.i";

    public static final String InstalledMedTeam = "./server/InstalledMedTeam/";
    public static final String InstalledMedTeamList = "./server/InstalledMedTeam/ids.i";

    public static final String InstalledCondtions = "./server/InstalledCondtions/";
    public static final String InstalledCondtionsList = "./server/InstalledCondtions/ids.i";

    public static final String InstalledProc = "./server/InstalledProc/";
    public static final String InstalledProcList = "./server/InstalledProc/ids.i";

    public static final String SmartSystem = "./server/SmartSystem/";
    public static final String SmartSystemList = "./server/SmartSystem/ids.i";

    public static final String InstalledSchedules = "/InstalledSchedules/";
    public static final String InstalledSchedulesList = "/InstalledSchedules/ids.i";

    private volatile static int year = -1;
    private volatile static int month = -1;
    private volatile static int day = -1;
    private volatile static int hour = -1;
    private volatile static int minute = -1;

    public static String getCurrentTime() {

        if (year == -1) {
            try {
                File f = new File("./server/time.i");
                if (f.exists()) {
                    RandomAccessFile time = new RandomAccessFile("./server/time.i", "r");
                    year = time.readInt();
                    month = time.readInt();
                    day = time.readInt();
                    hour = time.readInt();
                    minute = time.readInt();
                    time.close();
                } else {
                    year = 0;
                    month = 0;
                    day = 0;
                    hour = 0;
                    minute = 0;
                }
            } catch (Exception ex) {
                Logger.getLogger(PathsMine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        DateTime d = DateTime.now().plusYears(year).plusMonths(month).
                plusDays(day).
                plusHours(hour).
                plusMinutes(minute);

        return d.getYear() + "-" + d.getMonthOfYear() + "-" + d.getDayOfMonth() + "-" + d.getHourOfDay() + "-" + d.getMinuteOfHour();
    }

    public static void skipDateTime(String d) {
        try {
            String[] h = d.split("-");
            year = Integer.parseInt(h[0]);
            month = Integer.parseInt(h[1]);
            day = Integer.parseInt(h[2]);
            hour = Integer.parseInt(h[3]);
            minute = Integer.parseInt(h[4]);

            RandomAccessFile time = new RandomAccessFile("./server/time.i", "rw");
            year += time.readInt();
            month += time.readInt();
            day += time.readInt();
            hour += time.readInt();
            minute += time.readInt();
            time.seek(0);
            time.writeInt(year);
            time.writeInt(month);
            time.writeInt(day);
            time.writeInt(hour);
            time.writeInt(minute);
            time.close();
        } catch (Exception ex) {
            Logger.getLogger(PathsMine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void delete(File f) {
        if (f.isFile()) {
            System.out.println("deleting " + f.getPath() + "   " + f.delete());
            return;
        }
        File[] files = f.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                delete(files[i]);
            }
        } else {
            System.out.println(f.list());
        }
        f.delete();
    }

    private static void deletefiles(File f) {

        File[] files = f.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    delete(files[i]);
                }
            }
        } else {
            System.out.println(f.list());
        }
    }

    public static void reset() {
        delete(new File("./server"));
        init();
    }

    static void init() {
        File f = new File(InstalledServicePathList);

        if (!f.exists()) {

            try {
                f.getParentFile().mkdirs();
                f.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(PathsMine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        f = new File("./server/time.i");
        if (!f.exists()) {
            try {
                f.getParentFile().mkdirs();
                f.createNewFile();
                RandomAccessFile time = new RandomAccessFile("./server/time.i", "rw");
                time.writeInt(0);
                time.writeInt(0);
                time.writeInt(0);
                time.writeInt(0);
                time.writeInt(0);
                time.close();
            } catch (IOException ex) {
                Logger.getLogger(PathsMine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        f = new File(Time);
        if (!f.exists()) {
            try {
                f.getParentFile().mkdirs();
                f.createNewFile();
                RandomAccessFile time = new RandomAccessFile(Time, "rw");
                time.writeLong(0);
                time.close();
            } catch (IOException ex) {
                Logger.getLogger(PathsMine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        f = new File(InstalledMedicationsList);
        if (!f.exists()) {

            try {
                f.getParentFile().mkdirs();
                f.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(PathsMine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        f = new File(InstalledInsurancesList);
        if (!f.exists()) {

            try {
                f.getParentFile().mkdirs();
                f.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(PathsMine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        f = new File(InstalledRoomsList);
        if (!f.exists()) {

            try {
                f.getParentFile().mkdirs();
                f.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(PathsMine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        f = new File(InstalledPatientsList);
        if (!f.exists()) {

            try {
                f.getParentFile().mkdirs();
                f.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(PathsMine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        f = new File(InstalledMedTeamList);
        if (!f.exists()) {

            try {
                f.getParentFile().mkdirs();
                f.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(PathsMine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        f = new File(InstalledCondtionsList);
        if (!f.exists()) {

            try {
                f.getParentFile().mkdirs();
                f.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(PathsMine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        f = new File(InstalledProcList);
        if (!f.exists()) {

            try {
                f.getParentFile().mkdirs();
                f.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(PathsMine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        f = new File(SmartSystemList);
        if (!f.exists()) {

            try {
                f.getParentFile().mkdirs();
                f.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(PathsMine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
