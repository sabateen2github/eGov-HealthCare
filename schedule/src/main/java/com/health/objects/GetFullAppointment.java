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
public class GetFullAppointment {

    public static class Request {

        public int req_type;
        public long room_id;
    }

    public static class FullAppointment {

        public String room_path;
        public String date;
        public String from;
        public String to;
        public int capacity;
        public int patientsCount;
        public long room_id;
        public long app_id;

    }

    public static List<FullAppointment> getFull(long room_id) {

        Request req = new Request();
        req.req_type = Types.Type_GetRoomFullAppintments;
        req.room_id = room_id;
        RequestHandler handler = new RequestHandler((new Gson()).toJson(req));
        @NonNls
        String json = handler.send();
        if (json == null) {
            return null;
        }
        return (new Gson()).fromJson(json, (new TypeToken<ArrayList<FullAppointment>>() {
        }).getType());
    }

}
