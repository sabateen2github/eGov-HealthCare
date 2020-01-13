package com.health.objects;

import com.google.gson.Gson;
import com.health.requestHandler.RequestHandler;
import java.util.ArrayList;

import java.util.List;

public class GetRoomCurrentAppointment {

    public static class Request {

        public int req_type;
        public long room_id;

    }

    public static CurrentAppointment GetRoomCurrentAppointment(long room_id) {

        Request req = new Request();
        req.req_type = Types.Type_Get_Room_Current_Appointment;
        req.room_id = room_id;
        RequestHandler handler = new RequestHandler((new Gson()).toJson(req));
        String json = handler.send();
        if (json == null) {
            return null;
        }
        CurrentAppointment in = (new Gson().fromJson(json, CurrentAppointment.class));
        return in;
    }

    public static class CurrentAppointment {

        public long room_id;
        public long appointment_id;
        public String date;
        public String from;
        public String to;
        public int capacity;
        public int queue_amount;

        public List<Patient> patients=new ArrayList<>();
        public CurrentPatient current;
    }

    public static class CurrentPatient {
        public long patient_id;
        public long procedure_id;
        public long request;
        public String name;
        public String age;
    }

    public static class Patient {

        public String name;
        public String age;
    }
    

}
