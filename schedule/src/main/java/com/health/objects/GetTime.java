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
public class GetTime {

    public static class Request {

        int req_type;

    }

    public static Time getTime() {

        GetCondtion.Request req = new GetCondtion.Request();
        req.req_type = Types.Type_GetTime;
        RequestHandler handler = new RequestHandler((new Gson()).toJson(req));
        String json = handler.send();
        if (json == null) {
            return null;
        }
        Time in = (new Gson().fromJson(json, Time.class));
        return in;

    }

    public static class Time {

        public int year;
        public int month;
        public int days;
        public int hour;
        public int min;

    }

}
