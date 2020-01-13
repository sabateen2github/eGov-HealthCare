package com.health.objects;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.health.requestHandler.RequestHandler;
import java.util.ArrayList;

import java.util.List;

public class InstallRooms {

    public static class Request {

        public int req_type;
        public List<GetAvailibleRooms.Room> rooms;
    }

    public static List<Long> InstallProceduresTemplates(List<GetAvailibleRooms.Room> rooms) {

        Request req = new Request();
        req.req_type = Types.Type_Install_Rooms;
        req.rooms = rooms;
        RequestHandler handler = new RequestHandler((new Gson()).toJson(req));
        String json = handler.send();
        if (json == null) {
            return null;
        }
        try {
            return (new Gson()).fromJson(json,(new TypeToken<ArrayList<Long>>(){}).getType());

        } catch (Exception e) {
            return null;
        }
    }

}
