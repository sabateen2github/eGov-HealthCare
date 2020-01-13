
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alaa.server;

import com.alaa.server.Util.Date;
import com.alaa.server.Util.Period;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.translate.AmazonTranslate;
import com.amazonaws.services.translate.AmazonTranslateClient;
import com.amazonaws.services.translate.model.DetectedLanguageLowConfidenceException;
import com.amazonaws.services.translate.model.TranslateTextRequest;
import com.amazonaws.services.translate.model.TranslateTextResult;
import com.google.gson.reflect.TypeToken;
import com.health.objects.GetAffectedAppointments;
import com.health.objects.GetAvailibleInsurances;
import com.health.objects.GetAvailibleMedications;
import com.health.objects.GetAvailibleRooms;
import com.health.objects.GetAvailibleRooms.Room;
import com.health.objects.GetAvailibleServices;
import com.health.objects.GetCondtion;
import com.health.objects.GetFullAppointment;
import com.health.objects.GetNotifications;
import com.health.objects.GetProcedure;
import com.health.objects.GetRoomCurrentAppointment;
import com.health.objects.GetRoomsAppointments;
import com.health.objects.GetTime.Time;
import com.health.objects.Strings;
import com.health.objects.Types;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;

/**
 *
 * @author Inspiron
 */
public class Views {

    public static class TranslateHandler {

        static boolean is = true;

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

        public static String convertStringToAr(String d) {
            char[] chars = d.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                chars[i] = checkforarabic(chars[i]);
            }
            return new String(chars);
        }

