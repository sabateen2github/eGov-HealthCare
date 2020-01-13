/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.installer.fund.handler;

import com.health.installer.fund.utils.Globals;
import com.health.installer.fund.utils.Util;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Inspiron
 */
public class RoomTagHandler {

    public static RoomTag createRoomTag() {
        RoomTag tag = new RoomTag();
        tag.rooms = new ArrayList<>();
        tag.services = new ArrayList<>();
        return tag;

    }

    public static RoomTag getRoomTag(long id) {

        for (RoomTag tag : Globals.roomtags) {
            if (tag.id == id) {
                return tag;
            }
        }
        return null;
    }

    public static long save(RoomTag t) {

        for (RoomTag tag : Globals.roomtags) {
            if (tag.id == t.id || tag.name.equals(t.name)) {
                tag.name = t.name;
                tag.rooms = t.rooms;
                tag.services = t.services;
                return t.id;
            }
        }
        ArrayList<Long> ids = new ArrayList<>();
        for (RoomTag tag : Globals.roomtags) {
            ids.add(tag.id);
        }

        long id = Util.generateID(ids);
        t.id = id;
        Globals.roomtags.add(t);

        for (int i = 0; i < Globals.Rooms.size(); i++) {
            if (t.rooms.contains(Globals.Rooms.get(i).room_id)) {
                for (int y = 0; y < t.services.size(); y++) {
                    Globals.Rooms.get(i).services.add(ServiceHandler.getService(t.services.get(y)));
                }
            }
        }

        return id;
    }

    public static class RoomTag {

        public String name;
        public long id;
        public List<Long> services;
        public List<Long> rooms;
    }
}
