package com.health.objects;

import com.google.gson.Gson;
import com.health.requestHandler.RequestHandler;

import java.util.ArrayList;
import java.util.List;

public class GetNotifications {

    public static final int Notification_Msg = 3;
    public static final int Notification_Activity = 1;
    public static final int Notification_Medication = 2;
    public static final int Notification_Visit_Created = 5;
    public static final int Notification_Visit_Appointment_Change = 4;
    public static final int Notification_Visit_Done = 6;
    public static final int Notification_Visit_Failed = 7;
    public static final int Notification_Condtion_Added = 8;
    public static final int Notification_Condtion_Removed = 9;
    public static final int Notification_Appointment_Near = 10;

    public static List<GetNotifications.Notification> getNotifications(long patient_id) {
        GetNotifications.Request req = new GetNotifications.Request();
        req.req_type = Types.Type_Get_Notifications;
        req.patient_id = patient_id;
        RequestHandler handler = new RequestHandler((new Gson()).toJson(req));
        String json = handler.send();

        if (json == null) {
            return new ArrayList<>();
        }
        Notifications notifications = (new Gson()).fromJson(json, Notifications.class);

        if (notifications == null) {

            return new ArrayList<>();
        }

        return notifications.notifications;
    }

    public static class Request {

        public int req_type;
        public long patient_id;

    }

    public static class Notifications {

        public List<Notification> notifications;
    }

    public static class Notification {

        public long notification_id;
        public String date;
        public int type;
        public long condtion_id;
        public long procedure_id;
        public long medication_id;
        public long activity_id;
        public String msg;
        public String date_added;
    }

}
