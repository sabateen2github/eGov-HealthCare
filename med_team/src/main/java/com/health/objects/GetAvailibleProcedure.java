package com.health.objects;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.health.requestHandler.RequestHandler;

import java.util.ArrayList;
import java.util.List;

public class GetAvailibleProcedure {

    public static class Request {

        public int req_type;
        public boolean for_patient;

    }

    public static List<GetProcedure.Procedure> getAvailibleProcedures(boolean for_patient) {

        Request req = new Request();
        req.req_type = Types.Type_Get_Availible_Procedures;
        req.for_patient = for_patient;
        RequestHandler handler = new RequestHandler((new Gson()).toJson(req));
        String json = handler.send();
        if (json == null) {
            return new ArrayList<>();
        }
        List<GetProcedure.Procedure> in = (new Gson().fromJson(json,new TypeToken<ArrayList<GetProcedure.Procedure>>(){}.getType()));
        return in;
    }

}
