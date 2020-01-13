package com.health.objects;

import com.google.gson.Gson;
import com.health.requestHandler.RequestHandler;

public class GetPersonalData {

    public static class Request {

        public int req_type;
        public long patient_id;
    }

    public static PersonalData GetPersonalData(long patient_id) {

        Request req = new Request();
        req.req_type = Types.Type_Get_Personal_Data;
        req.patient_id = patient_id;
        RequestHandler handler = new RequestHandler((new Gson()).toJson(req));
        String json = handler.send();
        if (json == null) {
            return null;
        }
        PersonalData in = (new Gson().fromJson(json, PersonalData.class));
        return in;
    }

    public static class PersonalData {

        public String name;
        public String name_ar;
        public String gender;
        public long insurance_id;
        public String insurance_path;
        public String phone;
        public String address;
        public float height;
        public float weight;
        public String date_of_birth;
        public long last_time_modified;
        public long last_time_server_modified;
        public transient int lang;

        public boolean isSame(PersonalData d) {

            return name.equals(d.name) && gender.equals(d.gender) && insurance_id == d.insurance_id && insurance_path.equals(d.insurance_path) && phone.equals(d.phone)
                    && address.equals(d.address) && height == d.height && weight == d.weight && date_of_birth.equals(d.date_of_birth);

        }

    }

}
