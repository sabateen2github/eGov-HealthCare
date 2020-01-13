package com.health.objects;

import com.google.gson.Gson;
import com.health.requestHandler.RequestHandler;

import java.util.ArrayList;
import java.util.List;

public class GetActiveProcedures {


    public static class Request {

        public int req_type;
        public long patient_id;

    }


    public static List<GetProcedure.Procedure> getActiveProcedures(long patient_id) {

        Request req = new Request();
        req.req_type = Types.Type_Get_Active_Proc;
        req.patient_id = patient_id;

        RequestHandler handler = new RequestHandler((new Gson()).toJson(req));
        String json = handler.send();
        if (json == null) return new ArrayList<>();
        Procedures proc = (new Gson().fromJson(json, Procedures.class));

        for (int i = 0; i < proc.procedures.size(); i++) {
            proc.procedures.get(i).State_St = Strings.State_Active;

        }

        return proc.procedures;
    }

    public static class Procedures {
        public List<GetProcedure.Procedure> procedures;
    }


}
