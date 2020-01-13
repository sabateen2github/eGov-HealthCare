package com.health.objects;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.health.requestHandler.RequestHandler;
import java.util.ArrayList;

import java.util.List;
import sun.nio.cs.ext.IBM856;

public class InstallServices {
    
    public static class Request {
        
        public int req_type;
        public List<GetAvailibleServices.Service> services;
        
    }
    
    public static List<Long> InstallServices(List<GetAvailibleServices.Service> services) {
        
        System.out.println("New Line");
        for (int i = 0; i < services.size(); i++) {
            System.out.println(services.get(i));
        }
        
        Request req = new Request();
        req.req_type = Types.Type_Install_Services;
        req.services = services;
        RequestHandler handler = new RequestHandler((new Gson()).toJson(req));
        String json = handler.send();
        if (json == null) {
            return null;
        }
        try {
            return (new Gson()).fromJson(json, (new TypeToken<ArrayList<Long>>() {
            }).getType());
            
        } catch (Exception e) {
            return null;
        }
    }
    
}
