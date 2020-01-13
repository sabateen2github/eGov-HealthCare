/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alaa.server;

import com.health.objects.GetAvailibleInsurances;
import com.health.objects.GetAvailibleMedications;
import com.health.objects.GetAvailibleRooms;
import com.health.objects.GetAvailibleServices;
import com.health.objects.GetCondtion;
import com.health.objects.GetProcedure;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Inspiron
 */
public class Cache {

    private static class Lock {

        private volatile boolean is_locked = false;
    }

    private static HashMap<Long, Executor> rooms_Exe;
    private static HashMap<Long, Executor> patients_Exe;
    private static HashMap<Long, Executor> services_Exe;
    private static HashMap<Long, Executor> insurance_Exe;
    private static HashMap<Long, Executor> medications_Exe;
    private static HashMap<Long, Executor> sessions_Exe;
    private static HashMap<Long, Executor> proc_Exe;
    private static HashMap<Long, Executor> cond_Exe;
    private static HashMap<Long, Executor> medteam_Exe;

    private static HashMap<Long, Lock> rooms_locks;
    private static HashMap<Long, Lock> patients_locks;
    private static HashMap<Long, Lock> services_locks;
    private static HashMap<Long, Lock> insurance_locks;
    private static HashMap<Long, Lock> medications_locks;
    private static HashMap<Long, Lock> sessions_locks;
    private static HashMap<Long, Lock> proc_locks;
    private static HashMap<Long, Lock> cond_locks;
    private static HashMap<Long, Lock> medteam_locks;

    private static HashMap<Long, GetAvailibleRooms.Room> rooms;
    private static HashMap<Long, List<AppointmentRoom>> appointments;

    private static HashMap<Long, Patient> patients;
    private static HashMap<Long, MedTeam> medTeams;
    private static HashMap<Long, GetAvailibleServices.Service> services;
    private static HashMap<Long, GetAvailibleMedications.Medications> medications;
    private static HashMap<Long, GetAvailibleInsurances.Insurance> insurances;
    private static HashMap<Long, Session> sessions;
    private static HashMap<Long, GetProcedure.Procedure> proc;
    private static HashMap<Long, GetCondtion.Condtion> conds;

    private static List<Long> room_ids;
    private static List<Long> service_ids;
    private static List<Long> medication_ids;
    private static List<Long> insurance_ids;
    private static List<Long> cond_ids;
    private static List<Long> proc_ids;
    private static List<Long> patient_ids;
    private static List<Long> medTeam_ids;
    private static List<Long> session_ids;

    static {
        rooms_Exe = new HashMap<>();
        patients_Exe = new HashMap<>();
        medications_Exe = new HashMap<>();
        services_Exe = new HashMap<>();
        insurance_Exe = new HashMap<>();
        sessions_Exe = new HashMap<>();
        proc_Exe = new HashMap<>();
        medteam_Exe = new HashMap<>();
        cond_Exe = new HashMap<>();

        rooms_locks = new HashMap<>();
        patients_locks = new HashMap<>();
        medications_locks = new HashMap<>();
        services_locks = new HashMap<>();
        insurance_locks = new HashMap<>();
        sessions_locks = new HashMap<>();
        proc_locks = new HashMap<>();
        medteam_locks = new HashMap<>();
        cond_locks = new HashMap<>();

        rooms = new HashMap<>();
        patients = new HashMap<>();
        medications = new HashMap<>();
        services = new HashMap<>();
        insurances = new HashMap<>();
        sessions = new HashMap<>();
        proc = new HashMap<>();
        medTeams = new HashMap<>();
        conds = new HashMap<>();
        appointments = new HashMap<>();
    }

    public static void init() {

    }

    private static void checkRoomExe(long id) {
        if (!rooms_Exe.containsKey(id)) {
            rooms_Exe.put(id, Executors.newSingleThreadExecutor());
        }
    }

    private static void checkServiceExe(long id) {
        if (!services_Exe.containsKey(id)) {
            services_Exe.put(id, Executors.newSingleThreadExecutor());
        }
    }

