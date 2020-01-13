package com.health.objects;

import com.google.gson.Gson;
import com.health.requestHandler.RequestHandler;

import java.util.ArrayList;
import java.util.List;

public class GetRoomsAppointments {

    public static class Request {

        public int req_type;
        public List<Long> rooms;
        public int lang;

    }

    public static List<Appointment> GetRoomsAppointments(List<Long> rooms) {

        Request req = new Request();
        req.req_type = Types.Type_Get_Rooms_Appointments;
        req.rooms = rooms;
        RequestHandler handler = new RequestHandler((new Gson()).toJson(req));
        String json = handler.send();
        if (json == null) {
            return new ArrayList<>();
        }
        Appointments cond = (new Gson().fromJson(json, Appointments.class));
        return cond.appointments;
    }

    public static class Appointments {

        public List<Appointment> appointments;
    }

    public static class Appointment {

        public long room_id;
        public long appointment_id;
        public String room_path;
        public String date;
        public String from;
        public String to;
        public int capacity;

    }

}
