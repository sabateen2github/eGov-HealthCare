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
public class setLang {

    public static class Request {

        public int req_type;
        public int lang;
        public long id;
    }

    public static boolean setLang(int lang, long patient_id) {

        Request req = new Request();
        req.req_type = Types.Type_setLang;
        req.lang = lang;
        req.id = patient_id;
        RequestHandler handler = new RequestHandler((new Gson()).toJson(req));
        String json = handler.send();
        if (json == null) {
            return false;
        }
        if (json.contains(Strings.SUCCESS)) {
            return true;
        } else {
            return false;
        }
    }

}