    private static void checkMedicationExe(long id) {
        if (!medications_Exe.containsKey(id)) {
            medications_Exe.put(id, Executors.newSingleThreadExecutor());
        }
    }

    private static void checkPatientExe(long id) {
        if (!patients_Exe.containsKey(id)) {
            patients_Exe.put(id, Executors.newSingleThreadExecutor());
        }
    }

    private static void checkInsuranceExe(long id) {
        if (!insurance_Exe.containsKey(id)) {
            insurance_Exe.put(id, Executors.newSingleThreadExecutor());
        }
    }

    private static void checkMedTeamExe(long id) {
        if (!medteam_Exe.containsKey(id)) {
            medteam_Exe.put(id, Executors.newSingleThreadExecutor());
        }
    }

    private static void checkSessionExe(long id) {
        if (!sessions_Exe.containsKey(id)) {
            sessions_Exe.put(id, Executors.newSingleThreadExecutor());
        }
    }

    private static void checkProcExe(long id) {
        if (!proc_Exe.containsKey(id)) {
            proc_Exe.put(id, Executors.newSingleThreadExecutor());
        }
    }

    private static void checkCondExe(long id) {
        if (!cond_Exe.containsKey(id)) {
            cond_Exe.put(id, Executors.newSingleThreadExecutor());
        }
    }

    public static List<AppointmentRoom> getAppointments(long room_id) {

        List<AppointmentRoom> apps = appointments.get(room_id);
        if (apps == null) {

        }
        return apps;
    }

    public static List<Long> getRoomsIds() {

    }

    public static List<Long> getMedTeamIds() {

    }

    public static List<Long> getServicesIDs() {

    }

    public static List<Long> getInsurancesIds() {

    }

    public static List<Long> getPatientIds() {

    }

    public static List<Long> getMedicationIds() {

    }

    public static List<Long> getProceduresIds() {

    }

    public static List<Long> getCondtionsIds() {

    }

    public static List<Long> getSessionIds() {

    }

    public static Session getSession(long id) {

    }

