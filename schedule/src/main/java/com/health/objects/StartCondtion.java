package com.health.objects;

import com.google.gson.Gson;
import com.health.requestHandler.RequestHandler;

public class StartCondtion {

    public static class Request {

        public int req_type;
        public long patient_id;
        public GetCondtion.Condtion condtion;

    }

    public static long StartCondtion(long patient_id, GetCondtion.Condtion cond) {
        Request req = new Request();
        req.req_type = Types.Type_StartCondtion;
        req.patient_id = patient_id;
        req.condtion = cond;
        RequestHandler handler = new RequestHandler((new Gson()).toJson(req));
        String json = handler.send();
        if (json == null) {
            return 0;
        }
        if (json.contains("Error")) {
            return 0;
        } else {
            return ((Response)(new Gson()).fromJson(json, Response.class)).id;
        }
    }

    public static class Response {

        public long id;

    }

}