        public static String convertStringToEn(String d) {
            char[] chars = d.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                chars[i] = checkforarabic(chars[i]);
            }
            return new String(chars);
        }

        public static String translate(String text, int from_lang, int to_lang, long patient_id) {

            long hash = ((long) text.hashCode()) << 32 | to_lang;

            boolean is = false;
            File f = new File(PathsMine.InstalledPatients + patient_id + "tr/", hash + ".txt");
            if (!f.getParentFile().exists()) {
                is = false;
                try {
                    f.getParentFile().mkdirs();
                } catch (Exception ex) {
                    Logger.getLogger(Views.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                if (f.exists()) {
                    is = true;
                } else {
                    is = false;
                }
            }

            if (is) {
                try {
                    RandomAccessFile file = new RandomAccessFile(f, "rw");
                    int size = file.readInt();
                    byte[] date = new byte[size];
                    file.read(date);
                    String g = new String(date, "UTF-8");

                    String tr = g;
                    file.close();
                    return tr;
                } catch (Exception e) {
                    e.printStackTrace();
                    return text;
                }
            } else {
                try {
                    String str = translateText(text, from_lang, to_lang);

                    f.createNewFile();
                    RandomAccessFile file = new RandomAccessFile(f, "rw");
                    byte[] dk = str.getBytes("UTF-8");
                    int size = dk.length;
                    file.writeInt(size);
                    file.write(dk);
                    file.close();
                    return str;
                } catch (Exception e) {
                    e.printStackTrace();
                    return text;
                }
            }
        }

        public static String translateText(String text, int lang_from, int lang_to) {

            try {
                System.setProperty("aws.accessKeyId", "AKIAJDUWAH5BWBBJAD7A");
                System.setProperty("aws.secretKey", "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");
                AWSCredentialsProvider credential = DefaultAWSCredentialsProviderChain.getInstance();
                AmazonTranslate translate = AmazonTranslateClient.builder().withCredentials(credential).withRegion(Regions.DEFAULT_REGION).build();
                if (lang_from == -1) {
                    TranslateTextRequest request = new TranslateTextRequest().withText(text).withSourceLanguageCode("auto").withTargetLanguageCode(lang_to == Types.Lang_Arabic ? "ar" : "en");
                    TranslateTextResult result = translate.translateText(request);
                    if (!result.getSourceLanguageCode().equals("ar") && !result.getSourceLanguageCode().equals("en")) {
                        return text;
                    }
                    return result.getTranslatedText();

                } else if (lang_from == Types.Lang_English) {
                    TranslateTextRequest request = new TranslateTextRequest().withText(text).withSourceLanguageCode("en").withTargetLanguageCode(lang_to == Types.Lang_Arabic ? "ar" : "en");
                    TranslateTextResult result = translate.translateText(request);
                    return result.getTranslatedText();
                } else {
                    TranslateTextRequest request = new TranslateTextRequest().withText(text).withSourceLanguageCode("ar").withTargetLanguageCode(lang_to == Types.Lang_Arabic ? "ar" : "en");
                    TranslateTextResult result = translate.translateText(request);
                    return result.getTranslatedText();
                }
            } catch (DetectedLanguageLowConfidenceException e) {
                ////System.out.println("low confidence");
                if (e.getDetectedLanguageCode().equals("ar") && lang_to == Types.Lang_English) {
                    return translateText(text, Types.Lang_Arabic, lang_to);
                } else if (e.getDetectedLanguageCode().equals("en") && lang_to == Types.Lang_Arabic) {
                    return translateText(text, Types.Lang_English, lang_to);
                } else {
                    return text;
                }
            } catch (Exception e) {
                e.printStackTrace();
                ////System.out.println("Error translate " + text);
                return text;
            }
        }
    }

    private static void updateLastTimeModified(Patient patient) {
        patient.last_time_modified = System.currentTimeMillis();
        patient.personalData.last_time_modified = patient.last_time_modified;
    }

    private static Session getSession(List<GetAvailibleServices.Service> services, int z) {
        try {
            RandomAccessFile list = new RandomAccessFile(PathsMine.SmartSystemList, "r");
            long num = list.length() / Long.BYTES;
            ArrayList<Long> ids = new ArrayList<>((int) num);
            while (num > 0) {
                ids.add(list.readLong());
                num--;
            }
            list.close();

            Session s = null;
            for (int i = 0; i < ids.size(); i++) {
                s = getSession(ids.get(i));
                if (s != null) {

                    if (s.service_ids.size() < services.size()) {
                        s = null;
                        continue;
                    }
                    boolean quit = false;
                    for (int x = 0; x < services.size(); x++) {
                        boolean found = false;
                        for (int y = 0; y < s.service_ids.size(); y++) {
                            if (services.get(x).service_id == s.service_ids.get(y)) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            quit = true;
                            break;
                        }
                    }
                    if (quit) {
                        s = null;
                        continue;
                    }
                    return s;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Views.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static Session getSession(List<Long> services) {

        try {
            RandomAccessFile list = new RandomAccessFile(PathsMine.SmartSystemList, "r");
            long num = list.length() / Long.BYTES;
            ArrayList<Long> ids = new ArrayList<>((int) num);
            while (num > 0) {
                ids.add(list.readLong());
                num--;
            }
            list.close();

            Session s = null;
            for (int i = 0; i < ids.size(); i++) {
                s = getSession(ids.get(i));

                if (s != null) {

                    if (s.service_ids.size() < services.size()) {
                        s = null;
                        continue;
                    }

                    boolean quit = false;
                    for (int x = 0; x < services.size(); x++) {
                        boolean found = false;
                        for (int y = 0; y < s.service_ids.size(); y++) {
                            if (services.get(x).equals(s.service_ids.get(y))) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {

                            quit = true;
                            break;
                        }
                    }

                    if (quit) {
                        s = null;
                        continue;
                    }
                    return s;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Views.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String handleBook(String json) {
        String res = null;

        try {
            com.health.objects.BookAppointment.Request request = server.gson.fromJson(json, com.health.objects.BookAppointment.Request.class);
            Patient p = getPatient(request.patient_id);
            com.health.objects.GetProcedure.Procedure c = Util.findProc(request.procedure_id, p);

            if (c == null) {
                return Strings.SUCCESS;
            }

            if (c.state == Types.State_Proc_Active) {

                GetProcedure.ServicesGroup group = null;
                for (GetProcedure.ServicesGroup f : c.services) {
                    if (f.req_id == request.request_id) {
                        group = f;
                        break;
                    }
                }

                if (group != null) {

                    if (group.appointment.appointment_id != 0) {
                        AppointmentRoom app = getAppointmentRoom(group.appointment.appointment_id, group.appointment.room_id);
                        boolean found = false;
                        for (int i = 0; i < app.Queue.size(); i++) {
                            if (app.Queue.get(i).patient_id == p.id) {
                                app.Queue.remove(i);
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            for (int i = 0; i < app.booked.size(); i++) {
                                if (app.booked.get(i).patient_id == p.id) {
                                    app.booked.remove(i);
                                    found = true;
                                    break;
                                }
                            }
                        }
                        if (!found) {
                            for (int i = 0; i < app.temp_booked.size(); i++) {
                                if (app.temp_booked.get(i).patient_id == p.id) {
                                    app.temp_booked.remove(i);
                                    break;
                                }
                            }
                        }
                        updateAppointmentRoom(app, group.appointment.room_id);
                        if (group.appointment.state != Types.Appointment_Invoice_Not_paid) {
                            group.appointment.state = Types.Appointment_Not_Booked;
                        }

                    }

                    GetAvailibleRooms.Room room = getRoom(request.room_id);
                    AppointmentRoom app = getAppointmentRoom(request.appointment_id, request.room_id);

                    group.appointment.appointment_id = app.id;
                    group.appointment.date = app.date;
                    group.appointment.from = app.from;
                    group.appointment.to = app.to;
                    group.appointment.room_id = room.room_id;
                    group.appointment.room_path = room.room_path;

                    if (group.appointment.state == Types.Appointment_Not_Booked) {
                        String h = Util.add(group.appointment.date, "0-0-0-" + group.appointment.from);
                        if (Util.isAfter(PathsMine.getCurrentTime(), h)) {
                            group.appointment.queue_amount = app.Queue.size();
                            group.appointment.state = Types.Appointment_Waiting_For_Enter_Line;
                        } else {
                            group.appointment.state = Types.Appointment_Waiting_For_Date;
                        }
                    }

                    AppointmentRoom.Patient pat = new AppointmentRoom.Patient();
                    pat.patient_id = p.id;
                    pat.procedure_id = c.proc_id;
                    pat.request_id = group.req_id;
                    if (group.appointment.state == Types.Appointment_Invoice_Not_paid) {
                        boolean add = true;
                        for (int i = 0; i < app.temp_booked.size(); i++) {
                            if (app.temp_booked.get(i).patient_id == p.id) {
                                add = false;
                                break;
                            }
                        }
                        if (add) {
                            app.temp_booked.add(pat);
                        }
                    } else {
                        boolean add = true;
                        for (int i = 0; i < app.booked.size(); i++) {
                            if (app.booked.get(i).patient_id == p.id) {
                                add = false;
                                break;
                            }
                        }
                        if (add) {
                            app.booked.add(pat);
                        }
                    }
                    updateAppointmentRoom(app, room.room_id);
                }
                updateLastTimeModified(p);
                savePatient(p);
            }
            res = Strings.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;

    }

    private static GetAvailibleRooms.Room getRoom(long room_id) throws FileNotFoundException, IOException {
        GetAvailibleRooms.Room room;
        check(PathsMine.InstalledRooms + room_id, "details.i");
        RandomAccessFile room_details = new RandomAccessFile(PathsMine.InstalledRooms + room_id + "/details.i", "r");
        int size = room_details.readInt();
        byte[] date = new byte[size];
        room_details.read(date);
        String g = new String(date, "UTF-8");
        room = server.gson.fromJson(g, GetAvailibleRooms.Room.class);
        room_details.close();
        return room;
    }

    public static String handleDoneRequest(String json) {
        String res = null;
        try {
            com.health.objects.DoneRequest.Request request = server.gson.fromJson(json, com.health.objects.DoneRequest.Request.class);
            Patient p = getPatient(request.patient_id);
            com.health.objects.GetProcedure.Procedure c = Util.findProc(request.procedure_id, p);
            if (c == null) {
                return Strings.SUCCESS;
            }
            if (c.state == Types.State_Proc_Active) {

                Object obj = null;
                int order = 0;

                for (GetProcedure.FeedBack f : c.feedbacks) {

                    if (f.req_id == request.request_id) {
                        obj = f;
                        order = f.order;
                        f.state = Types.Request_Done;
                        MedTeam m = getMedTeam(f.med_team_id);
                        if (m != null) {
                            for (int i = 0; i < m.patients.size(); i++) {
                                if (m.patients.get(i).patient_id == p.id) {
                                    m.patients.remove(i);
                                    break;
                                }
                            }
                            updateMedTeam(m);
                        }
                        break;
                    }

                }
                if (obj == null) {
                    for (GetProcedure.SmartFeedBack f : c.smart_feedbacks) {
                        if (f.req_id == request.request_id) {
                            obj = f;
                            order = f.order;
                            Session m = getSession(f.session_id);
                            if (m != null) {
                                for (int i = 0; i < m.patients.size(); i++) {
                                    if (m.patients.get(i).patient_id == p.id) {
                                        m.patients.remove(i);
                                        break;
                                    }
                                }
                                updateSession(m);
                            }
                            break;
                        }

                    }
                }

                /*
                if (obj == null) {
                    for (GetProcedure.ServicesGroup f : c.services) {
                        if (f.req_id == request.request_id) {
                            obj = f;
                            order = f.order;

                            if (f.appointment.state == Types.Appointment_Invoice_Not_paid) {

                                if (f.appointment.room_id != 0) {
                                    AppointmentRoom app = getAppointmentRoom(f.appointment.appointment_id, f.appointment.room_id);
                                    for (int i = 0; i < app.temp_booked.size(); i++) {
                                        if (app.temp_booked.get(i).patient_id == p.id) {
                                            app.temp_booked.remove(i);
                                            break;
                                        }
                                    }
                                    updateAppointmentRoom(app, f.appointment.room_id);
                                }

                            } else if (f.appointment.state == Types.Appointment_Not_Booked) {

                            } else if (f.appointment.state == Types.Appointment_Waiting_For_Date) {
                                if (f.appointment.room_id != 0) {
                                    AppointmentRoom app = getAppointmentRoom(f.appointment.appointment_id, f.appointment.room_id);
                                    for (int i = 0; i < app.booked.size(); i++) {
                                        if (app.booked.get(i).patient_id == p.id) {
                                            app.booked.remove(i);
                                            break;
                                        }
                                    }
                                    updateAppointmentRoom(app, f.appointment.room_id);
                                }

                            } else if (f.appointment.state == Types.Appointment_Waiting_For_Enter_Line) {
                                if (f.appointment.room_id != 0) {
                                    AppointmentRoom app = getAppointmentRoom(f.appointment.appointment_id, f.appointment.room_id);
                                    for (int i = 0; i < app.booked.size(); i++) {
                                        if (app.booked.get(i).patient_id == p.id) {
                                            app.booked.remove(i);
                                            break;
                                        }
                                    }
                                    updateAppointmentRoom(app, f.appointment.room_id);
                                }

                            } else if (f.appointment.state == Types.Appointment_Waiting_For_Enter) {
                                if (f.appointment.room_id != 0) {
                                    AppointmentRoom app = getAppointmentRoom(f.appointment.appointment_id, f.appointment.room_id);
                                    for (int i = 0; i < app.Queue.size(); i++) {
                                        if (app.Queue.get(i).patient_id == p.id) {
                                            app.Queue.remove(i);
                                            break;
                                        }
                                    }
                                    updateAppointmentRoom(app, f.appointment.room_id);
                                }
                            }
                            break;
                        }
                    }

                }
                 */
 /*
                if (obj instanceof GetProcedure.ServicesGroup) {

                    boolean found = false;
                    for (int i = 0; i < c.services.size(); i++) {

                        if (c.services.get(i).state == Types.Request_InProgress) {
                            found = true;
                            break;
                        }
                    }

                    if (!found) {

                        boolean isActive = false;
                        for (int i = 0; i < c.feedbacks.size(); i++) {

                            if (c.feedbacks.get(i).order == order + 1) {
                                isActive = true;
                                c.feedbacks.get(i).state = Types.Request_InProgress;
                                MedTeam m = getMedTeam(c.feedbacks.get(i).med_team_id);
                                MedTeam.Patient mP = new MedTeam.Patient();
                                mP.patient_id = p.id;
                                mP.procedure_id = c.proc_id;
                                mP.request_id = c.feedbacks.get(i).req_id;
                                m.patients.add(mP);
                                updateMedTeam(m);
                            }
                        }

                        for (int i = 0; i < c.smart_feedbacks.size(); i++) {
                            if (c.smart_feedbacks.get(i).order == order + 1) {
                                isActive = true;
                                c.smart_feedbacks.get(i).state = Types.Request_InProgress;
                                Session m = getSession(c.smart_feedbacks.get(i).services, 0);
                                if (m == null) {
                                    m = createSession(c.smart_feedbacks.get(i).services);
                                }
                                c.smart_feedbacks.get(i).session_id = m.id;
                                Session.Patient mP = new Session.Patient();
                                mP.patient_id = p.id;
                                mP.procedure_id = c.proc_id;
                                mP.request_id = c.smart_feedbacks.get(i).req_id;
                                m.patients.add(mP);
                                updateSession(m);
                            }

                        }
                        for (int i = 0; i < c.services.size(); i++) {
                            if (c.services.get(i).order == order + 1) {
                                isActive = true;
                                c.services.get(i).state = Types.Request_InProgress;
                                if (c.services.get(i).appointment == null) {
                                    c.services.get(i).appointment = new GetProcedure.Appointment();
                                }
                                c.services.get(i).appointment.state = Types.Appointment_Invoice_Not_paid;
                            }
                        }
                        if (!isActive) {
                            com.health.objects.GetNotifications.Notification noti = new com.health.objects.GetNotifications.Notification();
                            noti.type = com.health.objects.GetNotifications.Notification_Visit_Done;
                            noti.condtion_id = c.condtion_id;
                            noti.procedure_id = c.proc_id;
                            noti.notification_id = Util.generateIDNotification(p.notifications);
                            noti.date = PathsMine.getCurrentTime();
                            p.notifications.add(noti);
                            p.activeProcedures.remove(c);
                            c.state = Types.State_Proc_History;
                            p.historyProcedures.add(c);
                        }

                    }

                } else {*/
                boolean isActive = false;
                for (int i = 0; i < c.feedbacks.size(); i++) {
                    if (c.feedbacks.get(i).order == order + 1) {
                        isActive = true;
                        c.feedbacks.get(i).state = Types.Request_InProgress;
                        MedTeam m = getMedTeam(c.feedbacks.get(i).med_team_id);
                        MedTeam.Patient mP = new MedTeam.Patient();
                        mP.patient_id = p.id;
                        mP.procedure_id = c.proc_id;
                        mP.request_id = c.feedbacks.get(i).req_id;
                        m.patients.add(mP);
                        updateMedTeam(m);
                    }
                }

                for (int i = 0; i < c.smart_feedbacks.size(); i++) {

                    if (c.smart_feedbacks.get(i).order == order + 1) {
                        isActive = true;
                        c.smart_feedbacks.get(i).state = Types.Request_InProgress;
                        Session m = getSession(c.smart_feedbacks.get(i).services, 0);
                        if (m == null) {
                            m = createSession(c.smart_feedbacks.get(i).services);
                        }
                        c.smart_feedbacks.get(i).session_id = m.id;

                        Session.Patient mP = new Session.Patient();
                        mP.patient_id = p.id;
                        mP.procedure_id = c.proc_id;
                        mP.request_id = c.smart_feedbacks.get(i).req_id;
                        m.patients.add(mP);
                        updateSession(m);
                    }

                }

                for (int i = 0; i < c.services.size(); i++) {
                    if (c.services.get(i).order == order + 1) {
                        isActive = true;
                        c.services.get(i).state = Types.Request_InProgress;
                        if (c.services.get(i).appointment == null) {
                            c.services.get(i).appointment = new GetProcedure.Appointment();
                        }
                        c.services.get(i).appointment.state = Types.Appointment_Invoice_Not_paid;

                    }
                }
                if (!isActive) {
                    com.health.objects.GetNotifications.Notification noti = new com.health.objects.GetNotifications.Notification();
                    noti.type = com.health.objects.GetNotifications.Notification_Visit_Done;
                    noti.condtion_id = c.condtion_id;
                    noti.procedure_id = c.proc_id;
                    noti.notification_id = Util.generateIDNotification(p.notifications);
                    noti.date = PathsMine.getCurrentTime();
                    noti.date_added = PathsMine.getCurrentTime();
                    noti.date_added = PathsMine.getCurrentTime();

                    p.notifications.add(noti);
                    p.activeProcedures.remove(c);
                    c.state = Types.State_Proc_History;
                    p.historyProcedures.add(c);
                }
                //}

                updateLastTimeModified(p);
                savePatient(p);
            }

            res = Strings.SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;

    }

    public static String handlePayService(String json) {
        String res = null;
        try {
            com.health.objects.PayForService.Request request = server.gson.fromJson(json, com.health.objects.PayForService.Request.class);
            Patient p = getPatient(request.patient_id);
            com.health.objects.GetProcedure.Procedure c = Util.findProc(request.procedure_id, p);
            if (c == null) {
                return Strings.SUCCESS;
            }
            if (c.state == Types.State_Proc_Active) {
                GetProcedure.ServicesGroup group = null;
                for (GetProcedure.ServicesGroup f : c.services) {
                    if (f.req_id == request.request_id) {
                        group = f;
                        break;
                    }
                }
                if (group != null) {

                    if (group.appointment.appointment_id != 0) {

                        AppointmentRoom app = getAppointmentRoom(group.appointment.appointment_id, group.appointment.room_id);

                        AppointmentRoom.Patient pat = null;
                        for (int i = 0; i < app.temp_booked.size(); i++) {
                            if (app.temp_booked.get(i).patient_id == p.id) {
                                pat = app.temp_booked.remove(i);
                                app.booked.add(pat);
                                break;
                            }
                        }
                        String h = Util.add(group.appointment.date, "0-0-0-" + group.appointment.from);
                        if (Util.isAfter(PathsMine.getCurrentTime(), h)) {
                            group.appointment.queue_amount = app.Queue.size();
                            group.appointment.state = Types.Appointment_Waiting_For_Enter_Line;
                        } else {
                            group.appointment.state = Types.Appointment_Waiting_For_Date;
                        }
                        updateAppointmentRoom(app, group.appointment.room_id);
                    } else {
                        group.appointment.state = Types.Appointment_Not_Booked;
                        group.appointment.appointment_id = 0;
                    }

                }

                updateLastTimeModified(p);
                savePatient(p);
            }

            res = Strings.SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return res;

    }

    public static boolean handleReset(String json) {
        try {
            server.isResetting = true;
            PathsMine.reset();
            server.isResetting = false;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String handleEnterLine(String json) {
        String res = null;
        try {
            com.health.objects.EnterTheLine.Request request = server.gson.fromJson(json, com.health.objects.EnterTheLine.Request.class);
            Patient p = getPatient(request.patient_id);
            com.health.objects.GetProcedure.Procedure c = Util.findProc(request.procedure_id, p);
            if (c == null) {
                return Strings.SUCCESS;
            }
            if (c.state == Types.State_Proc_Active) {
                GetProcedure.ServicesGroup group = null;
                for (GetProcedure.ServicesGroup f : c.services) {
                    if (f.req_id == request.request_id) {
                        group = f;
                        break;
                    }
                }
                if (group != null) {

                    if (group.appointment.appointment_id != 0) {

                        AppointmentRoom app = getAppointmentRoom(group.appointment.appointment_id, group.appointment.room_id);

                        AppointmentRoom.Patient pat = null;
                        for (int i = 0; i < app.booked.size(); i++) {
                            if (app.booked.get(i).patient_id == p.id) {
                                pat = app.booked.remove(i);
                                app.Queue.add(pat);
                                break;
                            }
                        }
                        group.appointment.state = Types.Appointment_Waiting_For_Enter;
                        updateAppointmentRoom(app, group.appointment.room_id);

                    } else {
                        group.appointment.state = Types.Appointment_Not_Booked;
                        group.appointment.appointment_id = 0;
                    }
                }
                updateLastTimeModified(p);
                savePatient(p);
            }

            res = Strings.SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    public static String handlePatientHandle(String json) {
        String res = null;

        try {
            com.health.objects.HandlePatient.Request request = server.gson.fromJson(json, com.health.objects.HandlePatient.Request.class);

            Patient p = getPatient(request.patient_id);
            com.health.objects.GetProcedure.Procedure c = Util.findProc(request.procedure_id, p);
            if (c == null) {
                return Strings.SUCCESS;
            }

            if (c.state == Types.State_Proc_Active) {
                GetProcedure.ServicesGroup group = null;
                for (GetProcedure.ServicesGroup f : c.services) {
                    if (f.req_id == request.request_id) {
                        group = f;
                        break;
                    }
                }

                if (group != null && group.appointment.appointment_id != 0) {

                    AppointmentRoom app = getAppointmentRoom(group.appointment.appointment_id, group.appointment.room_id);

                    AppointmentRoom.Patient pat = null;
                    for (int i = 0; i < app.Queue.size(); i++) {
                        if (app.Queue.get(i).patient_id == p.id) {
                            pat = app.Queue.remove(i);
                            break;
                        }
                    }
                    pat.handeled = true;
                    updateAppointmentRoom(app, group.appointment.room_id);
                    group.state = Types.Request_Done;

                    boolean isActive = false;
                    for (int i = 0; i < c.feedbacks.size(); i++) {

                        if (c.feedbacks.get(i).order == group.order + 1) {
                            isActive = true;
                            c.feedbacks.get(i).state = Types.Request_InProgress;
                            MedTeam m = null;
                            try {
                                m = getMedTeam(c.feedbacks.get(i).med_team_id);
                            } catch (IOException ex) {
                                Logger.getLogger(Views.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            MedTeam.Patient mP = new MedTeam.Patient();
                            mP.patient_id = p.id;
                            mP.procedure_id = c.proc_id;
                            mP.request_id = c.feedbacks.get(i).req_id;
                            m.patients.add(mP);
                            try {
                                updateMedTeam(m);
                            } catch (IOException ex) {
                                Logger.getLogger(Views.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }

                    for (int i = 0; i < c.smart_feedbacks.size(); i++) {

                        if (c.smart_feedbacks.get(i).order == group.order + 1) {
                            isActive = true;
                            c.smart_feedbacks.get(i).state = Types.Request_InProgress;
                            Session m = getSession(c.smart_feedbacks.get(i).services, 0);
                            if (m == null) {
                                m = createSession(c.smart_feedbacks.get(i).services);
                            }
                            c.smart_feedbacks.get(i).session_id = m.id;

                            Session.Patient mP = new Session.Patient();
                            mP.patient_id = p.id;
                            mP.procedure_id = c.proc_id;
                            mP.request_id = c.smart_feedbacks.get(i).req_id;
                            m.patients.add(mP);
                            updateSession(m);
                        }

                    }
                    for (int i = 0; i < c.services.size(); i++) {
                        if (c.services.get(i).order == group.order + 1) {
                            isActive = true;
                            c.services.get(i).state = Types.Request_InProgress;
                            if (c.services.get(i).appointment == null) {
                                c.services.get(i).appointment = new GetProcedure.Appointment();
                            }
                            c.services.get(i).appointment.state = Types.Appointment_Invoice_Not_paid;
                        }
                    }
                    if (!isActive) {
                        com.health.objects.GetNotifications.Notification noti = new com.health.objects.GetNotifications.Notification();
                        noti.type = com.health.objects.GetNotifications.Notification_Visit_Done;
                        noti.condtion_id = c.condtion_id;
                        noti.procedure_id = c.proc_id;
                        noti.notification_id = Util.generateIDNotification(p.notifications);
                        noti.date = PathsMine.getCurrentTime();
                        noti.date_added = PathsMine.getCurrentTime();

                        p.notifications.add(noti);
                        p.activeProcedures.remove(c);
                        c.state = Types.State_Proc_History;
                        p.historyProcedures.add(c);
                    }
                } else {
                    throw new Exception();
                }
                updateLastTimeModified(p);
                savePatient(p);
            }

            res = Strings.SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;

    }

    public static String handleSkipTime(String json) {
        server.isResetting = true;
        String res = null;
        try {
            com.health.objects.SkipTime.Request request = server.gson.fromJson(json, com.health.objects.SkipTime.Request.class);
            PathsMine.skipDateTime(request.years + "-" + request.months + "-" + request.days + "-" + request.hours + "-" + request.minutes);
            RandomAccessFile time = Util.getFile("rw", PathsMine.Time);
            time.writeLong(System.currentTimeMillis());
            time.close();
            res = Strings.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }

        server.isResetting = false;
        return res;
    }

    public static String handleStopProcedure(String json) {

        String res = null;
        try {

            com.health.objects.StopProcedure.Request request = server.gson.fromJson(json, com.health.objects.StopProcedure.Request.class);
            Patient p = getPatient(request.patient_id);
            com.health.objects.GetProcedure.Procedure c = Util.findProc(request.procedure_id, p);
            if (c == null) {
                return Strings.SUCCESS;
            }

            if (c.state == Types.State_Proc_Active) {
                c.date_deactivation = PathsMine.getCurrentTime();
                c.state = com.health.objects.Types.State_Proc_History;
                p.historyProcedures.add(c);
                p.activeProcedures.remove(c);

                stopFeedBacks(p, c);
                stopSmartFeeds(p, c);
                stopVisits(p, c);

                com.health.objects.GetNotifications.Notification noti = new com.health.objects.GetNotifications.Notification();
                noti.type = com.health.objects.GetNotifications.Notification_Visit_Failed;
                noti.condtion_id = c.condtion_id;
                noti.procedure_id = c.proc_id;
                noti.notification_id = Util.generateIDNotification(p.notifications);
                noti.date = PathsMine.getCurrentTime();
                noti.date_added = PathsMine.getCurrentTime();

                p.notifications.add(noti);
                p.lastTimeUpdated = PathsMine.getCurrentTime();
                updateLastTimeModified(p);
                savePatient(p);
            }

            res = Strings.SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();

        }

        return res;
    }

    private static void stopFeedBacks(Patient p, GetProcedure.Procedure pr) throws IOException {

        for (GetProcedure.FeedBack f : pr.feedbacks) {

            if (f.state == Types.Request_Done) {
                continue;
            }

            if (f.state == Types.Request_NotProccesse) {
                f.state = Types.Request_Failed;
                continue;
            }

            MedTeam m = getMedTeam(f.med_team_id);
            if (m != null) {
                for (int i = 0; i < m.patients.size(); i++) {
                    if (m.patients.get(i).patient_id == p.id) {
                        m.patients.remove(i);
                        break;
                    }
                }
                updateMedTeam(m);
            }
            f.state = Types.Request_Failed;
        }

    }

    private static void stopSmartFeeds(Patient p, GetProcedure.Procedure pr) {

        for (GetProcedure.SmartFeedBack f : pr.smart_feedbacks) {

            if (f.state == Types.Request_Done) {
                continue;
            }

            if (f.state == Types.Request_NotProccesse) {
                f.state = Types.Request_Failed;
                continue;
            }

            Session m = getSession(f.session_id);
            if (m != null) {
                for (int i = 0; i < m.patients.size(); i++) {
                    if (m.patients.get(i).patient_id == p.id) {
                        m.patients.remove(i);
                        break;
                    }
                }
                updateSession(m);
            }
            f.state = Types.Request_Failed;
        }

    }

    private static void stopVisits(Patient p, GetProcedure.Procedure pr) {

        for (GetProcedure.ServicesGroup f : pr.services) {

            if (f.state == Types.Request_Done) {
                continue;
            }

            if (f.state == Types.Request_NotProccesse) {
                f.state = Types.Request_Failed;
                continue;
            }

            if (f.appointment.state == Types.Appointment_Invoice_Not_paid) {
                if (f.appointment.room_id != 0) {
                    AppointmentRoom app = getAppointmentRoom(f.appointment.appointment_id, f.appointment.room_id);
                    for (int i = 0; i < app.temp_booked.size(); i++) {
                        if (app.temp_booked.get(i).patient_id == p.id) {
                            app.temp_booked.remove(i);
                            break;
                        }
                    }
                    updateAppointmentRoom(app, f.appointment.room_id);
                }
            } else if (f.appointment.state == Types.Appointment_Not_Booked) {

            } else if (f.appointment.state == Types.Appointment_Waiting_For_Date) {
                if (f.appointment.room_id != 0) {
                    AppointmentRoom app = getAppointmentRoom(f.appointment.appointment_id, f.appointment.room_id);
                    for (int i = 0; i < app.booked.size(); i++) {
                        if (app.booked.get(i).patient_id == p.id) {
                            app.booked.remove(i);
                            break;
                        }
                    }
                    updateAppointmentRoom(app, f.appointment.room_id);
                }

            } else if (f.appointment.state == Types.Appointment_Waiting_For_Enter_Line) {
                if (f.appointment.room_id != 0) {
                    AppointmentRoom app = getAppointmentRoom(f.appointment.appointment_id, f.appointment.room_id);
                    for (int i = 0; i < app.booked.size(); i++) {
                        if (app.booked.get(i).patient_id == p.id) {
                            app.booked.remove(i);
                            break;
                        }
                    }
                    updateAppointmentRoom(app, f.appointment.room_id);
                }

            } else if (f.appointment.state == Types.Appointment_Waiting_For_Enter) {
                if (f.appointment.room_id != 0) {
                    AppointmentRoom app = getAppointmentRoom(f.appointment.appointment_id, f.appointment.room_id);
                    for (int i = 0; i < app.Queue.size(); i++) {
                        if (app.Queue.get(i).patient_id == p.id) {
                            app.Queue.remove(i);
                            break;
                        }
                    }
                    updateAppointmentRoom(app, f.appointment.room_id);
                }
            }
            f.state = Types.Request_Failed;
        }

    }

    private static MedTeam getMedTeam(long id) throws FileNotFoundException, IOException {

        MedTeam t = null;
        check(PathsMine.InstalledMedTeam, id + "");
        RandomAccessFile file = new RandomAccessFile(PathsMine.InstalledMedTeam + id, "r");
        int size = file.readInt();
        byte[] date = new byte[size];
        file.read(date);
        String g = new String(date, "UTF-8");
        t = server.gson.fromJson(g, MedTeam.class);
        file.close();
        return t;
    }

    private static void updateMedTeam(MedTeam team) throws IOException {
        check(PathsMine.InstalledMedTeam, team.id + "");
        RandomAccessFile file = new RandomAccessFile(PathsMine.InstalledMedTeam + team.id, "rw");
        file.setLength(0);
        byte[] data = server.gson.toJson(team).getBytes("UTF-8");
        int size = data.length;
        file.writeInt(size);
        file.write(data);
        file.close();
    }

    private static Session getSession(long id) {

        try {
            Session s = null;
            File f = new File(PathsMine.SmartSystem + id);
            if (!f.exists()) {
                return null;
            }

            RandomAccessFile room_details = new RandomAccessFile(PathsMine.SmartSystem + id, "r");
            int size = room_details.readInt();
            byte[] date = new byte[size];
            room_details.read(date);
            String g = new String(date, "UTF-8");
            s = server.gson.fromJson(g, Session.class);
            room_details.close();
            return s;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    private static Session createSession(List<com.health.objects.GetAvailibleServices.Service> services) {

        try {

            RandomAccessFile list = new RandomAccessFile(PathsMine.SmartSystemList, "rw");
            long num = list.length() / Long.BYTES;
            ArrayList<Long> ids = new ArrayList<>((int) num);
            while (num > 0) {
                ids.add(list.readLong());
                num--;
            }
            long id = Util.generateID(ids);
            list.writeLong(id);
            list.close();

            Session s = new Session();
            s.patients = new ArrayList<>();
            s.service_ids = new ArrayList<>();
            s.id = id;
            for (int i = 0; i < services.size(); i++) {
                s.service_ids.add(services.get(i).service_id);
            }
            check(PathsMine.SmartSystem, "" + id);
            RandomAccessFile room_details = new RandomAccessFile(PathsMine.SmartSystem + id, "rw");
            byte[] data = server.gson.toJson(s).getBytes("UTF-8");
            int size = data.length;
            room_details.writeInt(size);
            room_details.write(data);
            room_details.close();

            return getSession(id);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    private static void updateSession(Session s) {
        try {
            RandomAccessFile room_details = new RandomAccessFile(PathsMine.SmartSystem + s.id, "rw");
            room_details.setLength(0);
            byte[] data = server.gson.toJson(s).getBytes("UTF-8");
            int size = data.length;
            room_details.writeInt(size);
            room_details.write(data);
            room_details.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private static AppointmentRoom getAppointmentRoom(long app_id, long room_id) {
        try {
            check(PathsMine.InstalledRooms + room_id + PathsMine.InstalledSchedules, "" + app_id);
            RandomAccessFile app_file = new RandomAccessFile(PathsMine.InstalledRooms + room_id + PathsMine.InstalledSchedules + app_id, "r");
            int size = app_file.readInt();
            byte[] date = new byte[size];
            app_file.read(date);
            String g = new String(date, "UTF-8");
            AppointmentRoom app = server.gson.fromJson(g, AppointmentRoom.class);
            app_file.close();
            return app;
        } catch (IOException ex) {
            Logger.getLogger(Views.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static void updateAppointmentRoom(AppointmentRoom se, long room_id) {
        try {
            check(PathsMine.InstalledRooms + room_id + PathsMine.InstalledSchedules, "" + se.id);
            RandomAccessFile app_file = new RandomAccessFile(PathsMine.InstalledRooms + room_id + PathsMine.InstalledSchedules + se.id, "rw");
            app_file.setLength(0);
            byte[] data = server.gson.toJson(se).getBytes("UTF-8");
            int size = data.length;
            app_file.writeInt(size);
            app_file.write(data);
            app_file.close();
        } catch (IOException ex) {
            Logger.getLogger(Views.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String handleStopCondtion(String json) {
        String res = null;
        try {

            com.health.objects.StopCondtion.Request request = server.gson.fromJson(json, com.health.objects.StopCondtion.Request.class);
            Patient p = getPatient(request.patient_id);
            com.health.objects.GetCondtion.Condtion c = Util.findCondtion(request.condtion_id, p);
            if (c == null) {
                return Strings.SUCCESS;
            }

            if (c.state == Types.State_Cond_Active) {
                c.date_deactivation = PathsMine.getCurrentTime();
                c.state = com.health.objects.Types.State_Cond_History;
                p.historyCondtions.add(c);
                p.activeCondtions.remove(c);

                com.health.objects.GetNotifications.Notification noti = new com.health.objects.GetNotifications.Notification();
                noti.type = com.health.objects.GetNotifications.Notification_Condtion_Removed;
                noti.condtion_id = c.condtion_id;
                noti.notification_id = Util.generateIDNotification(p.notifications);
                noti.date = PathsMine.getCurrentTime();
                noti.date_added = PathsMine.getCurrentTime();

                p.notifications.add(noti);
                p.lastTimeUpdated = PathsMine.getCurrentTime();
                updateLastTimeModified(p);
                savePatient(p);
            }
            res = Strings.SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();

        }

        return res;

    }

    public static String handleStartProcedure(String json) {
        String res = null;

        try {
            com.health.objects.StartProcedure.Request request = server.gson.fromJson(json, com.health.objects.StartProcedure.Request.class);
            Patient p = getPatient(request.patient_id);
            com.health.objects.GetProcedure.Procedure c = request.procedure;
            c.type = Types.Type_Procedure_Independent;
            c.date_activation = PathsMine.getCurrentTime();
            c.state = com.health.objects.Types.State_Proc_Active;

            ArrayList<GetProcedure.Procedure> proc = new ArrayList<>();
            proc.addAll(p.activeProcedures);
            proc.addAll(p.historyProcedures);

            c.proc_id = Util.generateIDProc(proc);

            c.remaining_days = c.activation_period;

            for (GetProcedure.FeedBack f : c.feedbacks) {
                f.state = com.health.objects.Types.Request_NotProccesse;
            }

            for (GetProcedure.SmartFeedBack f : c.smart_feedbacks) {
                f.state = com.health.objects.Types.Request_NotProccesse;
            }

            for (GetProcedure.ServicesGroup f : c.services) {
                f.state = com.health.objects.Types.Request_NotProccesse;
            }

            p.activeProcedures.add(c);

            boolean isActive = false;
            for (int i = 0; i < c.feedbacks.size(); i++) {

                if (c.feedbacks.get(i).order == 0) {
                    isActive = true;
                    c.feedbacks.get(i).state = Types.Request_InProgress;

                    MedTeam m = getMedTeam(c.feedbacks.get(i).med_team_id);

                    MedTeam.Patient mP = new MedTeam.Patient();
                    mP.patient_id = p.id;
                    mP.procedure_id = c.proc_id;
                    mP.request_id = c.feedbacks.get(i).req_id;
                    m.patients.add(mP);
                    updateMedTeam(m);
                }
            }

            for (int i = 0; i < c.smart_feedbacks.size(); i++) {
                if (c.smart_feedbacks.get(i).order == 0) {
                    isActive = true;
                    c.smart_feedbacks.get(i).state = Types.Request_InProgress;
                    Session m = getSession(c.smart_feedbacks.get(i).services, 0);

                    if (m == null) {
                        m = createSession(c.smart_feedbacks.get(i).services);
                    }

                    c.smart_feedbacks.get(i).session_id = m.id;

                    Session.Patient mP = new Session.Patient();
                    mP.patient_id = p.id;
                    mP.procedure_id = c.proc_id;
                    mP.request_id = c.smart_feedbacks.get(i).req_id;
                    m.patients.add(mP);
                    updateSession(m);

                }

            }

            for (int i = 0; i < c.services.size(); i++) {
                if (c.services.get(i).order == 0) {
                    isActive = true;
                    c.services.get(i).state = Types.Request_InProgress;
                    if (c.services.get(i).appointment == null) {
                        c.services.get(i).appointment = new GetProcedure.Appointment();
                    }
                    c.services.get(i).appointment.state = Types.Appointment_Invoice_Not_paid;
                }
            }

            if (!isActive) {
                com.health.objects.GetNotifications.Notification noti = new com.health.objects.GetNotifications.Notification();
                noti.type = com.health.objects.GetNotifications.Notification_Msg;
                noti.condtion_id = 0;
                noti.procedure_id = 0;
                noti.msg = c.note_patient;
                noti.notification_id = Util.generateIDNotification(p.notifications);
                noti.date = PathsMine.getCurrentTime();
                noti.date_added = PathsMine.getCurrentTime();

                p.notifications.add(noti);
                p.activeProcedures.remove(c);
            } else {
                com.health.objects.GetNotifications.Notification noti = new com.health.objects.GetNotifications.Notification();
                noti.type = com.health.objects.GetNotifications.Notification_Visit_Created;
                noti.condtion_id = c.condtion_id;
                noti.procedure_id = c.proc_id;
                noti.notification_id = Util.generateIDNotification(p.notifications);
                noti.date = PathsMine.getCurrentTime();
                noti.date_added = PathsMine.getCurrentTime();

                p.notifications.add(noti);

            }
            p.lastTimeUpdated = PathsMine.getCurrentTime();
            updateLastTimeModified(p);
            savePatient(p);

            res = Strings.SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;

    }

    public static String handleStartCondtion(String json) {
        String res = null;

        try {

            com.health.objects.StartCondtion.Request request = server.gson.fromJson(json, com.health.objects.StartCondtion.Request.class
            );
            Patient p = getPatient(request.patient_id);
            com.health.objects.GetCondtion.Condtion c = request.condtion;

            if (c.medication_ordinary == null) {
                c.medication_ordinary = new ArrayList<>();
            }
            if (c.medications_routine == null) {
                c.medications_routine = new ArrayList<>();
            }
            if (c.activities == null) {
                c.activities = new ArrayList<>();
            }
            if (c.procedures == null) {
                c.procedures = new ArrayList<>();
            }
            if (c.attached_procedures == null) {
                c.attached_procedures = new ArrayList<>();
            }

            for (GetProcedure.Procedure pro : c.procedures) {
                if (pro.feedbacks == null) {
                    pro.feedbacks = new ArrayList<>();
                }
                if (pro.smart_feedbacks == null) {
                    pro.smart_feedbacks = new ArrayList<>();
                }

                if (pro.services == null) {
                    pro.services = new ArrayList<>();
                }
            }

            for (GetProcedure.Procedure pro : c.attached_procedures) {
                if (pro.feedbacks == null) {
                    pro.feedbacks = new ArrayList<>();
                }
                if (pro.smart_feedbacks == null) {
                    pro.smart_feedbacks = new ArrayList<>();
                }

                if (pro.services == null) {
                    pro.services = new ArrayList<>();
                }
            }

            List<com.health.objects.GetCondtion.Condtion> f = new ArrayList<>();
            f.addAll(p.activeCondtions);
            f.addAll(p.historyCondtions);
            c.condtion_id = Util.generateIDCondtion(f);

            c.date_activation = PathsMine.getCurrentTime();
            c.remaining_days = c.activation_period;
            c.state = com.health.objects.Types.State_Cond_Active;
            p.activeCondtions.add(c);
            com.health.objects.GetNotifications.Notification noti = new com.health.objects.GetNotifications.Notification();
            noti.type = com.health.objects.GetNotifications.Notification_Condtion_Added;
            noti.condtion_id = c.condtion_id;
            noti.notification_id = Util.generateIDNotification(p.notifications);
            noti.date = PathsMine.getCurrentTime();
            noti.date_added = PathsMine.getCurrentTime();
            p.notifications.add(noti);
            updateLastTimeModified(p);
            updatePatient(p);
            com.health.objects.StartCondtion.Response ress = new com.health.objects.StartCondtion.Response();
            ress.id = c.condtion_id;
            res = server.gson.toJson(ress);
            //System.out.println("Startting Condtion Finished ");

        } catch (Exception e) {
            e.printStackTrace();

        }

        return res;

    }

    private static Patient getPatient(long id) throws FileNotFoundException, IOException {

        updatePatient(id);
        check(PathsMine.InstalledPatients, "" + id);

        RandomAccessFile file = new RandomAccessFile(PathsMine.InstalledPatients + id, "r");
        int size = file.readInt();
        byte[] date = new byte[size];
        file.read(date);
        String g = new String(date, "UTF-8");
        String json = g;
        file.close();

        Patient patient = server.gson.fromJson(json, Patient.class);
        return patient;
    }

    public static String handleUpdateProcedure(String json) {
        String res = null;
        try {
            com.health.objects.UpdateProcedure.Request request = server.gson.fromJson(json, com.health.objects.UpdateProcedure.Request.class);
            Patient p = getPatient(request.patient_id);
            com.health.objects.GetProcedure.Procedure c = request.procedure;
            if (c == null) {
                return Strings.SUCCESS;
            }

            com.health.objects.GetProcedure.Procedure current = Util.findProc(c.proc_id, p);

            int order = 0;
            boolean found = false;
            for (GetProcedure.FeedBack f : current.feedbacks) {
                if (f.state == Types.Request_InProgress) {
                    f.state = Types.Request_Done;
                    order = f.order;
                    found = true;
                    break;
                }
            }
            if (!found) {
                for (GetProcedure.SmartFeedBack f : current.smart_feedbacks) {
                    if (f.state == Types.Request_InProgress) {
                        f.state = Types.Request_Done;
                        found = true;
                        order = f.order;
                        break;
                    }
                }
            }

            for (int i = 0; i < current.feedbacks.size(); i++) {
                if (current.feedbacks.get(i).state != Types.Request_Done) {

                    current.feedbacks.remove(i);
                    i--;
                }
            }
            for (int i = 0; i < current.smart_feedbacks.size(); i++) {
                if (current.smart_feedbacks.get(i).state != Types.Request_Done) {

                    current.smart_feedbacks.remove(i);
                    i--;
                }
            }
            for (int i = 0; i < current.services.size(); i++) {
                if (current.services.get(i).state != Types.Request_Done) {
                    current.services.remove(i);
                    i--;
                }
            }

            boolean isActive = false;
            for (int i = 0; i < c.feedbacks.size(); i++) {

                c.feedbacks.get(i).state = Types.Request_NotProccesse;
                if (c.feedbacks.get(i).order == 0) {
                    isActive = true;
                    c.feedbacks.get(i).state = Types.Request_InProgress;
                    MedTeam m = getMedTeam(c.feedbacks.get(i).med_team_id);
                    MedTeam.Patient mP = new MedTeam.Patient();
                    mP.patient_id = p.id;
                    mP.procedure_id = c.proc_id;
                    mP.request_id = c.feedbacks.get(i).req_id;
                    m.patients.add(mP);
                    updateMedTeam(m);
                }

                c.feedbacks.get(i).order += order + 1;
                current.feedbacks.add(c.feedbacks.get(i));
            }

            for (int i = 0; i < c.smart_feedbacks.size(); i++) {
                c.smart_feedbacks.get(i).state = Types.Request_NotProccesse;

                if (c.smart_feedbacks.get(i).order == 0) {
                    isActive = true;

                    c.smart_feedbacks.get(i).state = Types.Request_InProgress;
                    Session m = getSession(c.smart_feedbacks.get(i).services, 0);

                    if (m == null) {
                        m = createSession(c.smart_feedbacks.get(i).services);
                    }
                    c.smart_feedbacks.get(i).session_id = m.id;

                    Session.Patient mP = new Session.Patient();
                    mP.patient_id = p.id;
                    mP.procedure_id = c.proc_id;
                    mP.request_id = c.smart_feedbacks.get(i).req_id;
                    m.patients.add(mP);
                    updateSession(m);
                }
                c.smart_feedbacks.get(i).order += order + 1;
                current.smart_feedbacks.add(c.smart_feedbacks.get(i));
            }
            for (int i = 0; i < c.services.size(); i++) {
                c.services.get(i).state = Types.Request_NotProccesse;
                if (c.services.get(i).order == 0) {
                    isActive = true;
                    c.services.get(i).state = Types.Request_InProgress;
                    if (c.services.get(i).appointment == null) {
                        c.services.get(i).appointment = new GetProcedure.Appointment();
                    }

                    c.services.get(i).appointment.state = Types.Appointment_Invoice_Not_paid;
                }
                c.services.get(i).order += order + 1;
                current.services.add(c.services.get(i));
            }

            if (!isActive) {
                com.health.objects.GetNotifications.Notification noti = new com.health.objects.GetNotifications.Notification();
                noti.type = com.health.objects.GetNotifications.Notification_Visit_Done;
                noti.condtion_id = c.condtion_id;
                noti.notification_id = Util.generateIDNotification(p.notifications);
                noti.date = PathsMine.getCurrentTime();
                noti.date_added = PathsMine.getCurrentTime();

                p.notifications.add(noti);
                p.activeProcedures.remove(current);
                current.state = Types.State_Proc_History;
                p.historyProcedures.add(current);
            }
            p.lastTimeUpdated = PathsMine.getCurrentTime();
            updateLastTimeModified(p);
            savePatient(p);
            res = Strings.SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();

        }

        return res;

    }

    public static String handleInstallServices(String json) {

        String res = null;

        try {

            com.health.objects.InstallServices.Request request = server.gson.fromJson(json, com.health.objects.InstallServices.Request.class);

            RandomAccessFile list = new RandomAccessFile(PathsMine.InstalledServicePathList, "rw");
            long num = list.length() / Long.BYTES;
            ArrayList<Long> ids = new ArrayList<>((int) num);

            while (num > 0) {
                ids.add(list.readLong());
                num--;
            }
            ArrayList<Long> response = new ArrayList<>();

            for (int i = 0; i < request.services.size(); i++) {
                long id = request.services.get(i).service_id;
                if (server.Signature == request.services.get(i).service_id) {
                    id = Util.generateID(ids);
                    list.writeLong(id);
                }
                request.services.get(i).service_id = id;
                response.add(id);

                String service = server.gson.toJson(request.services.get(i));
                check(PathsMine.InstalledServicePath, "" + id);
                RandomAccessFile service_file = new RandomAccessFile(PathsMine.InstalledServicePath + id, "rw");
                service_file.setLength(0);

                byte[] data = service.getBytes("UTF-8");
                int size = data.length;
                service_file.writeInt(size);
                service_file.write(data);
                service_file.close();
            }
            list.close();
            res = server.gson.toJson(response, (new TypeToken<ArrayList<Long>>() {
            }).getType());

        } catch (Exception e) {
            e.printStackTrace();

        }
        return res;

    }

    public static String handleInstallInsurances(String json) {

        String res = null;
        try {

            com.health.objects.InstallInsurances.Request request = server.gson.fromJson(json, com.health.objects.InstallInsurances.Request.class);

            RandomAccessFile list = new RandomAccessFile(PathsMine.InstalledInsurancesList, "rw");

            long num = list.length() / Long.BYTES;
            ArrayList<Long> ids = new ArrayList<>((int) num);

            while (num > 0) {
                ids.add(list.readLong());
                num--;
            }
            ArrayList<Long> response = new ArrayList<Long>();
            for (int i = 0; i < request.insurances_paths.size(); i++) {
                long id = request.insurances_paths.get(i).insurance_id;
                if (request.insurances_paths.get(i).insurance_id == server.Signature) {
                    id = Util.generateID(ids);
                    list.writeLong(id);
                }
                request.insurances_paths.get(i).insurance_id = id;
                response.add(id);
                check(PathsMine.InstalledInsurances, "" + id);
                RandomAccessFile insurance_file = new RandomAccessFile(PathsMine.InstalledInsurances + id, "rw");
                insurance_file.setLength(0);
                byte[] data = server.gson.toJson(request.insurances_paths.get(i)).getBytes("UTF-8");
                int size = data.length;
                insurance_file.writeInt(size);
                insurance_file.write(data);
                insurance_file.close();
            }
            list.close();

            res = server.gson.toJson(response, (new TypeToken<ArrayList<Long>>() {
            }).getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;

    }

    public static String handleInstallMedications(String json) {
        String res = null;

        try {

            com.health.objects.InstallMedication.Request request = server.gson.fromJson(json, com.health.objects.InstallMedication.Request.class);

            RandomAccessFile list = new RandomAccessFile(PathsMine.InstalledMedicationsList, "rw");

            long num = list.length() / Long.BYTES;

            ArrayList<Long> ids = new ArrayList<>((int) num);

            while (num > 0) {
                ids.add(list.readLong());
                num--;
            }
            ArrayList<Long> respose = new ArrayList<>();

            for (int i = 0; i < request.medications.size(); i++) {
                long id = request.medications.get(i).medication_id;
                if (request.medications.get(i).medication_id == server.Signature) {
                    id = Util.generateID(ids);
                    list.writeLong(id);
                }
                respose.add(id);
                request.medications.get(i).medication_id = id;

                check(PathsMine.InstalledMedications, "" + id);
                RandomAccessFile medication_file = new RandomAccessFile(PathsMine.InstalledMedications + id, "rw");
                medication_file.setLength(0);
                byte[] data = server.gson.toJson(request.medications.get(i)).getBytes("UTF-8");
                int size = data.length;
                medication_file.writeInt(size);
                medication_file.write(data);
                medication_file.close();
            }
            list.close();
            res = server.gson.toJson(respose, (new TypeToken<ArrayList<Long>>() {
            }).getType());

        } catch (Exception e) {
            e.printStackTrace();

        }
        return res;
    }

    public static String handleInstallRooms(String json) {
        String res = null;
        try {
            com.health.objects.InstallRooms.Request request = server.gson.fromJson(json, com.health.objects.InstallRooms.Request.class
            );
            RandomAccessFile list = new RandomAccessFile(PathsMine.InstalledRoomsList, "rw");
            long num = list.length() / Long.BYTES;
            ArrayList<Long> ids = new ArrayList<>((int) num);
            while (num > 0) {
                ids.add(list.readLong());
                num--;
            }
            ArrayList<Long> respose = new ArrayList<>();
            for (int i = 0; i < request.rooms.size(); i++) {
                long id = request.rooms.get(i).room_id;
                if (request.rooms.get(i).room_id == server.Signature) {
                    id = Util.generateID(ids);
                    list.writeLong(id);
                }
                respose.add(id);
                com.health.objects.GetAvailibleRooms.Room room = request.rooms.get(i);
                room.room_id = id;
                File dir = new File(PathsMine.InstalledRooms + id);
                dir.mkdirs();
                dir = new File(dir, PathsMine.InstalledSchedules);
                dir.mkdirs();
                check(PathsMine.InstalledRooms + id, "/details.i");
                RandomAccessFile room_details = new RandomAccessFile(PathsMine.InstalledRooms + id + "/details.i", "rw");
                room_details.setLength(0);
                byte[] data = server.gson.toJson(room).getBytes("UTF-8");
                int size = data.length;
                room_details.writeInt(size);
                room_details.write(data);
                room_details.close();
            }
            list.close();
            res = server.gson.toJson(respose, (new TypeToken<ArrayList<Long>>() {
            }).getType());

        } catch (Exception e) {
            e.printStackTrace();

        }
        return res;
    }

    public static String handleInstallCondtions(String json) {
        String res = null;

        try {

            com.health.objects.InstallCondtionsTemplate.Request request = server.gson.fromJson(json, com.health.objects.InstallCondtionsTemplate.Request.class
            );

            RandomAccessFile list = new RandomAccessFile(PathsMine.InstalledCondtionsList, "rw");
            long num = list.length() / Long.BYTES;
            ArrayList<Long> ids = new ArrayList<>((int) num);
            while (num > 0) {
                ids.add(list.readLong());
                num--;
            }
            for (int i = 0; i < request.condtions.size(); i++) {
                long id = Util.generateID(ids);
                list.writeLong(id);
                com.health.objects.GetCondtion.Condtion cond = request.condtions.get(i);
                cond.condtion_id = id; // will be overwritten
                check(PathsMine.InstalledCondtions, "" + id);
                RandomAccessFile condtion_file = new RandomAccessFile(PathsMine.InstalledCondtions + id, "rw");
                byte[] data = server.gson.toJson(cond).getBytes("UTF-8");
                int size = data.length;
                condtion_file.writeInt(size);
                condtion_file.write(data);
                condtion_file.close();
            }
            list.close();
            res = Strings.SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();

        }

        return res;

    }

    public static String handleInstallProcedures(String json) {
        String res = null;
        try {

            com.health.objects.InstallProceduresTemplates.Request request = server.gson.fromJson(json, com.health.objects.InstallProceduresTemplates.Request.class);

            RandomAccessFile list = new RandomAccessFile(PathsMine.InstalledProcList, "rw");
            long num = list.length() / Long.BYTES;
            ArrayList<Long> ids = new ArrayList<>((int) num);
            while (num > 0) {
                ids.add(list.readLong());
                num--;
            }
            for (int i = 0; i < request.procedures.size(); i++) {
                long id = Util.generateID(ids);
                list.writeLong(id);
                com.health.objects.GetProcedure.Procedure proc = request.procedures.get(i);
                proc.proc_id = id; // will be overwritten
                check(PathsMine.InstalledProc, "" + id);

                RandomAccessFile proc_file = new RandomAccessFile(PathsMine.InstalledProc + id, "rw");
                byte[] data = server.gson.toJson(proc).getBytes("UTF-8");
                int size = data.length;
                proc_file.writeInt(size);
                proc_file.write(data);
                proc_file.close();
            }
            list.close();
            res = Strings.SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;

    }

    private static boolean isAffected(AppointmentRoom newApp, AppointmentRoom oldApp) {
        if (!newApp.date.equals(oldApp.date)) {
            return false;
        }
        Period old = new Period("1999-1-1-" + oldApp.from, "1999-1-1-" + oldApp.to);
        Period New = new Period("1999-1-1-" + newApp.from, "1999-1-1-" + newApp.to);
        if (!newApp.from.equals(oldApp.from) || old.getPeriodInMinutes() > New.getPeriodInMinutes() || newApp.capacity < (oldApp.Queue.size() + oldApp.booked.size() + oldApp.temp_booked.size())) {
            return true;
        }
        return false;
    }

    private static boolean isEqual(AppointmentRoom newApp, AppointmentRoom oldApp) {
        return newApp.date.equals(oldApp.date) && newApp.from.equals(oldApp.from) && newApp.to.equals(oldApp.to) && newApp.capacity >= (oldApp.Queue.size() + oldApp.temp_booked.size() + oldApp.booked.size());
    }

    private static AppointmentRoom translate(GetRoomsAppointments.Appointment app) {
        AppointmentRoom ap = new AppointmentRoom();
        ap.booked = new ArrayList<>();
        ap.temp_booked = new ArrayList<>();
        ap.Queue = new ArrayList<>();
        ap.date = app.date;
        ap.from = app.from;
        ap.to = app.to;
        ap.capacity = app.capacity;
        return ap;
    }

    public static String handleInstallSchedules(String json) {
        String res = null;

        try {
            com.health.objects.InstallSchedules.Request request = server.gson.fromJson(json, com.health.objects.InstallSchedules.Request.class
            );
            List<AppointmentRoom> apps = getFreshAppointments(request.room_id);
            List<AppointmentRoom> r = new ArrayList<>();

            List<AppointmentRoom> gen = new ArrayList<>();
            gen.addAll(apps);
            for (int i = 0; i < request.schedule.size(); i++) {
                AppointmentRoom ap = translate(request.schedule.get(i));
                if (isGone(ap)) {
                    continue;
                }

                boolean skip = false;
                for (int u = 0; u < apps.size(); u++) {
                    AppointmentRoom apr = apps.get(u);
                    if (isAffected(ap, apr) && !isEqual(ap, apr)) {
                        cancelAppointMent(apr, request.room_id);
                        break;
                    } else if (isEqual(ap, apr)) { //equivalent to isAffected(ap, apr) && isEqual(ap, apr)
                        skip = true;
                        break;
                    }
                }

                if (!skip) {
                    ap.id = Util.generateIDRoom(gen);
                    r.add(ap);
                    gen.add(ap);
                }
            }

            apps = getFreshAppointments(request.room_id);

            for (AppointmentRoom ap : r) {
                boolean added = false;
                for (int i = 0; i < apps.size(); i++) {
                    if (Util.isAfter(apps.get(i).date, ap.date)) {
                        apps.add(i, ap);
                        check(PathsMine.InstalledRooms + request.room_id + PathsMine.InstalledSchedules, "" + ap.id);
                        RandomAccessFile app_file = new RandomAccessFile(PathsMine.InstalledRooms + request.room_id + PathsMine.InstalledSchedules + ap.id, "rw");
                        byte[] data = server.gson.toJson(ap).getBytes("UTF-8");
                        int size = data.length;
                        app_file.writeInt(size);
                        app_file.write(data);
                        app_file.close();
                        added = true;
                        break;
                    }
                }
                if (!added) {
                    apps.add(ap);
                    check(PathsMine.InstalledRooms + request.room_id + PathsMine.InstalledSchedules, "" + ap.id);
                    RandomAccessFile app_file = new RandomAccessFile(PathsMine.InstalledRooms + request.room_id + PathsMine.InstalledSchedules + ap.id, "rw");
                    byte[] data = server.gson.toJson(ap).getBytes("UTF-8");
                    int size = data.length;
                    app_file.writeInt(size);
                    app_file.write(data);
                    app_file.close();
                }
            }

            File f = new File(PathsMine.InstalledRooms + request.room_id + PathsMine.InstalledSchedulesList);
            f.delete();
            check(PathsMine.InstalledRooms + request.room_id, PathsMine.InstalledSchedulesList);
            RandomAccessFile list = new RandomAccessFile(PathsMine.InstalledRooms + request.room_id + PathsMine.InstalledSchedulesList, "rw");
            for (AppointmentRoom app : apps) {
                list.writeLong(app.id);
            }
            list.close();
            res = Strings.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    private static List<AppointmentRoom> getRangeAppointments(long room_id, String date, int size) throws Exception {

        List<AppointmentRoom> apps = getFreshAppointments(room_id);
        int idx = 0;

        String[] split = date.split("-");
        date = split[0] + "-" + split[1] + "-" + split[2] + "-0-0";

        for (int i = 0; i < apps.size(); i++) {
            if (Util.isAfter(apps.get(i).date, date) || apps.get(i).date.equals(date)) {
                idx = i;
                break;
            }
        }

        List<AppointmentRoom> ap = new ArrayList<>();

        int y = (apps.size() - idx) > size ? size : apps.size() - idx;
        for (int u = 0; u < y; u++) {
            ap.add(apps.get(idx + u));

        }
        return ap;
    }

    private static List<AppointmentRoom> getFreshAppointments(long room_id) throws Exception {

        List<AppointmentRoom> apps = new ArrayList<>();
        check(PathsMine.InstalledRooms + room_id, "" + PathsMine.InstalledSchedulesList);
        RandomAccessFile list = new RandomAccessFile(PathsMine.InstalledRooms + room_id + PathsMine.InstalledSchedulesList, "r");
        long num = list.length() / Long.BYTES;
        ArrayList<Long> ids = new ArrayList<>((int) num);
        while (num > 0) {
            ids.add(list.readLong());
            num--;
        }
        list.close();

        for (int i = 0; i < ids.size(); i++) {
            check(PathsMine.InstalledRooms + room_id + PathsMine.InstalledSchedules, "" + ids.get(i));
            RandomAccessFile app_file = new RandomAccessFile(PathsMine.InstalledRooms + room_id + PathsMine.InstalledSchedules + ids.get(i), "r");
            int size = app_file.readInt();
            byte[] date = new byte[size];
            app_file.read(date);
            String g = new String(date, "UTF-8");
            AppointmentRoom app = server.gson.fromJson(g, AppointmentRoom.class
            );
            apps.add(app);
            app_file.close();
        }

        for (AppointmentRoom app : apps) {
            for (int i = 0; i < app.temp_booked.size(); i++) {
                updatePatient(app.temp_booked.get(i).patient_id);
            }
            for (int i = 0; i < app.booked.size(); i++) {
                updatePatient(app.booked.get(i).patient_id);
            }
            for (int i = 0; i < app.Queue.size(); i++) {
                updatePatient(app.Queue.get(i).patient_id);
            }
        }
        apps.clear();
        check(PathsMine.InstalledRooms + room_id, PathsMine.InstalledSchedulesList);
        list = new RandomAccessFile(PathsMine.InstalledRooms + room_id + PathsMine.InstalledSchedulesList, "r");
        num = list.length() / Long.BYTES;
        ids.clear();
        while (num > 0) {
            ids.add(list.readLong());
            num--;
        }
        list.close();

        for (int i = 0; i < ids.size(); i++) {
            check(PathsMine.InstalledRooms + room_id + PathsMine.InstalledSchedules, "" + ids.get(i));

            RandomAccessFile app_file = new RandomAccessFile(PathsMine.InstalledRooms + room_id + PathsMine.InstalledSchedules + ids.get(i), "r");
            int size = app_file.readInt();
            byte[] date = new byte[size];
            app_file.read(date);
            String g = new String(date, "UTF-8");
            AppointmentRoom app = server.gson.fromJson(g, AppointmentRoom.class
            );
            apps.add(app);
            app_file.close();
        }

        for (int y = 0; y < apps.size(); y++) {

            if (isGone(apps.get(y))) {
                File f = new File(PathsMine.InstalledRooms + room_id + PathsMine.InstalledSchedules + ids.get(y));
                f.delete();
                apps.remove(y);
                y--;
            }
        }

        File f = new File(PathsMine.InstalledRooms + room_id + PathsMine.InstalledSchedulesList);
        f.delete();
        for (int i = 0; i < apps.size(); i++) {
            for (int x = i + 1; x < apps.size(); x++) {
                if (Util.isAfter(apps.get(i).date, apps.get(x).date)) {
                    AppointmentRoom tmp = apps.get(i);
                    apps.set(i, apps.get(x));
                    apps.set(x, tmp);
                }
            }
        }
        check(PathsMine.InstalledRooms + room_id, "" + PathsMine.InstalledSchedulesList);
        list = new RandomAccessFile(PathsMine.InstalledRooms + room_id + PathsMine.InstalledSchedulesList, "rw");
        for (AppointmentRoom app : apps) {
            list.writeLong(app.id);
        }
        list.close();

        return apps;
    }

    private static boolean isGone(AppointmentRoom app) {

        /*
        String da = PathsMine.getCurrentTime();
        String split[] = da.split("-");
        String ui = split[0] + "-" + split[1] + "-" + split[2] + "-0-0";
        if (app.date.equals(ui)) {
            //System.out.println("Yes");
            return false;
        }
         */
        int last = app.date.lastIndexOf("-");
        last = app.date.lastIndexOf("-", last + 1);
        if ((Util.isAfter(PathsMine.getCurrentTime(), app.date.substring(0, last + 1) + app.to))) {
            return true;
        } else {
            return false;
        }
    }

    public static String handleInstallPatient(String json) {
        String res = null;

        try {
            com.health.objects.InstallPatientsAccounts.Request request = server.gson.fromJson(json, com.health.objects.InstallPatientsAccounts.Request.class
            );

            for (int i = 0; i < request.patients.size(); i++) {
                com.health.objects.GetPersonalData.PersonalData data = request.patients.get(i);
                Patient patient = new Patient();
                patient.activeCondtions = new ArrayList<>();
                patient.activeProcedures = new ArrayList<>();
                patient.historyCondtions = new ArrayList<>();
                patient.historyProcedures = new ArrayList<>();
                patient.notifications = new ArrayList<>();
                patient.personalData = data;
                patient.id = request.ids.get(i);
                patient.Pass = request.passwords.get(i);
                if (data.insurance_id != 0) {
                    RandomAccessFile list = new RandomAccessFile(PathsMine.InstalledInsurancesList, "r");

                    long num = list.length() / Long.BYTES;
                    ArrayList<Long> ids = new ArrayList<>((int) num);
                    while (num > 0) {
                        ids.add(list.readLong());
                        num--;
                    }
                    list.close();

                    for (int x = 0; x < ids.size(); x++) {

                        if (ids.get(x) == data.insurance_id) {
                            check(PathsMine.InstalledInsurances, "" + ids.get(x));

                            RandomAccessFile insurance_file = new RandomAccessFile(PathsMine.InstalledInsurances + ids.get(x), "r");
                            int size = insurance_file.readInt();
                            byte[] date = new byte[size];
                            insurance_file.read(date);
                            String g = new String(date, "UTF-8");
                            data.insurance_path = server.gson.fromJson(g, GetAvailibleInsurances.Insurance.class).insurance_path;
                            insurance_file.close();
                            break;
                        }
                    }

                    patient.personalData.insurance_path = data.insurance_path;
                }

                patient.lastTimeUpdated = PathsMine.getCurrentTime();
                updateLastTimeModified(patient);
                check(PathsMine.InstalledPatients, "" + patient.id);
                RandomAccessFile file = new RandomAccessFile(PathsMine.InstalledPatients + patient.id, "rw");
                byte[] datad = server.gson.toJson(patient).getBytes("UTF-8");
                int size = datad.length;
                file.writeInt(size);
                file.write(datad);
                file.close();
                file = new RandomAccessFile(PathsMine.InstalledPatientsList, "rw");
                int u = (int) (file.length() / Long.BYTES);
                List<Long> isd = new ArrayList<>(u);
                while (u > 0) {
                    isd.add(file.readLong());
                    u--;
                }
                if (!isd.contains(patient.id)) {
                    file.writeLong(patient.id);
                }
                file.close();

            }

            res = Strings.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();

        }

        return res;

    }

    public static String handleInstallMeDTeam(String json) {
        String res = null;

        try {
            com.health.objects.InstallMedTeamAccounts.Request request = server.gson.fromJson(json, com.health.objects.InstallMedTeamAccounts.Request.class
            );

            for (int i = 0; i < request.med_team.size(); i++) {
                com.health.objects.InstallMedTeamAccounts.MedTeam data = request.med_team.get(i);

                MedTeam t = new MedTeam();
                t.id = request.ids.get(i);
                t.name = data.name;
                t.phone = data.phone;
                t.pass = data.pass;
                t.patients = new ArrayList<>();
                check(PathsMine.InstalledMedTeam, "" + t.id);

                RandomAccessFile file = new RandomAccessFile(PathsMine.InstalledMedTeam + t.id, "rw");
                byte[] datad = server.gson.toJson(t).getBytes("UTF-8");
                int size = datad.length;
                file.writeInt(size);
                file.write(datad);
                file.close();

                file = new RandomAccessFile(PathsMine.InstalledMedTeamList, "rw");
                file.seek(file.length());
                file.writeLong(t.id);
                file.close();

            }

            res = Strings.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();

        }

        return res;

    }

    public static String handleGetAvailibleCondtions(String json) {
        String res = null;

        try {

            RandomAccessFile list = new RandomAccessFile(PathsMine.InstalledCondtionsList, "r");
            long num = list.length() / Long.BYTES;
            ArrayList<Long> ids = new ArrayList<>((int) num);
            while (num > 0) {
                ids.add(list.readLong());
                num--;
            }
            list.close();

            List<GetCondtion.Condtion> condtions = new ArrayList<>();
            for (int i = 0; i < ids.size(); i++) {
                check(PathsMine.InstalledCondtions, "" + ids.get(i));
                RandomAccessFile condtion_file = new RandomAccessFile(PathsMine.InstalledCondtions + ids.get(i), "rw");
                int size = condtion_file.readInt();
                byte[] date = new byte[size];
                condtion_file.read(date);
                String g = new String(date, "UTF-8");
                condtions.add(server.gson.fromJson(g, GetCondtion.Condtion.class));
                condtion_file.close();
            }

            com.health.objects.GetAvailibleCondtions.Condtions cp = new com.health.objects.GetAvailibleCondtions.Condtions();
            cp.condtions = condtions;

            res = server.gson.toJson(cp);

        } catch (Exception e) {
            e.printStackTrace();

        }

        return res;

    }

    private static void translateProc(GetProcedure.Procedure pr, long patient_id, int to) {

        String name = TranslateHandler.translate(pr.name, -1, to, patient_id);
        String note_p = TranslateHandler.translate(pr.note_patient, -1, to, patient_id);
        String obj = TranslateHandler.translate(pr.objective, -1, to, patient_id);;
        pr.name = name;
        pr.note_patient = note_p;
        pr.objective = obj;

        for (int i = 0; i < pr.services.size(); i++) {
            for (int x = 0; x < pr.services.get(i).services.size(); x++) {
                String path = TranslateHandler.translate(pr.services.get(i).services.get(x).service_path, -1, to, -1999);
                pr.services.get(i).services.get(x).service_path = path;
                for (int y = 0; y < pr.services.get(i).services.get(x).insurances.size(); y++) {
                    String inPath = TranslateHandler.translate(pr.services.get(i).services.get(x).insurances.get(y).insurance_path, -1, to, -1999);
                    pr.services.get(i).services.get(x).insurances.get(y).insurance_path = inPath;
                }
            }
            for (int x = 0; x < pr.services.get(i).rooms.size(); x++) {

                String rpath = TranslateHandler.translate(pr.services.get(i).rooms.get(x).room_path, -1, to, -1999);
                pr.services.get(i).rooms.get(x).room_path = rpath;
            }

            if (pr.services.get(i).appointment != null && pr.services.get(i).appointment.appointment_id != 0) {
                pr.services.get(i).appointment.room_path = TranslateHandler.translate(pr.services.get(i).appointment.room_path, -1, to, -1999);
            }
        }
    }

    private static void translateCondtion(GetCondtion.Condtion c, long patient_id, int to) {

        String name = TranslateHandler.translate(c.name, -1, to, patient_id);
        String note_p = TranslateHandler.translate(c.note_patient, -1, to, patient_id);
        String desc = TranslateHandler.translate(c.desc, -1, to, patient_id);;

        c.name = name;
        c.note_patient = note_p;
        c.desc = desc;

        for (int i = 0; i < c.procedures.size(); i++) {
            translateProc(c.procedures.get(i), patient_id, to);
        }
        for (int i = 0; i < c.attached_procedures.size(); i++) {
            translateProc(c.attached_procedures.get(i), patient_id, to);
        }

        for (int i = 0; i < c.medication_ordinary.size(); i++) {

            String note_pM = TranslateHandler.translate(c.medication_ordinary.get(i).note_patient, -1, to, patient_id);
            String dosage = TranslateHandler.translate(c.medication_ordinary.get(i).dosage_amount, -1, to, patient_id);
            String nameM = TranslateHandler.translate(c.medication_ordinary.get(i).medication.medication_name, -1, to, -1999);
            String ind = TranslateHandler.translate(c.medication_ordinary.get(i).indications, -1, to, patient_id);
            String route = TranslateHandler.translate(c.medication_ordinary.get(i).medication.route_type, -1, to, -1999);
            c.medication_ordinary.get(i).note_patient = note_pM;
            c.medication_ordinary.get(i).dosage_amount = dosage;
            c.medication_ordinary.get(i).medication.medication_name = nameM;
            c.medication_ordinary.get(i).indications = ind;
            c.medication_ordinary.get(i).medication.route_type = route;
            for (int x = 0; x < c.medication_ordinary.get(i).medication.insurances.size(); x++) {
                String inPath = TranslateHandler.translate(c.medication_ordinary.get(i).medication.insurances.get(x).insurance_path, -1, to, -1999);
                c.medication_ordinary.get(i).medication.insurances.get(x).insurance_path = inPath;
            }
        }

        for (int i = 0; i < c.medications_routine.size(); i++) {

            String note_pM = TranslateHandler.translate(c.medications_routine.get(i).note_patient, -1, to, patient_id);
            String dosage = TranslateHandler.translate(c.medications_routine.get(i).dosage_amount, -1, to, patient_id);
            String nameM = TranslateHandler.translate(c.medications_routine.get(i).medication.medication_name, -1, to, -1999);
            String route = TranslateHandler.translate(c.medications_routine.get(i).medication.route_type, -1, to, -1999);
            c.medications_routine.get(i).note_patient = note_pM;
            c.medications_routine.get(i).dosage_amount = dosage;
            c.medications_routine.get(i).medication.medication_name = nameM;
            c.medications_routine.get(i).medication.route_type = route;
            for (int x = 0; x < c.medications_routine.get(i).medication.insurances.size(); x++) {
                String inPath = TranslateHandler.translate(c.medications_routine.get(i).medication.insurances.get(x).insurance_path, -1, to, -1999);
                c.medications_routine.get(i).medication.insurances.get(x).insurance_path = inPath;

            }
        }

        for (int i = 0; i < c.activities.size(); i++) {
            String descA = TranslateHandler.translate(c.activities.get(i).description, -1, to, patient_id);
            String obj = TranslateHandler.translate(c.activities.get(i).objective, -1, to, patient_id);
            c.activities.get(i).description = descA;
            c.activities.get(i).objective = obj;
        }
    }

    private static void translateNotification(GetNotifications.Notification noti, long patient_id, int to) {

        if (noti.msg != null) {
            noti.msg = TranslateHandler.translate(noti.msg, -1, to, patient_id);
        }
    }

    public static String handleGetAvailibleProcedures(String json) {

        String res = null;

        try {

            //System.out.println("GetAvailible PRoc 1");
            com.health.objects.GetAvailibleProcedure.Request req = server.gson.fromJson(json, com.health.objects.GetAvailibleProcedure.Request.class);

            RandomAccessFile list = new RandomAccessFile(PathsMine.InstalledProcList, "r");
            long num = list.length() / Long.BYTES;
            ArrayList<Long> ids = new ArrayList<>((int) num);
            while (num > 0) {
                ids.add(list.readLong());
                num--;
            }
            list.close();
            //System.out.println("GetAvailible PRoc 2");

            List<GetProcedure.Procedure> procs = new ArrayList<>();
            for (int i = 0; i < ids.size(); i++) {
                check(PathsMine.InstalledProc, "" + ids.get(i));

                RandomAccessFile proc_file = new RandomAccessFile(PathsMine.InstalledProc + ids.get(i), "rw");
                int size = proc_file.readInt();
                byte[] date = new byte[size];
                proc_file.read(date);
                String g = new String(date, "UTF-8");
                procs.add(server.gson.fromJson(g, GetProcedure.Procedure.class));
                proc_file.close();
                if (req.for_patient) {

                    if (procs.get(procs.size() - 1).type != Types.Type_Template_User) {
                        procs.remove(procs.size() - 1);
                    }
                }
            }
            //System.out.println("GetAvailible PRoc 3");

            for (int i = 0; i < procs.size(); i++) {
                translateProc(procs.get(i), -1999L, req.lang);
            }
            res = server.gson.toJson(procs);

            //System.out.println("GetAvailible PRoc 4");
        } catch (Exception e) {
            e.printStackTrace();

        }

        return res;

    }

    public static String handleGetTime(String json) {
        String res = null;

        try {

            Time t = new Time();
            String current = PathsMine.getCurrentTime();
            String[] h = current.split("-");
            t.year = Integer.parseInt(h[0]);
            t.month = Integer.parseInt(h[1]);
            t.days = Integer.parseInt(h[2]);
            t.hour = Integer.parseInt(h[3]);
            t.min = Integer.parseInt(h[4]);

            DateTime time = new DateTime(t.year, t.month, t.days, t.hour, t.min);
            t.day = time.getDayOfWeek();
            res = server.gson.toJson(t, Time.class);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return res;

    }

    public static String handleGetAvailibleRooms(String json) {
        String res = null;
        try {
            RandomAccessFile list = new RandomAccessFile(PathsMine.InstalledRoomsList, "r");
            long num = list.length() / Long.BYTES;
            ArrayList<Long> ids = new ArrayList<>((int) num);
            while (num > 0) {
                ids.add(list.readLong());
                num--;
            }
            list.close();

            List<GetAvailibleRooms.Room> rooms = new ArrayList<>();
            for (int i = 0; i < ids.size(); i++) {

                rooms.add(getRoom(ids.get(i)));
            }

            GetAvailibleRooms.Rooms r = new GetAvailibleRooms.Rooms();
            r.rooms = rooms;

            res = server.gson.toJson(r);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return res;

    }

    public static String handleGetAvailibleServices(String json) {
        String res = null;

        try {
            RandomAccessFile list = new RandomAccessFile(PathsMine.InstalledServicePathList, "r");
            long num = list.length() / Long.BYTES;
            ArrayList<Long> ids = new ArrayList<>((int) num);
            while (num > 0) {
                ids.add(list.readLong());
                num--;
            }
            list.close();

            List<GetAvailibleServices.Service> services = new ArrayList<>();
            for (int i = 0; i < ids.size(); i++) {
                check(PathsMine.InstalledServicePath, "" + ids.get(i));

                RandomAccessFile service_file = new RandomAccessFile(PathsMine.InstalledServicePath + ids.get(i), "r");
                int size = service_file.readInt();
                byte[] date = new byte[size];
                service_file.read(date);
                String g = new String(date, "UTF-8");
                services.add(server.gson.fromJson(g, GetAvailibleServices.Service.class));
                service_file.close();
            }

            GetAvailibleServices.Services ser = new GetAvailibleServices.Services();
            ser.services = services;

            res = server.gson.toJson(ser);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return res;

    }

    public static String handleGetAvailibleInsurances(String json) {
        String res = null;
        try {
            RandomAccessFile list = new RandomAccessFile(PathsMine.InstalledInsurancesList, "r");

            long num = list.length() / Long.BYTES;
            ArrayList<Long> ids = new ArrayList<>((int) num);
            while (num > 0) {
                ids.add(list.readLong());
                num--;
            }
            list.close();

            List<GetAvailibleInsurances.Insurance> insurances = new ArrayList<>();
            for (int i = 0; i < ids.size(); i++) {

                check(PathsMine.InstalledInsurances, "" + ids.get(i));

                RandomAccessFile insurance_file = new RandomAccessFile(PathsMine.InstalledInsurances + ids.get(i), "r");
                int size = insurance_file.readInt();
                byte[] date = new byte[size];
                insurance_file.read(date);
                String g = new String(date, "UTF-8");
                insurances.add(server.gson.fromJson(g, GetAvailibleInsurances.Insurance.class));
                insurance_file.close();
            }

            GetAvailibleInsurances.Insurances ser = new GetAvailibleInsurances.Insurances();
            ser.insurances = insurances;
            res = server.gson.toJson(ser);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return res;

    }

    public static String handleGetAvailibleMedications(String json) {

        String res = null;
        try {
            RandomAccessFile list = new RandomAccessFile(PathsMine.InstalledMedicationsList, "r");
            long num = list.length() / Long.BYTES;
            ArrayList<Long> ids = new ArrayList<>((int) num);
            while (num > 0) {
                ids.add(list.readLong());
                num--;
            }
            list.close();
            List<GetCondtion.Medication> medications = new ArrayList<>();
            for (int i = 0; i < ids.size(); i++) {
                check(PathsMine.InstalledMedications, "" + ids.get(i));

                RandomAccessFile medication_file = new RandomAccessFile(PathsMine.InstalledMedications + ids.get(i), "r");
                int size = medication_file.readInt();
                byte[] date = new byte[size];
                medication_file.read(date);
                String g = new String(date, "UTF-8");
                medications.add(server.gson.fromJson(g, GetCondtion.Medication.class));
                medication_file.close();
            }

            GetAvailibleMedications.Medications ser = new GetAvailibleMedications.Medications();
            ser.medications = medications;
            res = server.gson.toJson(ser);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return res;

    }

    private static void check(String dir, String h) {

        try {
            File f = new File(dir, h);
            if (f.exists()) {
                return;
            }
            f.getParentFile().mkdirs();
            f.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(Views.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean exist(String path) {
        return new File(path).exists();
    }

    public static String handleCheckIDPass(String json) {
        String res = null;

        try {
            com.health.objects.CheckAccountIdPass.Request request = server.gson.fromJson(json, com.health.objects.CheckAccountIdPass.Request.class);

            if (request.account_type == Types.Acc_Type_Patient) {
                Patient p = getPatient(request.id);

                if (p.Pass.equals(request.pass)) {
                    res = "{ 'msg:'" + Strings.LET_PASS + "}";
                } else {

                    res = "{'error':1}";
                }

            } else {

                MedTeam med = getMedTeam(request.id);

                if (med.pass.equals(request.pass)) {
                    res = "{ 'msg:'" + Strings.LET_PASS + "}";
                } else {
                    res = "{'error':1}";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;

    }

    public static String handleGetCondtion(String json) {
        String res = null;

        try {
            com.health.objects.GetCondtion.Request request = server.gson.fromJson(json, com.health.objects.GetCondtion.Request.class);
            Patient p = getPatient(request.patient_id);

            com.health.objects.GetCondtion.Condtion procs = null;
            for (int i = 0; i < p.activeCondtions.size(); i++) {
                if (p.activeCondtions.get(i).condtion_id == request.condtion_id) {
                    procs = p.activeCondtions.get(i);
                    break;
                };
            }

            if (procs == null) {
                for (int i = 0; i < p.historyCondtions.size(); i++) {
                    if (p.historyCondtions.get(i).condtion_id == request.condtion_id) {
                        procs = p.historyCondtions.get(i);
                        break;
                    };
                }
            }

            //System.out.println("Get Condtion " + procs + "      " + p);
            translateCondtion(procs, p.id, p.lang);
            res = server.gson.toJson(procs);
        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println("Getting condtion Error ");

        }
        return res;

    }

    public static String handleGetProcedure(String json) {
        String res = null;

        try {
            com.health.objects.GetProcedure.Request request = server.gson.fromJson(json, com.health.objects.GetProcedure.Request.class);
            Patient p = getPatient(request.patient_id);

            com.health.objects.GetProcedure.Procedure procs = null;
            for (int i = 0; i < p.activeProcedures.size(); i++) {
                if (p.activeProcedures.get(i).proc_id == request.proc_id) {
                    procs = p.activeProcedures.get(i);
                    break;
                };
            }

            if (procs == null) {
                for (int i = 0; i < p.historyProcedures.size(); i++) {
                    if (p.historyProcedures.get(i).proc_id == request.proc_id) {
                        procs = p.historyProcedures.get(i);
                        break;
                    };
                }
            }

            translateProc(procs, p.id, p.lang);

            res = server.gson.toJson(procs);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return res;

    }

    public static String handleGetActiveFeedBack(String json) {
        String res = null;
        try {

            com.health.objects.GetActiveFeedbackList.Request request = server.gson.fromJson(json, com.health.objects.GetActiveFeedbackList.Request.class);
            MedTeam m = getMedTeam(request.med_team_id);
            if (m != null) {
                com.health.objects.GetActiveFeedbackList.Patients patients = new com.health.objects.GetActiveFeedbackList.Patients();
                patients.patients = new ArrayList<>();

                for (int i = 0; i < m.patients.size(); i++) {
                    com.health.objects.GetActiveFeedbackList.Patient o = new com.health.objects.GetActiveFeedbackList.Patient();
                    Patient p = getPatient(m.patients.get(i).patient_id);

                    Period pd = new Period(p.personalData.date_of_birth.replace("/", "-") + "-0-0");
                    o.age = pd.pyear;
                    o.patient_name = p.personalData.name;
                    o.date = p.personalData.date_of_birth;
                    o.patient_id = p.id;
                    o.procedure_id = m.patients.get(i).procedure_id;
                    o.req_id = m.patients.get(i).request_id;
                    for (int c = 0; c < p.activeProcedures.size(); c++) {
                        if (p.activeProcedures.get(c).proc_id == o.procedure_id) {
                            o.remaining_days = p.activeProcedures.get(c).remaining_days;
                            o.proc_date = p.activeProcedures.get(c).date_activation;
                            break;
                        }
                    }
                    patients.patients.add(o);
                }

                res = server.gson.toJson(patients);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;

    }

    public static String handleGetActiveSmartFeeds(String json) {
        String res = null;

        try {
            com.health.objects.GetSmartFeedBackList.Request request = server.gson.fromJson(json, com.health.objects.GetSmartFeedBackList.Request.class);
            Session s = getSession(request.services);
            if (s != null) {

                com.health.objects.GetSmartFeedBackList.Patients patients = new com.health.objects.GetSmartFeedBackList.Patients();
                patients.patients = new ArrayList<>();
                for (int i = 0; i < s.patients.size(); i++) {
                    com.health.objects.GetSmartFeedBackList.Patient o = new com.health.objects.GetSmartFeedBackList.Patient();
                    Patient p = getPatient(s.patients.get(i).patient_id);
                    Period pd = new Period(p.personalData.date_of_birth.replace("/", "-") + "-0-0");
                    o.age = pd.pyear;
                    o.name = p.personalData.name;
                    o.date = p.personalData.date_of_birth;
                    o.patient_id = p.id;
                    o.procedure_id = s.patients.get(i).procedure_id;
                    o.request_id = s.patients.get(i).request_id;
                    for (int c = 0; c < p.activeProcedures.size(); c++) {
                        if (p.activeProcedures.get(c).proc_id == o.procedure_id) {
                            o.remaining_days = p.activeProcedures.get(c).remaining_days;
                            break;
                        }
                    }
                    patients.patients.add(o);
                }
                res = server.gson.toJson(patients);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;

    }

    public static String handleAffectedAppointments(String json) {
        String res = null;

        try {
            com.health.objects.GetAffectedAppointments.Request request = server.gson.fromJson(json, com.health.objects.GetAffectedAppointments.Request.class);

            List<GetAffectedAppointments.AffectedAppointment> affected = new ArrayList<>();

            for (int i = 0; i < request.room_ids.size(); i++) {

                List<AppointmentRoom> apps = getFreshAppointments(request.room_ids.get(i));
                for (int x = 0; x < request.schedule.size(); x++) {

                    AppointmentRoom r = translate(request.schedule.get(x));

                    for (int y = 0; y < apps.size(); y++) {
                        if (isAffected(r, apps.get(y))) {
                            GetAffectedAppointments.AffectedAppointment s = new GetAffectedAppointments.AffectedAppointment();
                            s.app_id = apps.get(y).id;
                            s.capacity = apps.get(y).capacity;
                            s.patientsCount = apps.get(y).Queue.size() + apps.get(y).booked.size() + apps.get(y).temp_booked.size();
                            s.from = apps.get(y).from;
                            s.to = apps.get(y).to;
                            s.date = apps.get(y).date;
                            s.room_id = request.room_ids.get(i);
                            s.room_path = getRoom(request.room_ids.get(i)).room_path;
                            affected.add(s);
                        }
                    }
                }
            }

            res = server.gson.toJson(affected, new TypeToken<ArrayList<GetAffectedAppointments.AffectedAppointment>>() {
            }.getType());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;

    }

    public static String handleGetFullAppointment(String json) {
        String res = null;
        try {
            com.health.objects.GetFullAppointment.Request request = server.gson.fromJson(json, com.health.objects.GetFullAppointment.Request.class);

            List<GetFullAppointment.FullAppointment> full = new ArrayList<>();
            List<AppointmentRoom> apps = getFreshAppointments(request.room_id);

            for (int y = 0; y < apps.size(); y++) {
                GetFullAppointment.FullAppointment s = new GetFullAppointment.FullAppointment();
                s.app_id = apps.get(y).id;
                s.capacity = apps.get(y).capacity;
                s.patientsCount = apps.get(y).Queue.size() + apps.get(y).booked.size() + apps.get(y).temp_booked.size();
                s.from = apps.get(y).from;
                s.to = apps.get(y).to;
                s.date = apps.get(y).date;
                s.room_id = request.room_id;
                s.room_path = getRoom(request.room_id).room_path;
                full.add(s);

            }
            res = server.gson.toJson(full);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    public static String handleGetActiveCondtions(String json) {
        String res = null;

        try {

            com.health.objects.GetActiveCondtions.Request request = server.gson.fromJson(json, com.health.objects.GetActiveCondtions.Request.class);

            Patient p = getPatient(request.patient_id);

            com.health.objects.GetActiveCondtions.Condtions condtions = new com.health.objects.GetActiveCondtions.Condtions();

            condtions.condtions = p.activeCondtions;

            for (int i = 0; i < p.activeCondtions.size(); i++) {
                translateCondtion(p.activeCondtions.get(i), p.id, p.lang);
            }

            res = server.gson.toJson(condtions);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;

    }

    public static String handleCancelAppointment(String json) {
        String res = null;
        try {
            com.health.objects.CancelAppointment.Request request = server.gson.fromJson(json, com.health.objects.CancelAppointment.Request.class);
            cancelAppointMent(getAppointmentRoom(request.appointment_id, request.room_id), request.room_id);
            res = Strings.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    private static void cancelAppointMent(AppointmentRoom app, long room) {

        try {
            AppointmentRoom appr = getAppointmentRoom(app.id, room);
            if (appr != null) {
                check(PathsMine.InstalledRooms + room, "" + PathsMine.InstalledSchedulesList);
                RandomAccessFile list = new RandomAccessFile(PathsMine.InstalledRooms + room + PathsMine.InstalledSchedulesList, "rw");
                long num = list.length() / Long.BYTES;
                ArrayList<Long> ids = new ArrayList<>((int) num);
                while (num > 0) {
                    ids.add(list.readLong());
                    num--;
                }
                ids.remove(app.id);
                list.setLength(ids.size() * Long.BYTES);
                list.seek(0);
                for (int i = 0; i < ids.size(); i++) {
                    list.writeLong(ids.get(i));

                }
                list.close();
                File f = new File(PathsMine.InstalledRooms + room + PathsMine.InstalledSchedules, "" + app.id);
                f.delete();
                for (int i = 0; i < appr.Queue.size(); i++) {
                    updatePatient(appr.Queue.get(i).patient_id);
                }
                for (int i = 0; i < appr.booked.size(); i++) {
                    updatePatient(appr.booked.get(i).patient_id);
                }
                for (int i = 0; i < appr.temp_booked.size(); i++) {
                    updatePatient(appr.temp_booked.get(i).patient_id);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String handleGetActiveProcedures(String json) {
        String res = null;

        try {
            com.health.objects.GetActiveProcedures.Request request = server.gson.fromJson(json, com.health.objects.GetActiveProcedures.Request.class);
            Patient p = getPatient(request.patient_id);
            com.health.objects.GetActiveProcedures.Procedures procs = new com.health.objects.GetActiveProcedures.Procedures();
            procs.procedures = p.activeProcedures;

            for (int i = 0; i < p.activeProcedures.size(); i++) {
                translateProc(p.activeProcedures.get(i), p.id, p.lang);
            }
            res = server.gson.toJson(procs);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;

    }

    public static String handleGetNotifications(String json) {
        String res = null;
        try {
            com.health.objects.GetNotifications.Request request = server.gson.fromJson(json, com.health.objects.GetNotifications.Request.class);
            Patient p = getPatient(request.patient_id);
            com.health.objects.GetNotifications.Notifications notis = new com.health.objects.GetNotifications.Notifications();
            notis.notifications = p.notifications;

            List<GetNotifications.Notification> notifications = new ArrayList<>();
            int size = notis.notifications.size() > 25 ? 25 : notis.notifications.size();

            for (int i = size - 1; i >= 0; i--) {
                translateNotification(p.notifications.get(i), p.id, p.lang);
                notifications.add(p.notifications.get(i));
            }

            res = server.gson.toJson(notis);

        } catch (Exception e) {
            e.printStackTrace();

        }

        return res;

    }

    public static String handleSetLang(String json) {
        String res = null;
        try {
            com.health.objects.setLang.Request request = server.gson.fromJson(json, com.health.objects.setLang.Request.class);
            Patient p = getPatient(request.id);
            p.lang = request.lang;
            updateLastTimeModified(p);
            savePatient(p);
            res = server.gson.toJson(Strings.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return res;
    }

    public static String handleGetPersonalData(String json) {
        String res = null;
        try {
            com.health.objects.GetPersonalData.Request request = server.gson.fromJson(json, com.health.objects.GetPersonalData.Request.class);
            Patient p = getPatient(request.patient_id);
            com.health.objects.GetPersonalData.PersonalData notis = p.personalData;
            if (p.lang == Types.Lang_Arabic) {
                p.personalData.name = p.personalData.name_ar;
            }
            notis.address = TranslateHandler.translate(notis.address, -1, p.lang, p.id);
            notis.gender = TranslateHandler.translate(notis.gender, -1, p.lang, -1999);
            notis.insurance_path = TranslateHandler.translate(notis.insurance_path, -1, p.lang, -1999);
            RandomAccessFile time = Util.getFile("rw", PathsMine.Time);
            notis.last_time_server_modified = time.readLong();
            time.close();
            res = server.gson.toJson(notis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;

    }

    public static String handleGetMedData(String json) {
        String res = null;
        try {
            com.health.objects.GetMedData.Request request = server.gson.fromJson(json, com.health.objects.GetMedData.Request.class);
            MedTeam t = getMedTeam(request.med_id);

            com.health.objects.GetMedData.MedData notis = new com.health.objects.GetMedData.MedData();
            notis.id = t.id;
            notis.name = t.name;

            res = server.gson.toJson(notis);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return res;

    }

    public static String handleGetRoomsAppointments(String json) {
        String res = null;
        try {
            com.health.objects.GetRoomsAppointments.Request request = server.gson.fromJson(json, com.health.objects.GetRoomsAppointments.Request.class);
            List<com.health.objects.GetRoomsAppointments.Appointment> list_res = new ArrayList<>();

            for (int i = 0; i < request.rooms.size(); i++) {
                List<AppointmentRoom> list = getRangeAppointments(request.rooms.get(i), PathsMine.getCurrentTime(), 20);
                Room r = getRoom(request.rooms.get(i));
                for (int x = 0; x < list.size(); x++) {
                    if (list.get(x).Queue.size() == list.get(x).capacity) {
                        continue;
                    }
                    com.health.objects.GetRoomsAppointments.Appointment app = new com.health.objects.GetRoomsAppointments.Appointment();
                    app.appointment_id = list.get(x).id;
                    app.capacity = list.get(x).capacity;
                    app.date = list.get(x).date;
                    app.from = list.get(x).from;
                    app.to = list.get(x).to;
                    app.room_id = request.rooms.get(i);
                    app.room_path = TranslateHandler.translate(r.room_path, -1, request.lang, -1999);
                    list_res.add(app);
                }
            }

            com.health.objects.GetRoomsAppointments.Appointments notis = new com.health.objects.GetRoomsAppointments.Appointments();

            notis.appointments = list_res;

            res = server.gson.toJson(notis);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return res;

    }

    public static String handleGetCurrentRoomAppointments(String json) {
        String res = null;

        try {
            com.health.objects.GetRoomCurrentAppointment.Request request = server.gson.fromJson(json, com.health.objects.GetRoomCurrentAppointment.Request.class);
            List<AppointmentRoom> list = getFreshAppointments(request.room_id);
            if (list.size() > 0) {
                com.health.objects.GetRoomCurrentAppointment.CurrentAppointment app = new GetRoomCurrentAppointment.CurrentAppointment();
                app.appointment_id = list.get(0).id;
                app.capacity = list.get(0).capacity;
                app.date = list.get(0).date;
                app.queue_amount = list.get(0).Queue.size();
                app.from = list.get(0).from;
                app.to = list.get(0).to;
                app.room_id = request.room_id;
                com.health.objects.GetRoomCurrentAppointment.CurrentPatient pa = new com.health.objects.GetRoomCurrentAppointment.CurrentPatient();
                pa.patient_id = 0;
                int idx = 0;
                for (int i = 0; i < list.get(0).Queue.size(); i++) {
                    if (!list.get(0).Queue.get(i).handeled) {
                        Patient pat = getPatient(list.get(0).Queue.get(i).patient_id);
                        idx = i;
                        Period pd = new Period(pat.personalData.date_of_birth.replace("/", "-") + "-0-0");
                        pa.age = pd.pyear + " Years";
                        pa.name = pat.personalData.name;
                        pa.patient_id = pat.id;
                        pa.procedure_id = list.get(0).Queue.get(i).procedure_id;
                        pa.request = list.get(0).Queue.get(i).request_id;

                        break;
                    } else {
                        app.queue_amount--;
                    }
                }
                app.current = pa;
                for (int i = idx + 1; i < list.get(0).Queue.size(); i++) {
                    Patient pat = getPatient(list.get(0).Queue.get(i).patient_id);
                    Period pd = new Period(pat.personalData.date_of_birth.replace("/", "-") + "-0-0");

                    GetRoomCurrentAppointment.Patient p = new GetRoomCurrentAppointment.Patient();
                    p.age = pd.pyear + " Years";
                    p.name = pat.personalData.name;
                    app.patients.add(p);
                    break;
                }
                res = server.gson.toJson(app);

            } else {

                return "Empty error";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    // saves the patient
    private static Patient savePatient(Patient p) {

        try {
            check(PathsMine.InstalledPatients, "" + p.id);

            File f = new File(PathsMine.InstalledPatients + p.id);
            f.delete();
            RandomAccessFile file = new RandomAccessFile(PathsMine.InstalledPatients + p.id, "rw");
            byte[] datad = server.gson.toJson(p).getBytes("UTF-8");
            int size = datad.length;
            file.writeInt(size);
            file.write(datad);
            file.close();
            return p;
        } catch (Exception ex) {
            Logger.getLogger(Views.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println("Error");
        }
        return null;
    }

    private static void updatePatient(long id) throws IOException {

        check(PathsMine.InstalledPatients, "" + id);

        RandomAccessFile file = new RandomAccessFile(PathsMine.InstalledPatients + id, "r");
        int size = file.readInt();
        byte[] date = new byte[size];
        file.read(date);
        String g = new String(date, "UTF-8");
        String json = g;
        file.close();
        Patient p = server.gson.fromJson(json, Patient.class);
        updatePatient(p);
    }

    private static void updatePatient(Patient p) throws IOException {

        for (int i = 0; i < p.activeCondtions.size(); i++) {

            com.health.objects.GetCondtion.Condtion cond = p.activeCondtions.get(i);

            Period period = new Period(cond.date_activation);
            int extra = period.getPeriodInDays() - cond.activation_period;
            if (extra >= 0) {
                cond.state = Types.State_Cond_History;
                p.activeCondtions.remove(cond);
                p.historyCondtions.add(cond);
                i--;
                cond.date_deactivation = (new Date(cond.date_activation)).withDays(cond.activation_period);
                updateLastTimeModified(p);

                if (cond.activities == null) {
                    cond.activities = new ArrayList<>();
                }

                for (GetCondtion.Activity activity : cond.activities) {

                    //check if already deactive
                    String g1 = new Date(cond.date_activation).withDays(activity.delay_period);
                    String g2 = new Date(g1).withDays(activity.activation_period);
                    if (Util.isAfter(g2, p.lastTimeUpdated) && Util.isAfter(PathsMine.getCurrentTime(), g1)) {
                        int value = 1;
                        switch (activity.cycle_period_unit) {
                            case 0: {
                                value *= 60;
                            }
                            break;
                            case 1: {
                                value *= 24 * 60;
                            }
                            break;
                            case 2: {
                                value *= 7 * 24 * 60;
                            }
                            break;
                            case 3: {
                                value *= 30 * 7 * 24 * 60;
                            }
                            break;
                        }
                        Period actper = new Period(g1, p.lastTimeUpdated);
                        int lastoccure = actper.getPeriodInMinutes() - actper.getPeriodInMinutes() % (activity.cycle_period * value);
                        String instant = new Date(g1).with(0, 0, lastoccure);
                        actper = new Period(instant, Util.isAfter(g2, PathsMine.getCurrentTime()) ? PathsMine.getCurrentTime() : g2);
                        int times = actper.getPeriodInMinutes() / (activity.cycle_period * value);
                        for (int t = 1; t <= times; t++) {
                            com.health.objects.GetNotifications.Notification noti = new com.health.objects.GetNotifications.Notification();
                            noti.type = com.health.objects.GetNotifications.Notification_Activity;
                            noti.condtion_id = cond.condtion_id;
                            noti.procedure_id = 0;
                            noti.activity_id = activity.activity_id;
                            noti.notification_id = Util.generateIDNotification(p.notifications);
                            noti.date = Util.dateWith(instant, 0, 0, 0, 0, activity.cycle_period * t * value);
                            noti.date_added = PathsMine.getCurrentTime();

                            p.notifications.add(noti);
                        }
                    }
                }
                if (cond.medications_routine == null) {
                    cond.medications_routine = new ArrayList<>();
                }

                for (GetCondtion.MedicationRoutine medication : cond.medications_routine) {

                    //check if already deactive
                    String g1 = new Date(cond.date_activation).withDays(medication.delay_period);
                    String g2 = new Date(g1).withDays(medication.activation_period);
                    if (Util.isAfter(g2, p.lastTimeUpdated) && Util.isAfter(PathsMine.getCurrentTime(), g1)) {
                        int value = 1;
                        switch (medication.cycle_unit) {
                            case 0: {
                                value *= 60;
                            }
                            break;
                            case 1: {
                                value *= 24 * 60;
                            }
                            break;
                            case 2: {
                                value *= 7 * 24 * 60;
                            }
                            break;
                            case 3: {
                                value *= 30 * 7 * 24 * 60;
                            }
                            break;
                        }
                        Period actper = new Period(g1, p.lastTimeUpdated);
                        int lastoccure = (int) (actper.getPeriodInMinutes() - actper.getPeriodInMinutes() % (medication.dosage_cycle * value));
                        String instant = new Date(g1).with(0, 0, lastoccure);
                        actper = new Period(instant, Util.isAfter(g2, PathsMine.getCurrentTime()) ? PathsMine.getCurrentTime() : g2);
                        int times = (int) (actper.getPeriodInMinutes() / (medication.dosage_cycle * value));
                        for (int t = 1; t <= times; t++) {
                            com.health.objects.GetNotifications.Notification noti = new com.health.objects.GetNotifications.Notification();
                            noti.type = com.health.objects.GetNotifications.Notification_Medication;
                            noti.condtion_id = cond.condtion_id;
                            noti.procedure_id = 0;
                            noti.medication_id = medication.medication.medication_id;
                            noti.notification_id = Util.generateIDNotification(p.notifications);
                            noti.date = Util.dateWith(instant, 0, 0, 0, 0, (int) (medication.dosage_cycle * t * value));
                            noti.date_added = PathsMine.getCurrentTime();

                            p.notifications.add(noti);
                        }
                    }
                }
                if (cond.procedures == null) {
                    cond.procedures = new ArrayList<>();
                }

                for (GetProcedure.Procedure procedure : cond.procedures) {
                    //check if already deactive
                    if (procedure.type == Types.Type_Procedure_Condtion_Routine) {
                        String g1 = new Date(cond.date_activation).withDays(procedure.delay_period);
                        String g2 = new Date(g1).withDays(procedure.routine_activation_period);
                        if (Util.isAfter(g2, p.lastTimeUpdated) && Util.isAfter(PathsMine.getCurrentTime(), g1)) {
                            int value = 24 * 60;
                            Period actper = new Period(g1, p.lastTimeUpdated);
                            int lastoccure = (int) (actper.getPeriodInMinutes() - actper.getPeriodInMinutes() % (procedure.cycle_period * value));
                            String instant = new Date(g1).with(0, 0, lastoccure);
                            actper = new Period(instant, Util.isAfter(g2, PathsMine.getCurrentTime()) ? PathsMine.getCurrentTime() : g2);
                            int times = (int) (actper.getPeriodInMinutes() / (procedure.cycle_period * value));
                            for (int t = 1; t <= times; t++) {
                                com.health.objects.GetProcedure.Procedure c = server.gson.fromJson(server.gson.toJson(procedure), GetProcedure.Procedure.class);
                                c.date_activation = Util.dateWith(instant, 0, 0, 0, 0, (int) (c.cycle_period * t) * value);
                                c.state = com.health.objects.Types.State_Proc_Active;

                                ArrayList<GetProcedure.Procedure> proc = new ArrayList<>();
                                proc.addAll(p.activeProcedures);
                                proc.addAll(p.historyProcedures);
                                c.proc_id = Util.generateIDProc(proc);
                                c.remaining_days = c.activation_period;
                                for (GetProcedure.FeedBack f : c.feedbacks) {
                                    f.state = com.health.objects.Types.Request_NotProccesse;
                                }
                                for (GetProcedure.SmartFeedBack f : c.smart_feedbacks) {
                                    f.state = com.health.objects.Types.Request_NotProccesse;
                                }
                                for (GetProcedure.ServicesGroup f : c.services) {
                                    f.state = com.health.objects.Types.Request_NotProccesse;
                                }
                                updateLastTimeModified(p);
                                p.activeProcedures.add(c);

                                boolean isActive = false;
                                for (int x = 0; x < c.feedbacks.size(); x++) {

                                    c.feedbacks.get(x).state = Types.Request_NotProccesse;
                                    if (c.feedbacks.get(x).order == 0) {
                                        isActive = true;
                                        c.feedbacks.get(x).state = Types.Request_InProgress;
                                        MedTeam m = getMedTeam(c.feedbacks.get(x).med_team_id);
                                        MedTeam.Patient mP = new MedTeam.Patient();
                                        mP.patient_id = p.id;
                                        mP.procedure_id = c.proc_id;
                                        mP.request_id = c.feedbacks.get(x).req_id;
                                        m.patients.add(mP);
                                        updateMedTeam(m);
                                    }
                                }

                                for (int x = 0; x < c.smart_feedbacks.size(); x++) {
                                    c.smart_feedbacks.get(x).state = Types.Request_NotProccesse;
                                    if (c.smart_feedbacks.get(x).order == 0) {
                                        isActive = true;
                                        c.smart_feedbacks.get(x).state = Types.Request_InProgress;
                                        Session m = getSession(c.smart_feedbacks.get(x).services, 0);
                                        if (m == null) {
                                            m = createSession(c.smart_feedbacks.get(x).services);
                                        }
                                        c.smart_feedbacks.get(i).session_id = m.id;

                                        Session.Patient mP = new Session.Patient();
                                        mP.patient_id = p.id;
                                        mP.procedure_id = c.proc_id;
                                        mP.request_id = c.smart_feedbacks.get(x).req_id;
                                        m.patients.add(mP);
                                        updateSession(m);
                                    }

                                }
                                for (int x = 0; x < c.services.size(); x++) {
                                    c.services.get(x).state = Types.Request_NotProccesse;
                                    if (c.services.get(x).order == 0) {
                                        isActive = true;
                                        if (c.services.get(x).appointment == null) {
                                            c.services.get(x).appointment = new GetProcedure.Appointment();
                                        }
                                        c.services.get(x).state = Types.Request_InProgress;
                                        c.services.get(x).appointment.state = Types.Appointment_Invoice_Not_paid;

                                    }

                                }
                                if (!isActive) {
                                    com.health.objects.GetNotifications.Notification noti = new com.health.objects.GetNotifications.Notification();
                                    noti.type = com.health.objects.GetNotifications.Notification_Msg;
                                    noti.condtion_id = 0;
                                    noti.procedure_id = 0;
                                    noti.msg = c.note_patient;
                                    noti.notification_id = Util.generateIDNotification(p.notifications);
                                    noti.date = Util.dateWith(instant, 0, 0, 0, 0, (int) (c.cycle_period * t * value));
                                    noti.date_added = PathsMine.getCurrentTime();

                                    p.notifications.add(noti);
                                    p.activeProcedures.remove(c);
                                } else {
                                    com.health.objects.GetNotifications.Notification noti = new com.health.objects.GetNotifications.Notification();
                                    noti.type = com.health.objects.GetNotifications.Notification_Visit_Created;
                                    noti.condtion_id = cond.condtion_id;
                                    noti.procedure_id = c.proc_id;
                                    noti.notification_id = Util.generateIDNotification(p.notifications);
                                    noti.date = Util.dateWith(instant, 0, 0, 0, 0, (int) (c.cycle_period * t * value));
                                    noti.date_added = PathsMine.getCurrentTime();

                                    p.notifications.add(noti);
                                }
                            }
                        }
                    } else {

                        String g1 = new Date(cond.date_activation).withDays(procedure.delay_period);
                        if (Util.isAfter(g1, p.lastTimeUpdated)) {
                            if (Util.isAfter(PathsMine.getCurrentTime(), g1) || g1.equals(p.lastTimeUpdated)) {
                                com.health.objects.GetProcedure.Procedure c = server.gson.fromJson(server.gson.toJson(procedure), GetProcedure.Procedure.class);

                                c.date_activation = g1;
                                c.state = com.health.objects.Types.State_Proc_Active;

                                ArrayList<GetProcedure.Procedure> proc = new ArrayList<>();
                                proc.addAll(p.activeProcedures);
                                proc.addAll(p.historyProcedures);
                                c.proc_id = Util.generateIDProc(proc);
                                c.remaining_days = c.activation_period;
                                for (GetProcedure.FeedBack f : c.feedbacks) {
                                    f.state = com.health.objects.Types.Request_NotProccesse;
                                }
                                for (GetProcedure.SmartFeedBack f : c.smart_feedbacks) {
                                    f.state = com.health.objects.Types.Request_NotProccesse;
                                }
                                for (GetProcedure.ServicesGroup f : c.services) {
                                    f.state = com.health.objects.Types.Request_NotProccesse;
                                }
                                updateLastTimeModified(p);
                                p.activeProcedures.add(c);

                                boolean isActive = false;
                                for (int x = 0; x < c.feedbacks.size(); x++) {

                                    c.feedbacks.get(x).state = Types.Request_NotProccesse;
                                    if (c.feedbacks.get(x).order == 0) {
                                        isActive = true;
                                        c.feedbacks.get(x).state = Types.Request_InProgress;
                                        MedTeam m = getMedTeam(c.feedbacks.get(x).med_team_id);
                                        MedTeam.Patient mP = new MedTeam.Patient();
                                        mP.patient_id = p.id;
                                        mP.procedure_id = c.proc_id;
                                        mP.request_id = c.feedbacks.get(x).req_id;
                                        m.patients.add(mP);
                                        updateMedTeam(m);
                                    }
                                }

                                for (int x = 0; x < c.smart_feedbacks.size(); x++) {
                                    c.smart_feedbacks.get(x).state = Types.Request_NotProccesse;
                                    if (c.smart_feedbacks.get(x).order == 0) {
                                        isActive = true;
                                        c.smart_feedbacks.get(x).state = Types.Request_InProgress;
                                        Session m = getSession(c.smart_feedbacks.get(x).services, 0);
                                        if (m == null) {
                                            m = createSession(c.smart_feedbacks.get(x).services);
                                        }
                                        c.smart_feedbacks.get(i).session_id = m.id;

                                        Session.Patient mP = new Session.Patient();
                                        mP.patient_id = p.id;
                                        mP.procedure_id = c.proc_id;
                                        mP.request_id = c.smart_feedbacks.get(x).req_id;
                                        m.patients.add(mP);
                                        updateSession(m);
                                    }

                                }
                                for (int x = 0; x < c.services.size(); x++) {
                                    c.services.get(x).state = Types.Request_NotProccesse;
                                    if (c.services.get(x).order == 0) {
                                        isActive = true;
                                        if (c.services.get(x).appointment == null) {
                                            c.services.get(x).appointment = new GetProcedure.Appointment();
                                        }
                                        c.services.get(x).state = Types.Request_InProgress;
                                        c.services.get(x).appointment.state = Types.Appointment_Invoice_Not_paid;
                                    }
                                }
                                if (!isActive) {
                                    com.health.objects.GetNotifications.Notification noti = new com.health.objects.GetNotifications.Notification();
                                    noti.type = com.health.objects.GetNotifications.Notification_Msg;
                                    noti.condtion_id = 0;
                                    noti.procedure_id = 0;
                                    noti.msg = c.note_patient;
                                    noti.notification_id = Util.generateIDNotification(p.notifications);
                                    noti.date = g1;
                                    noti.date_added = PathsMine.getCurrentTime();

                                    p.notifications.add(noti);
                                    p.activeProcedures.remove(c);
                                } else {
                                    com.health.objects.GetNotifications.Notification noti = new com.health.objects.GetNotifications.Notification();
                                    noti.type = com.health.objects.GetNotifications.Notification_Visit_Created;
                                    noti.condtion_id = cond.condtion_id;
                                    noti.procedure_id = c.proc_id;
                                    noti.notification_id = Util.generateIDNotification(p.notifications);
                                    noti.date = g1;
                                    noti.date_added = PathsMine.getCurrentTime();

                                    p.notifications.add(noti);
                                }

                            }

                        }

                    }

                }
                com.health.objects.GetNotifications.Notification noti = new com.health.objects.GetNotifications.Notification();
                noti.type = com.health.objects.GetNotifications.Notification_Condtion_Removed;
                noti.condtion_id = cond.condtion_id;
                noti.procedure_id = 0;
                noti.notification_id = Util.generateIDNotification(p.notifications);
                noti.date = (new Date(cond.date_activation)).withDays(cond.activation_period);
                noti.date_added = PathsMine.getCurrentTime();

                p.notifications.add(noti);
            } else {

                //if not deactive
                cond.remaining_days = -extra;

                if (cond.activities == null) {
                    cond.activities = new ArrayList<>();
                }
                for (GetCondtion.Activity activity : cond.activities) {

                    //check if already deactive
                    String g1 = new Date(cond.date_activation).withDays(activity.delay_period);
                    String g2 = new Date(g1).withDays(activity.activation_period);
                    if (Util.isAfter(g2, p.lastTimeUpdated) && Util.isAfter(PathsMine.getCurrentTime(), g1)) {
                        int value = 1;
                        switch (activity.cycle_period_unit) {
                            case 0: {
                                value *= 60;
                            }
                            break;
                            case 1: {
                                value *= 24 * 60;
                            }
                            break;
                            case 2: {
                                value *= 7 * 24 * 60;
                            }
                            break;
                            case 3: {
                                value *= 30 * 7 * 24 * 60;
                            }
                            break;
                        }
                        Period actper = new Period(g1, p.lastTimeUpdated);
                        int lastoccure = actper.getPeriodInMinutes() - actper.getPeriodInMinutes() % (activity.cycle_period * value);
                        String instant = new Date(g1).with(0, 0, lastoccure);
                        actper = new Period(instant, Util.isAfter(g2, PathsMine.getCurrentTime()) ? PathsMine.getCurrentTime() : g2);
                        int times = actper.getPeriodInMinutes() / (activity.cycle_period * value);
                        for (int t = 1; t <= times; t++) {
                            com.health.objects.GetNotifications.Notification noti = new com.health.objects.GetNotifications.Notification();
                            noti.type = com.health.objects.GetNotifications.Notification_Activity;
                            noti.condtion_id = cond.condtion_id;
                            noti.procedure_id = 0;
                            noti.activity_id = activity.activity_id;
                            noti.notification_id = Util.generateIDNotification(p.notifications);
                            noti.date = Util.dateWith(instant, 0, 0, 0, 0, activity.cycle_period * t * value);
                            noti.date_added = PathsMine.getCurrentTime();
                            p.notifications.add(noti);
                            //System.out.print("New 1");
                            updateLastTimeModified(p);
                        }
                    }
                }
                if (cond.medications_routine == null) {
                    cond.medications_routine = new ArrayList<>();
                }

                for (GetCondtion.MedicationRoutine medication : cond.medications_routine) {

                    //check if already deactive
                    String g1 = new Date(cond.date_activation).withDays(medication.delay_period);
                    String g2 = new Date(g1).withDays(medication.activation_period);

                    if (Util.isAfter(g2, p.lastTimeUpdated) && Util.isAfter(PathsMine.getCurrentTime(), g1)) {

                        int value = 1;
                        switch (medication.cycle_unit) {
                            case 0: {
                                value *= 60;
                            }
                            break;
                            case 1: {
                                value *= 24 * 60;
                            }
                            break;
                            case 2: {
                                value *= 7 * 24 * 60;
                            }
                            break;
                            case 3: {
                                value *= 30 * 7 * 24 * 60;
                            }
                            break;
                        }

                        Period actper = new Period(g1, p.lastTimeUpdated);
                        int lastoccure = (int) (actper.getPeriodInMinutes() - actper.getPeriodInMinutes() % (medication.dosage_cycle * value));
                        String instant = new Date(g1).with(0, 0, lastoccure);
                        actper = new Period(instant, Util.isAfter(g2, PathsMine.getCurrentTime()) ? PathsMine.getCurrentTime() : g2);
                        int times = (int) (actper.getPeriodInMinutes() / (medication.dosage_cycle * value));
                        for (int t = 1; t <= times; t++) {
                            com.health.objects.GetNotifications.Notification noti = new com.health.objects.GetNotifications.Notification();
                            noti.type = com.health.objects.GetNotifications.Notification_Medication;
                            noti.condtion_id = cond.condtion_id;
                            noti.procedure_id = 0;
                            noti.medication_id = medication.medication.medication_id;
                            noti.notification_id = Util.generateIDNotification(p.notifications);
                            noti.date = Util.dateWith(instant, 0, 0, 0, 0, (int) (medication.dosage_cycle * t * value));
                            updateLastTimeModified(p);
                            noti.date_added = PathsMine.getCurrentTime();
                            p.notifications.add(noti);
                            //System.out.print("New 2");

                        }

                    }

                }
                if (cond.procedures == null) {
                    cond.procedures = new ArrayList<>();
                }

                for (GetProcedure.Procedure procedure : cond.procedures) {
                    //check if already deactive
                    if (procedure.type == Types.Type_Procedure_Condtion_Routine) {
                        String g1 = new Date(cond.date_activation).withDays(procedure.delay_period);
                        String g2 = new Date(g1).withDays(procedure.routine_activation_period);
                        if (Util.isAfter(g2, p.lastTimeUpdated) && Util.isAfter(PathsMine.getCurrentTime(), g1)) {
                            int value = 24 * 60;
                            Period actper = new Period(g1, p.lastTimeUpdated);
                            int lastoccure = (int) (actper.getPeriodInMinutes() - actper.getPeriodInMinutes() % (procedure.cycle_period * value));
                            String instant = new Date(g1).with(0, 0, lastoccure);
                            actper = new Period(instant, Util.isAfter(g2, PathsMine.getCurrentTime()) ? PathsMine.getCurrentTime() : g2);
                            int times = (int) (actper.getPeriodInMinutes() / (procedure.cycle_period * value));
                            for (int t = 1; t <= times; t++) {

                                com.health.objects.GetProcedure.Procedure c = server.gson.fromJson(server.gson.toJson(procedure), GetProcedure.Procedure.class);

                                c.date_activation = Util.dateWith(instant, 0, 0, 0, 0, (int) (c.cycle_period * t * value));

                                c.state = com.health.objects.Types.State_Proc_Active;

                                ArrayList<GetProcedure.Procedure> proc = new ArrayList<>();
                                proc.addAll(p.activeProcedures);
                                proc.addAll(p.historyProcedures);
                                c.proc_id = Util.generateIDProc(proc);
                                c.remaining_days = c.activation_period;

                                for (GetProcedure.FeedBack f : c.feedbacks) {
                                    f.state = com.health.objects.Types.Request_NotProccesse;
                                }
                                for (GetProcedure.SmartFeedBack f : c.smart_feedbacks) {
                                    f.state = com.health.objects.Types.Request_NotProccesse;
                                }
                                for (GetProcedure.ServicesGroup f : c.services) {
                                    f.state = com.health.objects.Types.Request_NotProccesse;
                                }
                                //System.out.print("New 3");

                                updateLastTimeModified(p);
                                p.activeProcedures.add(c);
                                boolean isActive = false;
                                for (int x = 0; x < c.feedbacks.size(); x++) {
                                    c.feedbacks.get(x).state = Types.Request_NotProccesse;
                                    if (c.feedbacks.get(x).order == 0) {
                                        isActive = true;
                                        c.feedbacks.get(x).state = Types.Request_InProgress;
                                        MedTeam m = getMedTeam(c.feedbacks.get(x).med_team_id);
                                        MedTeam.Patient mP = new MedTeam.Patient();
                                        mP.patient_id = p.id;
                                        mP.procedure_id = c.proc_id;
                                        mP.request_id = c.feedbacks.get(x).req_id;
                                        m.patients.add(mP);
                                        updateMedTeam(m);
                                    }
                                }

                                for (int x = 0; x < c.smart_feedbacks.size(); x++) {
                                    c.smart_feedbacks.get(x).state = Types.Request_NotProccesse;
                                    if (c.smart_feedbacks.get(x).order == 0) {
                                        isActive = true;
                                        c.smart_feedbacks.get(x).state = Types.Request_InProgress;
                                        Session m = getSession(c.smart_feedbacks.get(x).services, 0);
                                        if (m == null) {
                                            m = createSession(c.smart_feedbacks.get(x).services);
                                        }
                                        c.smart_feedbacks.get(i).session_id = m.id;
                                        Session.Patient mP = new Session.Patient();
                                        mP.patient_id = p.id;
                                        mP.procedure_id = c.proc_id;
                                        mP.request_id = c.smart_feedbacks.get(x).req_id;
                                        m.patients.add(mP);
                                        updateSession(m);
                                    }
                                }

                                for (int x = 0; x < c.services.size(); x++) {
                                    c.services.get(x).state = Types.Request_NotProccesse;
                                    if (c.services.get(x).order == 0) {
                                        isActive = true;
                                        if (c.services.get(x).appointment == null) {
                                            c.services.get(x).appointment = new GetProcedure.Appointment();
                                        }
                                        c.services.get(x).state = Types.Request_InProgress;
                                        c.services.get(x).appointment.state = Types.Appointment_Invoice_Not_paid;
                                    }

                                }
                                if (!isActive) {
                                    com.health.objects.GetNotifications.Notification noti = new com.health.objects.GetNotifications.Notification();
                                    noti.type = com.health.objects.GetNotifications.Notification_Msg;
                                    noti.condtion_id = 0;
                                    noti.procedure_id = 0;
                                    noti.msg = c.note_patient;
                                    noti.notification_id = Util.generateIDNotification(p.notifications);
                                    noti.date = Util.dateWith(instant, 0, 0, 0, 0, (int) (c.cycle_period * t * value));
                                    noti.date_added = PathsMine.getCurrentTime();
                                    p.notifications.add(noti);
                                    p.activeProcedures.remove(c);
                                } else {
                                    com.health.objects.GetNotifications.Notification noti = new com.health.objects.GetNotifications.Notification();
                                    noti.type = com.health.objects.GetNotifications.Notification_Visit_Created;
                                    noti.condtion_id = cond.condtion_id;
                                    noti.procedure_id = c.proc_id;
                                    noti.notification_id = Util.generateIDNotification(p.notifications);
                                    noti.date = Util.dateWith(instant, 0, 0, 0, 0, (int) (c.cycle_period * t * value));
                                    noti.date_added = PathsMine.getCurrentTime();
                                    p.notifications.add(noti);
                                }
                            }
                        }
                    } else {

                        String g1 = new Date(cond.date_activation).withDays(procedure.delay_period);
                        if (Util.isAfter(g1, p.lastTimeUpdated) || g1.equals(p.lastTimeUpdated)) {
                            if (Util.isAfter(PathsMine.getCurrentTime(), g1)) {
                                com.health.objects.GetProcedure.Procedure c = server.gson.fromJson(server.gson.toJson(procedure), GetProcedure.Procedure.class);

                                c.date_activation = g1;
                                c.state = com.health.objects.Types.State_Proc_Active;

                                ArrayList<GetProcedure.Procedure> proc = new ArrayList<>();
                                proc.addAll(p.activeProcedures);
                                proc.addAll(p.historyProcedures);
                                c.proc_id = Util.generateIDProc(proc);
                                c.remaining_days = c.activation_period;
                                for (GetProcedure.FeedBack f : c.feedbacks) {
                                    f.state = com.health.objects.Types.Request_NotProccesse;
                                }
                                for (GetProcedure.SmartFeedBack f : c.smart_feedbacks) {
                                    f.state = com.health.objects.Types.Request_NotProccesse;
                                }
                                for (GetProcedure.ServicesGroup f : c.services) {
                                    f.state = com.health.objects.Types.Request_NotProccesse;
                                }
                                //System.out.print("New 4");

                                updateLastTimeModified(p);
                                p.activeProcedures.add(c);

                                boolean isActive = false;
                                for (int x = 0; x < c.feedbacks.size(); x++) {

                                    c.feedbacks.get(x).state = Types.Request_NotProccesse;
                                    if (c.feedbacks.get(x).order == 0) {
                                        isActive = true;
                                        c.feedbacks.get(x).state = Types.Request_InProgress;
                                        MedTeam m = getMedTeam(c.feedbacks.get(x).med_team_id);
                                        MedTeam.Patient mP = new MedTeam.Patient();
                                        mP.patient_id = p.id;
                                        mP.procedure_id = c.proc_id;
                                        mP.request_id = c.feedbacks.get(x).req_id;
                                        m.patients.add(mP);
                                        updateMedTeam(m);
                                    }
                                }

                                for (int x = 0; x < c.smart_feedbacks.size(); x++) {
                                    c.smart_feedbacks.get(x).state = Types.Request_NotProccesse;
                                    if (c.smart_feedbacks.get(x).order == 0) {
                                        isActive = true;
                                        c.smart_feedbacks.get(x).state = Types.Request_InProgress;
                                        Session m = getSession(c.smart_feedbacks.get(x).services, 0);
                                        if (m == null) {
                                            m = createSession(c.smart_feedbacks.get(x).services);
                                        }
                                        c.smart_feedbacks.get(i).session_id = m.id;

                                        Session.Patient mP = new Session.Patient();
                                        mP.patient_id = p.id;
                                        mP.procedure_id = c.proc_id;
                                        mP.request_id = c.smart_feedbacks.get(x).req_id;
                                        m.patients.add(mP);
                                        updateSession(m);
                                    }

                                }
                                for (int x = 0; x < c.services.size(); x++) {
                                    c.services.get(x).state = Types.Request_NotProccesse;
                                    if (c.services.get(x).order == 0) {
                                        isActive = true;
                                        if (c.services.get(x).appointment == null) {
                                            c.services.get(x).appointment = new GetProcedure.Appointment();
                                        }
                                        c.services.get(x).state = Types.Request_InProgress;
                                        c.services.get(x).appointment.state = Types.Appointment_Invoice_Not_paid;
                                    }
                                }
                                if (!isActive) {
                                    com.health.objects.GetNotifications.Notification noti = new com.health.objects.GetNotifications.Notification();
                                    noti.type = com.health.objects.GetNotifications.Notification_Msg;
                                    noti.condtion_id = 0;
                                    noti.procedure_id = 0;
                                    noti.msg = c.note_patient;
                                    noti.notification_id = Util.generateIDNotification(p.notifications);
                                    noti.date = g1;
                                    noti.date_added = PathsMine.getCurrentTime();
                                    p.notifications.add(noti);
                                    p.activeProcedures.remove(c);
                                } else {
                                    com.health.objects.GetNotifications.Notification noti = new com.health.objects.GetNotifications.Notification();
                                    noti.type = com.health.objects.GetNotifications.Notification_Visit_Created;
                                    noti.condtion_id = cond.condtion_id;
                                    noti.procedure_id = c.proc_id;
                                    noti.notification_id = Util.generateIDNotification(p.notifications);
                                    noti.date = g1;
                                    noti.date_added = PathsMine.getCurrentTime();

                                    p.notifications.add(noti);
                                }

                            }

                        }

                    }

                }
            }
        }

        for (int i = 0; i < p.activeProcedures.size(); i++) {
            com.health.objects.GetProcedure.Procedure proc = p.activeProcedures.get(i);
            Period period = new Period(proc.date_activation);
            int extra = period.getPeriodInDays() - proc.activation_period;
            if (extra >= 0) {
                com.health.objects.GetProcedure.Procedure c = proc;
                if (c.state == Types.State_Proc_Active) {

                    c.date_deactivation = Util.dateWith(c.date_activation, 0, 0, c.activation_period, 0, 0);
                    c.state = com.health.objects.Types.State_Proc_History;
                    p.historyProcedures.add(c);
                    stopFeedBacks(p, c);
                    stopSmartFeeds(p, c);
                    stopVisits(p, c);
                    p.activeProcedures.remove(i);
                    i--;
                    com.health.objects.GetNotifications.Notification noti = new com.health.objects.GetNotifications.Notification();
                    noti.type = com.health.objects.GetNotifications.Notification_Visit_Failed;
                    noti.condtion_id = c.condtion_id;
                    noti.procedure_id = c.proc_id;

                    noti.notification_id = Util.generateIDNotification(p.notifications);
                    noti.date = Util.dateWith(c.date_activation, 0, 0, c.activation_period, 0, 0);
                    noti.date_added = PathsMine.getCurrentTime();
                    System.out.println(noti.procedure_id);
                    p.notifications.add(noti);
                    updateLastTimeModified(p);
                }
            } else {
                proc.remaining_days = -extra;
                for (GetProcedure.ServicesGroup group : proc.services) {
                    if (group.appointment == null || group.appointment.appointment_id == 0 || group.state != Types.Request_InProgress) {
                        continue;
                    }

                    File fileapp = new File(PathsMine.InstalledRooms + group.appointment.room_id + PathsMine.InstalledSchedules, "" + group.appointment.appointment_id);
                    if (!fileapp.exists()) {
                        if (group.appointment.state == Types.Appointment_Invoice_Not_paid) {
                            group.appointment.appointment_id = 0;
                        } else {
                            group.appointment.appointment_id = 0;
                            group.appointment.state = Types.Appointment_Not_Booked;
                        }
                        //System.out.print("New 6");
                        updateLastTimeModified(p);
                        continue;
                    }

                    String h = Util.add(group.appointment.date, "0-0-0-" + group.appointment.to);
                    if (Util.isAfter(PathsMine.getCurrentTime(), h)) {
                        if (group.appointment.state == Types.Appointment_Invoice_Not_paid) {
                            AppointmentRoom app = getAppointmentRoom(group.appointment.appointment_id, group.appointment.room_id);
                            for (int x = 0; x < app.temp_booked.size(); x++) {
                                if (app.temp_booked.get(x).patient_id == p.id) {
                                    app.temp_booked.remove(x);
                                    break;
                                }
                            }
                            //System.out.print("New 7");
                            updateAppointmentRoom(app, group.appointment.room_id);
                            group.appointment.appointment_id = 0;

                        } else if (group.appointment.state == Types.Appointment_Not_Booked) {

                        } else if (group.appointment.state == Types.Appointment_Waiting_For_Date) {
                            AppointmentRoom app = getAppointmentRoom(group.appointment.appointment_id, group.appointment.room_id);
                            for (int x = 0; x < app.booked.size(); x++) {
                                if (app.booked.get(x).patient_id == p.id) {
                                    app.booked.remove(x);
                                    break;
                                }
                            }
                            //System.out.print("New 8");
                            updateAppointmentRoom(app, group.appointment.room_id);
                            group.appointment.appointment_id = 0;
                            group.appointment.state = Types.Appointment_Not_Booked;
                        } else if (group.appointment.state == Types.Appointment_Waiting_For_Enter_Line) {

                            AppointmentRoom app = getAppointmentRoom(group.appointment.appointment_id, group.appointment.room_id);
                            for (int x = 0; x < app.booked.size(); x++) {
                                if (app.booked.get(x).patient_id == p.id) {
                                    app.booked.remove(x);
                                    break;
                                }
                            }
                            //System.out.print("New 9");
                            updateAppointmentRoom(app, group.appointment.room_id);
                            group.appointment.appointment_id = 0;
                            group.appointment.state = Types.Appointment_Not_Booked;
                        } else if (group.appointment.state == Types.Appointment_Waiting_For_Enter) {

                            AppointmentRoom app = getAppointmentRoom(group.appointment.appointment_id, group.appointment.room_id);
                            for (int x = 0; x < app.Queue.size(); x++) {
                                if (app.Queue.get(x).patient_id == p.id) {
                                    app.Queue.remove(x);
                                    break;
                                }
                            }
                            //System.out.print("New 10");
                            updateAppointmentRoom(app, group.appointment.room_id);
                            group.appointment.appointment_id = 0;
                            group.appointment.state = Types.Appointment_Not_Booked;
                        }

                        group.appointment.appointment_id = 0;
                        group.appointment.room_id = 0;
                        com.health.objects.GetNotifications.Notification noti = new com.health.objects.GetNotifications.Notification();
                        noti.type = com.health.objects.GetNotifications.Notification_Visit_Appointment_Change;
                        noti.condtion_id = proc.condtion_id;
                        noti.procedure_id = proc.proc_id;
                        noti.notification_id = Util.generateIDNotification(p.notifications);
                        noti.date = PathsMine.getCurrentTime();
                        noti.date_added = PathsMine.getCurrentTime();

                        p.notifications.add(noti);
                        //System.out.print("New 11");
                        updateLastTimeModified(p);
                    } else {

                        AppointmentRoom app = getAppointmentRoom(group.appointment.appointment_id, group.appointment.room_id);

                        if (group.appointment.queue_amount != app.Queue.size()) {
                            group.appointment.queue_amount = app.Queue.size();
                            updateLastTimeModified(p);
                            //System.out.print("New 12");

                        }
                        int idx = -1;
                        for (int y = 0; y < app.Queue.size(); y++) {
                            if (app.Queue.get(y).patient_id == p.id) {
                                idx = y;
                                break;
                            }
                        }
                        if (group.appointment.order != idx) {
                            group.appointment.order = idx;
                            updateLastTimeModified(p);
                            //System.out.print("New 13");

                        }

                        h = Util.add(group.appointment.date, "0-0-0-" + group.appointment.from);
                        if (Util.isAfter(PathsMine.getCurrentTime(), h) && group.appointment.state == Types.Appointment_Waiting_For_Date) {
                            group.appointment.state = Types.Appointment_Waiting_For_Enter_Line;
                            updateLastTimeModified(p);
                            //System.out.print("New 14");

                        }
                    }
                }
            }
        }

        if (p.notifications.size() > 0) {
            int i = p.notifications.size();
            while (i > 0 && Util.isAfter(p.notifications.get(i - 1).date_added, p.lastTimeUpdated)) {
                i--;
            }

            for (int u = i; u < p.notifications.size(); u++) {
                String f = p.notifications.get(u).date;
                int early = u;
                for (int y = u + 1; y < p.notifications.size(); y++) {
                    String t = p.notifications.get(y).date;
                    if (Util.isAfter(f, t)) {
                        f = t;
                        early = y;
                    }
                }
                GetNotifications.Notification ji = p.notifications.get(early);
                p.notifications.set(early, p.notifications.get(u));
                p.notifications.set(u, ji);
            }
        }
        p.lastTimeUpdated = PathsMine.getCurrentTime();
        savePatient(p);
    }

}