    public static AppointmentRoom getAppointment(long room_id, long app_id) {

        Lock l = rooms_locks.get(room_id);
        synchronized (l) {

            while (l.is_locked) {
                try {
                    l.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Cache.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            l.is_locked = true;

        }
    }

    public static Patient getPatient(long id) {

    }

    public static GetCondtion.Condtion getCondtion(long id) {

    }

    public static GetProcedure.Procedure getProcedure(long id) {

    }

    public static GetAvailibleInsurances.Insurance getInsurance(long id) {

    }

    public static GetAvailibleMedications.Medications getMedication(long id) {

    }

    public static GetAvailibleServices.Service getService(long id) {

    }

    public static GetAvailibleRooms.Room getRoom(long id) {

    }

    public static MedTeam getMedTeam(long id) {

    }

    public static void releaseAndsaveSessions(long id) {
        checkSessionExe(id);
        Lock l = sessions_locks.get(id);
        synchronized (l) {
            l.is_locked = false;
        }
        final Session s = server.gson.fromJson(server.gson.toJson(sessions.get(id)), Session.class);
        sessions_Exe.get(id).execute(new Runnable() {
            @Override
            public void run() {

            }
        });

    }

    public static void releaseAndsaveMedTeam(long id) {
        checkMedTeamExe(id);
        Lock l = medteam_locks.get(id);
        synchronized (l) {

            l.is_locked = false;
        }

        final MedTeam s = server.gson.fromJson(server.gson.toJson(medTeams.get(id)), MedTeam.class);
        medteam_Exe.get(id).execute(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    public static void releaseAndsavePatient(long id) {
        checkPatientExe(id);
        Lock l = patients_locks.get(id);
        synchronized (l) {

            l.is_locked = false;
        }

        final Patient s = server.gson.fromJson(server.gson.toJson(patients.get(id)), Patient.class);
        patients_Exe.get(id).execute(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    public static void releaseAndsaveRoom(long id) {
        checkRoomExe(id);
        Lock l = rooms_locks.get(id);
        synchronized (l) {

            l.is_locked = false;
        }
        final GetAvailibleRooms.Room s = server.gson.fromJson(server.gson.toJson(rooms.get(id)), GetAvailibleRooms.Room.class);
        rooms_Exe.get(id).execute(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    public static void releaseAndsaveAppointment(long room_id, long id) {
        checkRoomExe(room_id);
        Lock l = rooms_locks.get(room_id);
        synchronized (l) {

            l.is_locked = false;
        }

        final AppointmentRoom s = server.gson.fromJson(server.gson.toJson(appointments.get(id)), AppointmentRoom.class);
        rooms_Exe.get(room_id).execute(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    public static void releaseAndsaveService(long id) {
        checkServiceExe(id);
        Lock l = services_locks.get(id);
        synchronized (l) {

            l.is_locked = false;
        }
        final GetAvailibleServices.Service s = server.gson.fromJson(server.gson.toJson(services.get(id)), GetAvailibleServices.Service.class);
        services_Exe.get(id).execute(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    public static void releaseAndsaveProc(long id) {
        checkProcExe(id);
        Lock l = proc_locks.get(id);
        synchronized (l) {

            l.is_locked = false;
        }

        final GetProcedure.Procedure s = server.gson.fromJson(server.gson.toJson(proc.get(id)), GetProcedure.Procedure.class);
        proc_Exe.get(id).execute(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    public static void releaseAndsaveCond(long id) {
        checkCondExe(id);
        Lock l = cond_locks.get(id);
        synchronized (l) {

            l.is_locked = false;
        }

        final GetCondtion.Condtion s = server.gson.fromJson(server.gson.toJson(conds.get(id)), GetCondtion.Condtion.class);
        cond_Exe.get(id).execute(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    public static void releaseAndsaveMedication(long id) {
        checkMedicationExe(id);
        Lock l = medications_locks.get(id);
        synchronized (l) {

            l.is_locked = false;
        }

        final GetAvailibleMedications.Medications s = server.gson.fromJson(server.gson.toJson(medications.get(id)), GetAvailibleMedications.Medications.class);
        medications_Exe.get(id).execute(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    public static void releaseAndsaveInsurance(long id) {
        checkInsuranceExe(id);
        Lock l = insurance_locks.get(id);
        synchronized (l) {

            l.is_locked = false;
        }
        final GetAvailibleInsurances.Insurance s = server.gson.fromJson(server.gson.toJson(insurances.get(id)), GetAvailibleInsurances.Insurance.class);
        insurance_Exe.get(id).execute(new Runnable() {
            @Override
            public void run() {

            }
        });

    }

    //called only when installed or resetting
    public static void revalidatePatients() {

        try {
            RandomAccessFile list = new RandomAccessFile(PathsMine.InstalledPatients, "r");
            long num = list.length() / Long.BYTES;
            while (num > 0) {
                patient_ids.add(list.readLong());
                num--;
            }
            list.close();
        } catch (Exception e) {
        }

        patients.clear();
        patients_Exe.clear();
        patients_locks.clear();
        List<Long> ids = getPatientIds();
        for (int y = 0; y < ids.size(); y++) {
            patients_locks.put(ids.get(y), new Lock());
        }

    }
    //called only when installed or resetting

    public static void revalidateMedTeams() {

        try {
            RandomAccessFile list = new RandomAccessFile(PathsMine.InstalledMedTeamList, "r");
            long num = list.length() / Long.BYTES;
            while (num > 0) {
                medTeam_ids.add(list.readLong());
                num--;
            }
            list.close();
        } catch (Exception e) {
        }

        medTeams.clear();
        medteam_Exe.clear();
        medteam_locks.clear();
        List<Long> ids = getMedTeamIds();
        for (int y = 0; y < ids.size(); y++) {
            medteam_locks.put(ids.get(y), new Lock());
        }
    }
    //called only when installed or resetting

    public static void revalidateRooms() {

        try {
            RandomAccessFile list = new RandomAccessFile(PathsMine.InstalledRoomsList, "r");
            long num = list.length() / Long.BYTES;
            while (num > 0) {
                room_ids.add(list.readLong());
                num--;
            }
            list.close();
        } catch (Exception e) {
        }

        rooms.clear();
        rooms_Exe.clear();
        rooms_locks.clear();
        List<Long> ids = getRoomsIds();
        for (int y = 0; y < ids.size(); y++) {
            rooms_locks.put(ids.get(y), new Lock());
        }
    }
    //called only when installed or resetting

    public static void revalidateMedications() {

        try {
            RandomAccessFile list = new RandomAccessFile(PathsMine.InstalledMedicationsList, "r");
            long num = list.length() / Long.BYTES;
            while (num > 0) {
                medication_ids.add(list.readLong());
                num--;
            }
            list.close();
        } catch (Exception e) {
        }

        medications.clear();
        medications_Exe.clear();
        medications_locks.clear();
        List<Long> ids = getMedicationIds();
        for (int y = 0; y < ids.size(); y++) {
            medications_locks.put(ids.get(y), new Lock());
        }
    }
    //called only when installed or resetting

    public static void revalidateServices() {

        try {
            RandomAccessFile list = new RandomAccessFile(PathsMine.InstalledServicePathList, "r");
            long num = list.length() / Long.BYTES;
            while (num > 0) {
                service_ids.add(list.readLong());
                num--;
            }
            list.close();
        } catch (Exception e) {
        }

        services.clear();
        services_Exe.clear();
        services_locks.clear();
        List<Long> ids = getServicesIDs();
        for (int y = 0; y < ids.size(); y++) {
            services_locks.put(ids.get(y), new Lock());
        }
    }
    //called only when installed or resetting

    public static void revalidateInsurances() {

        try {
            RandomAccessFile list = new RandomAccessFile(PathsMine.InstalledInsurancesList, "r");
            long num = list.length() / Long.BYTES;
            while (num > 0) {
                insurance_ids.add(list.readLong());
                num--;
            }
            list.close();
        } catch (Exception e) {
        }

        insurances.clear();
        insurance_Exe.clear();
        insurance_locks.clear();
        List<Long> ids = getInsurancesIds();
        for (int y = 0; y < ids.size(); y++) {
            insurance_locks.put(ids.get(y), new Lock());
        }
    }
    //called only when installed or resetting

    public static void revalidateProc() {

        try {
            RandomAccessFile list = new RandomAccessFile(PathsMine.InstalledProcList, "r");
            long num = list.length() / Long.BYTES;
            while (num > 0) {
                proc_ids.add(list.readLong());
                num--;
            }
            list.close();
        } catch (Exception e) {
        }

        proc.clear();
        proc_Exe.clear();
        proc_locks.clear();
        List<Long> ids = getProceduresIds();
        for (int y = 0; y < ids.size(); y++) {
            proc_locks.put(ids.get(y), new Lock());
        }
    }
    //called only when installed or resetting

    public static void revalidateCond() {

        try {
            RandomAccessFile list = new RandomAccessFile(PathsMine.InstalledCondtionsList, "r");
            long num = list.length() / Long.BYTES;
            while (num > 0) {
                cond_ids.add(list.readLong());
                num--;
            }
            list.close();
        } catch (Exception e) {
        }

        conds.clear();
        cond_Exe.clear();
        cond_locks.clear();
        List<Long> ids = getCondtionsIds();
        for (int y = 0; y < ids.size(); y++) {
            cond_locks.put(ids.get(y), new Lock());
        }
    }
    //called only when installed or resetting

    public static void revalidateSessions() {

        session_ids.clear();

        try {
            RandomAccessFile list = new RandomAccessFile(PathsMine.SmartSystemList, "r");
            long num = list.length() / Long.BYTES;
            while (num > 0) {
                session_ids.add(list.readLong());
                num--;
            }
            list.close();
        } catch (Exception e) {
        }
        sessions.clear();
        sessions_Exe.clear();
        sessions_locks.clear();
        List<Long> ids = getSessionIds();
        for (int y = 0; y < ids.size(); y++) {
            sessions_locks.put(ids.get(y), new Lock());

        }

    }

}
