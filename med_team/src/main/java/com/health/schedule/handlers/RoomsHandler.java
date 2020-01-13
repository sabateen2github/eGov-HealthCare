/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.schedule.handlers;

import com.health.objects.GetAvailibleRooms;
import com.health.objects.GetAvailibleServices;
import com.health.project.medteam.Globals;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Inspiron
 */
public class RoomsHandler {

    public static List<GetAvailibleRooms.Room> getRoomsForServices(List<GetAvailibleServices.Service> ser) {
        List<GetAvailibleRooms.Room> rooms = new ArrayList<>();
        for (int i = 0; i < Globals.rooms.size(); i++) {
            boolean valid = true;
            for (int y = 0; y < ser.size(); y++) {
                boolean found = false;
                for (int u = 0; u < Globals.rooms.get(i).services.size(); u++) {

                    if (ser.get(y).service_id == Globals.rooms.get(i).services.get(u).service_id) {
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
                rooms.add(Globals.rooms.get(i));
            }
        }
        return rooms;
    }

}
