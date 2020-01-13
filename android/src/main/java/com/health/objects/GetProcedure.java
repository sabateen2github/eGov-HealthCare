package com.health.objects;

import com.google.gson.Gson;
import com.health.requestHandler.RequestHandler;

import java.util.List;

public class GetProcedure {

    public static class Request {

        public int req_type;
        public long patient_id;
        public long proc_id;

    }

    public static Procedure getProcedure(long patient_id, long proc_id) {
        Request req = new Request();
        req.req_type = Types.Type_Get_Procedure;
        req.proc_id = proc_id;
        req.patient_id = patient_id;
        RequestHandler handler = new RequestHandler((new Gson()).toJson(req));
        String json = handler.send();
        if (json == null) {
            return null;
        }
        Procedure in = (new Gson().fromJson(json, Procedure.class));

        if (in.state == Types.State_Proc_History) {


            boolean is_fail = false;
            for (int i = 0; i < in.services.size(); i++) {

                if (in.services.get(i).state == Types.Request_Failed) {
                    is_fail = true;
                    break;
                }

            }

            if (!is_fail) {
                for (int i = 0; i < in.feedbacks.size(); i++) {

                    if (in.feedbacks.get(i).state == Types.Request_Failed) {
                        is_fail = true;
                        break;
                    }
                }
            }
            if (!is_fail) {
                for (int i = 0; i < in.smart_feedbacks.size(); i++) {

                    if (in.smart_feedbacks.get(i).state == Types.Request_Failed) {
                        is_fail = true;
                        break;
                    }
                }
            }

            if (is_fail) {

                in.State_St = Strings.State_Failed;
            } else {
                in.State_St = Strings.State_Done;

            }


        } else {

            in.State_St = Strings.State_Active;
        }

        return in;
    }

    public static class Procedure {

        public long proc_id;
        public long condtion_id;
        public String name;
        public int activation_period;
        public int remaining_days;
        public String note_med;
        public String note_patient;
        public String objective;
        public int state;
        public String date_activation;
        public String date_deactivation;
        public List<FeedBack> feedbacks;
        public List<SmartFeedBack> smart_feedbacks;
        public List<ServicesGroup> services;
        public int type;
        public int delay_period;
        public int routine_activation_period;
        public int cycle_period;

        public transient String Routine_period_String;
        public transient String State_St;

    }

    public static class ServicesGroup {

        public long req_id;
        public int state;
        public int order;
        public int count;
        public String note_med;
        public String note_res;//deprecated
        public Appointment appointment;
        public List<GetAvailibleServices.Service> services;
        public List<GetAvailibleRooms.Room> rooms;
    }

    public static class Appointment {

        public int state;
        public long room_id;
        public long appointment_id = 0;
        public String room_path;
        public String date;
        public String from; // h's:m's_am/pm
        public String to;
        public int order;
        public int queue_amount;
    }

    public static class SmartFeedBack {

        public long req_id;
        public int state;
        public List<GetAvailibleServices.Service> services;
        public int order;
        public String note_med;
        public long session_id;
    }

    public static class FeedBack {

        public long req_id;
        public int state;
        public int order;
        public String note_med;
        public long med_team_id;
        public String med_team_name;
    }

}
