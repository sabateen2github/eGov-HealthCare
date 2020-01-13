package com.health.objects;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.health.project.entry.Application;
import com.health.requestHandler.RequestHandler;

import java.util.ArrayList;
import java.util.List;

public class GetAvailibleProcedure {

    public static class Request {

        public int req_type;
        public boolean for_patient;
        public int lang;

    }

    public static List<GetProcedure.Procedure> getAvailibleProcedures(boolean for_patient) {

        Request req = new Request();
        req.req_type = Types.Type_Get_Availible_Procedures;
        req.for_patient = for_patient;
        req.lang = Application.lang;
        RequestHandler handler = new RequestHandler((new Gson()).toJson(req));
        String json = handler.send();
        if (json == null) {
            return new ArrayList<>();
        }
        Log.e("Alaa","Good  "+json);

        List<GetProcedure.Procedure> in = null;
        try {
            in = (new Gson().fromJson(json, new TypeToken<List<GetProcedure.Procedure>>() {
            }.getType()));
        } catch (Exception e) {

            in = new ArrayList<>();
            Log.e("Alaa","GetAvaile  "+json);
        }
        return in;
    }

}
