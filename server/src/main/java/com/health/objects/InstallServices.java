package com.health.objects;

import com.alaa.server.server;
import com.google.gson.Gson;
import com.health.requestHandler.RequestHandler;
import java.util.ArrayList;

import java.util.List;

public class InstallServices {

    public static class Request {

        public int req_type;
        public List<GetAvailibleServices.Service> services;

    }

    public static List<Long> InstallServices(List<GetAvailibleServices.Service> services) {

        Request req = new Request();
        req.req_type = Types.Type_Install_Services;
        req.services = services;
        RequestHandler handler = new RequestHandler((new Gson()).toJson(req));
        String json = handler.send();
        if (json == null) {
            return null;
        }
        try {
            return server.gson.fromJson(json, (new ArrayList<Long>()).getClass());

        } catch (Exception e) {
            return null;
        }
    }

}
