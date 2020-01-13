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
public class Reset {
    
    public static class Request {
        
        int req_type;
        
    }
    
    public static boolean reset() {
        
        GetCondtion.Request req = new GetCondtion.Request();
        req.req_type = Types.Type_Reset;
        RequestHandler handler = new RequestHandler((new Gson()).toJson(req));
        String json = handler.send();
        if (json == null) {
            return false;
        }
        return json.contains(Strings.SUCCESS);
        
    }
}
