/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.installer.fund.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.health.installer.fund.utils.Globals;
import static com.health.installer.fund.utils.Globals.Medications;
import static com.health.installer.fund.utils.Globals.Rooms;
import static com.health.installer.fund.utils.Globals.Services;
import static com.health.installer.fund.utils.Globals.templateProc;
import com.health.installer.fund.utils.Util;
import com.health.objects.GetCondtion;
import com.health.objects.GetProcedure;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Inspiron
 */
public class CondtionHandler {

    public static GetCondtion.Condtion createInstance(GetCondtion.Condtion temp) {

        GetCondtion.Condtion c;
        if (temp == null) {
            c = new GetCondtion.Condtion();
            initcond(c);
            c.installed = false;
        } else {
            c = makeCopy(temp);
            c.installed = false;
        }

        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < Globals.condtions.size(); i++) {
            ids.add(Globals.condtions.get(i).condtion_id);
        }
        c.condtion_id = Util.generateID(ids);
        return c;
    }

    public static void updateCondtion(GetCondtion.Condtion cond) {

        for (int y = 0; y < cond.medication_ordinary.size(); y++) {
            for (int u = 0; u < Medications.size(); u++) {
                if (Medications.get(u).medication_id == cond.medication_ordinary.get(y).medication.medication_id) {
                    cond.medication_ordinary.get(y).medication = Medications.get(u);
                }
            }
        }

        for (int y = 0; y < cond.medications_routine.size(); y++) {
            for (int u = 0; u < Medications.size(); u++) {
                if (Medications.get(u).medication_id == cond.medications_routine.get(y).medication.medication_id) {
                    cond.medications_routine.get(y).medication = Medications.get(u);
                }
            }
        }
        for (int y1 = 0; y1 < cond.attached_procedures.size(); y1++) {
            ProcedureHandler.updateProcedure(cond.attached_procedures.get(y1));
        }
        for (int y1 = 0; y1 < cond.procedures.size(); y1++) {
            ProcedureHandler.updateProcedure(cond.procedures.get(y1));
        }
    }

    public static void save(GetCondtion.Condtion con) {

        if (con.condtion_id == -1999628) {
            List<Long> ids = new ArrayList<>();
            for (int i = 0; i < Globals.condtions.size(); i++) {
                ids.add(Globals.condtions.get(i).condtion_id);
            }
            con.condtion_id = Util.generateID(ids);
        }
        updateCondtion(con);
        for (int i = 0; i < Globals.condtions.size(); i++) {
            if (Globals.condtions.get(i).condtion_id == con.condtion_id) {
                Globals.condtions.set(i, con);
                return;
            }
        }
        Globals.condtions.add(con);

    }

    private static void initcond(GetCondtion.Condtion cond) {
        cond.date_activation = "";
        cond.date_deactivation = "";
        cond.desc = "";
        cond.name = "";
        cond.note_med = "";
        cond.note_patient = "";
        cond.activities = new ArrayList<>();
        cond.attached_procedures = new ArrayList<>();
        cond.medication_ordinary = new ArrayList<>();
        cond.medications_routine = new ArrayList<>();
        cond.procedures = new ArrayList<>();
    }

    public static GetCondtion.Condtion makeCopy(GetCondtion.Condtion c) {

        Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.STATIC).create();

        String json = gson.toJson(c);
        GetCondtion.Condtion cond = gson.fromJson(json, GetCondtion.Condtion.class);
        for (int y = 0; y < cond.medication_ordinary.size(); y++) {
            for (int u = 0; u < Medications.size(); u++) {
                if (Medications.get(u).medication_id == cond.medication_ordinary.get(y).medication.medication_id) {
                    cond.medication_ordinary.get(y).medication = Medications.get(u);
                }
            }
        }

        for (int y = 0; y < cond.medications_routine.size(); y++) {
            for (int u = 0; u < Medications.size(); u++) {
                if (Medications.get(u).medication_id == cond.medications_routine.get(y).medication.medication_id) {
                    cond.medications_routine.get(y).medication = Medications.get(u);
                }
            }
        }
        for (int y1 = 0; y1 < cond.attached_procedures.size(); y1++) {

            GetProcedure.Procedure pr = cond.attached_procedures.get(y1);
            for (int x = 0; x < pr.smart_feedbacks.size(); x++) {

                GetProcedure.SmartFeedBack d = pr.smart_feedbacks.get(x);
                for (int y = 0; y < d.services.size(); y++) {
                    for (int u = 0; u < Services.size(); u++) {
                        if (Services.get(u).service_id == d.services.get(y).service_id) {
                            d.services.set(y, Services.get(u));
                        }
                    }
                }
            }

            for (int x = 0; x < pr.services.size(); x++) {
                GetProcedure.ServicesGroup d = pr.services.get(x);
                for (int y = 0; y < d.services.size(); y++) {
                    for (int u = 0; u < Services.size(); u++) {
                        if (Services.get(u).service_id == d.services.get(y).service_id) {
                            d.services.set(y, Services.get(u));
                        }
                    }
                }

                for (int y = 0; y < d.rooms.size(); y++) {
                    for (int u = 0; u < Rooms.size(); u++) {
                        if (Rooms.get(u).room_id == d.rooms.get(y).room_id) {
                            d.rooms.set(y, Rooms.get(u));
                        }
                    }
                }
            }
        }
        for (int y1 = 0; y1 < cond.procedures.size(); y1++) {

            GetProcedure.Procedure pr = cond.procedures.get(y1);
            for (int x = 0; x < pr.smart_feedbacks.size(); x++) {

                GetProcedure.SmartFeedBack d = pr.smart_feedbacks.get(x);
                for (int y = 0; y < d.services.size(); y++) {
                    for (int u = 0; u < Services.size(); u++) {
                        if (Services.get(u).service_id == d.services.get(y).service_id) {
                            d.services.set(y, Services.get(u));
                        }
                    }
                }
            }

            for (int x = 0; x < pr.services.size(); x++) {
                GetProcedure.ServicesGroup d = pr.services.get(x);
                for (int y = 0; y < d.services.size(); y++) {
                    for (int u = 0; u < Services.size(); u++) {
                        if (Services.get(u).service_id == d.services.get(y).service_id) {
                            d.services.set(y, Services.get(u));
                        }
                    }
                }

                for (int y = 0; y < d.rooms.size(); y++) {
                    for (int u = 0; u < Rooms.size(); u++) {
                        if (Rooms.get(u).room_id == d.rooms.get(y).room_id) {
                            d.rooms.set(y, Rooms.get(u));
                        }
                    }
                }
            }
        }
        return cond;
    }

}
