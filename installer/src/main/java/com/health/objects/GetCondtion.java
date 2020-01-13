package com.health.objects;

import com.google.gson.Gson;
import com.health.installer.fund.handler.PriceTagHandler;
import com.health.installer.fund.utils.Globals;
import com.health.requestHandler.RequestHandler;

import java.util.List;

public class GetCondtion {

    public static class Request {

        public int req_type;
        public long patient_id;
        public long condtion_id;

    }

    public static Condtion getCondtion(long patient_id, long condtion_id) {

        Request req = new Request();
        req.req_type = Types.Type_Get_Condtion;
        req.condtion_id = condtion_id;
        req.patient_id = patient_id;
        RequestHandler handler = new RequestHandler((new Gson()).toJson(req));
        String json = handler.send();
        if (json == null) {
            return null;
        }
        Condtion in = (new Gson().fromJson(json, Condtion.class));
        return in;
    }

    public static class Condtion {

        public int state;
        public long condtion_id;
        public String name;
        public int activation_period;
        public int remaining_days;
        public String date_activation;
        public String date_deactivation;
        public String desc;
        public String note_med;
        public String note_patient;
        public List<MedicationRoutine> medications_routine;
        public List<MedicationOrdinary> medication_ordinary;
        public List<Activity> activities;
        public List<GetProcedure.Procedure> procedures;
        public List<GetProcedure.Procedure> attached_procedures;
        public transient boolean installed = false;
    }

    public static class Medication {

        public long medication_id = Globals.Signature;
        public String medication_name;
        public float price;
        public String route_type;
        public String prescription_unit;
        public List<GetAvailibleInsurances.Insurance> insurances;
        public transient PriceTagHandler.PriceTag priceTag;
        public transient boolean installed = false;

    }

    public static class MedicationOrdinary {

        public Medication medication;
        public String indications;
        public String dosage_amount;
        public String note_patient;
        public String note_med;
        public int prescribed_amount;
        public int prescription_cycle;
        public int prescription_cycle_unit;
        public int delay_period;
        public int activation_period;

    }

    public static class MedicationRoutine {

        public Medication medication;
        public float dosage_cycle;
        public int cycle_unit;
        public String dosage_amount;
        public String note_patient;
        public String note_med;
        public int prescribed_amount;
        public int delay_period;
        public int activation_period;

    }

    public static class Activity {

        public long activity_id;
        public int delay_period;
        public int activation_period;
        public int cycle_period;
        public int cycle_period_unit;
        public String description;
        public String objective;
        public String note_med;
    }

}
