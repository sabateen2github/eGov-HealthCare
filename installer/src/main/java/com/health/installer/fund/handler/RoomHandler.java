/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.installer.fund.handler;

import com.health.installer.fund.utils.Globals;
import static com.health.installer.fund.utils.Globals.Rooms;
import static com.health.installer.fund.utils.Globals.roomtags;
import com.health.installer.fund.utils.Util;
import com.health.objects.GetAvailibleRooms;
import java.util.ArrayList;

/**
 *
 * @author Inspiron
 */
public class RoomHandler {

    public static GetAvailibleRooms.Room createRoom() {
        GetAvailibleRooms.Room room = new GetAvailibleRooms.Room();
        room.services = new ArrayList<>();
        return room;
    }

    public static GetAvailibleRooms.Room getRoom(long id) {
        for (GetAvailibleRooms.Room room : Globals.Rooms) {
            if (room.room_id == id) {
                return room;
            }
        }
        return null;
    }

    public static long saveRoom(GetAvailibleRooms.Room r) {

        for (GetAvailibleRooms.Room room : Globals.Rooms) {
            if (room.room_id == r.room_id || room.room_path.equals(r.room_path)) {
                room.room_path = r.room_path;
                room.services = r.services;
                return room.room_id;
            }
        }
        ArrayList<Long> ids = new ArrayList<>();
        for (GetAvailibleRooms.Room room : Globals.Rooms) {
            ids.add(room.room_id);
        }
        long id = Util.generateID(ids);
        r.room_id = id;
        Globals.Rooms.add(r);

        RoomTagHandler.RoomTag tag = r.roomtag;
        r.services.clear();
        if (tag != null) {
            for (int x = 0; x < roomtags.size(); x++) {
                if (roomtags.get(x).id == tag.id) {
                    r.roomtag = roomtags.get(x);
                }
            }
            for (int y = 0; y < tag.services.size(); y++) {
                r.services.add(ServiceHandler.getService(tag.services.get(y)));
            }
        }
        return id;
    }

}
