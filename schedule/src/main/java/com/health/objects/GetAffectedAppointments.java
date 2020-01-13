/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.objects;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.health.requestHandler.RequestHandler;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NonNls;

/**
 *
 * @author Inspiron
 */
public class GetAffectedAppointments {

    public static class Request {

        public int req_type;
        public List<Long> room_ids;
        public List<GetRoomsAppointments.Appointment> schedule;
    }

    public static class AffectedAppointment {

        public String room_path;
        public String date;
        public String from;
        public String to;
        public int capacity;
        public int patientsCount;
        public long room_id;
        public long app_id;

    }

    public static List<AffectedAppointment> getAffected(List<GetRoomsAppointments.Appointment> appointments, List<Long> room_ids) {

        Request req = new Request();
        req.req_type = Types.Type_AffectedAppointments;
        req.room_ids = room_ids;
        req.schedule = appointments;
        RequestHandler handler = new RequestHandler((new Gson()).toJson(req));
        @NonNls
        String json = handler.send();
        if (json == null) {
            return null;
        }

        System.out.println("Source   " + json);
        return (new Gson()).fromJson(json, (new TypeToken<ArrayList<AffectedAppointment>>() {
        }).getType());
    }

}
