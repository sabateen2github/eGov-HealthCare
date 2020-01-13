/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.objects;

import com.google.gson.Gson;
import com.health.requestHandler.RequestHandler;

/**
 *
 * @author Inspiron
 */
public class CancelAppointment {

    public static class Request {

        public int req_type;
        public long room_id;
        public long appointment_id;
    }

    public static boolean CancelAppointment(long room_id, long appointment_id) {
        Request r = new Request();
        r.req_type = Types.Type_CancelAppointment;
        r.room_id = room_id;
        r.appointment_id = appointment_id;

        String json = (new Gson()).toJson(r);
        RequestHandler handler = new RequestHandler(json);
        String res = handler.send();
        return res != null && res.contains(Strings.SUCCESS);
    }

}
