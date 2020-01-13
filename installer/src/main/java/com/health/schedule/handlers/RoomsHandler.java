/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.schedule.handlers;

import com.health.installer.fund.utils.Globals;
import com.health.objects.GetAvailibleRooms;
import com.health.objects.GetAvailibleServices;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Inspiron
 */
public class RoomsHandler {

    public static List<GetAvailibleRooms.Room> getRoomsForServices(List<GetAvailibleServices.Service> ser) {
        List<GetAvailibleRooms.Room> rooms = new ArrayList<>();
        for (int i = 0; i < Globals.Rooms.size(); i++) {
            boolean valid = true;
            for (int y = 0; y < ser.size(); y++) {
                boolean found = false;
                if (Globals.Rooms.get(i).roomtag == null) {
                    continue;

                }
                for (int u = 0; u < Globals.Rooms.get(i).roomtag.services.size(); u++) {
                    if (ser.get(y).service_id == Globals.Rooms.get(i).roomtag.services.get(u)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    valid = false;
                    break;
                }
            }
            if (valid) {
                rooms.add(Globals.Rooms.get(i));
            }
        }
        return rooms;
    }
}
