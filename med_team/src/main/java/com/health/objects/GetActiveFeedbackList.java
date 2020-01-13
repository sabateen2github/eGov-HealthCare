package com.health.objects;

import com.google.gson.Gson;
import com.health.requestHandler.RequestHandler;

import java.util.ArrayList;
import java.util.List;

public class GetActiveFeedbackList {

    public static class Request {

        public int req_type;
        public long med_team_id;

    }

    public static List<Patient> getActiveFeedBack(long med_team_id) {
        Request req = new Request();
        req.req_type = Types.Type_Get_Active_Feedback_List;
        req.med_team_id = med_team_id;

        RequestHandler handler = new RequestHandler((new Gson()).toJson(req));
        String json = handler.send();
        if (json == null) {
            return new ArrayList<>();
        }

        Patients patients = (new Gson().fromJson(json, Patients.class));
        return patients.patients;
    }

    public static class Patients {

        public List<Patient> patients;
    }

    public static class Patient {

        public long patient_id;
        public long procedure_id;
        public long req_id;
        public String patient_name;
        public String date;
        public int age;
        public int remaining_days;
        public String proc_date;
    }

}
