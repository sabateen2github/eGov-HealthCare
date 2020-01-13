package com.health.objects;

import com.google.gson.Gson;
import com.health.requestHandler.RequestHandler;

public class BookAppointment {

    public static class Request {

        public int req_type;
        public long patient_id;
        public long procedure_id;
        public long request_id;
        public long room_id;
        public long appointment_id;

    }

    public static boolean bookAppointment(long proc, long room_id, long appointment_id, long reqs_id, long id) {
        Request req = new Request();
        req.req_type = Types.Type_Book_Appointment;
        req.patient_id = id;
        req.appointment_id = appointment_id;
        req.procedure_id = proc;
        req.room_id = room_id;

        req.request_id = reqs_id;

        String json = (new Gson()).toJson(req);
        RequestHandler handler = new RequestHandler(json);
        String msg = handler.send();
        return msg != null;
    }

}
