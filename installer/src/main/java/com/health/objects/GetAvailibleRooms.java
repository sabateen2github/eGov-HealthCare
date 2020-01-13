package com.health.objects;

import com.google.gson.Gson;
import com.health.installer.fund.handler.RoomTagHandler;
import com.health.installer.fund.utils.Globals;
import com.health.requestHandler.RequestHandler;

import java.util.ArrayList;
import java.util.List;

public class GetAvailibleRooms {

    public static class Request {

        public int req_type;
    }

    public static List<Room> getAvailibleRooms() {

        Request req = new Request();
        req.req_type = Types.Type_Get_Availible_Rooms;
        RequestHandler handler = new RequestHandler((new Gson()).toJson(req));
        String json = handler.send();
        if (json == null) {
            return new ArrayList<>();
        }
        Rooms in = (new Gson().fromJson(json, Rooms.class));
        return in.rooms;
    }

    public static class Rooms {

        public List<Room> rooms;
    }

    public static class Room {

        public long room_id = Globals.Signature;
        public String room_path;
        public List<GetAvailibleServices.Service> services;
        public transient RoomTagHandler.RoomTag roomtag;
        public transient boolean installed = false;

    }

}
