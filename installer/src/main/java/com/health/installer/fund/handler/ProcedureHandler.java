/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.installer.fund.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.health.installer.fund.utils.Globals;
import static com.health.installer.fund.utils.Globals.Rooms;
import static com.health.installer.fund.utils.Globals.Services;
import com.health.installer.fund.utils.Util;
import com.health.objects.GetProcedure;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Inspiron
 */
public class ProcedureHandler {

    public static GetProcedure.Procedure createInstance(GetProcedure.Procedure temp) {

        GetProcedure.Procedure c;
        if (temp == null) {
            c = new GetProcedure.Procedure();
            initcond(c);
        } else {
            c = makeCopy(temp);
            c.installed = false;
        }

        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < Globals.templateProc.size(); i++) {
            ids.add(Globals.templateProc.get(i).proc_id);
        }

        c.proc_id = Util.generateID(ids);

        return c;
    }

    public static void updateProcedure(GetProcedure.Procedure pr) {
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

    public static void save(GetProcedure.Procedure con) {

        if (con.proc_id == -1999628) {
            List<Long> ids = new ArrayList<>();
            for (int i = 0; i < Globals.templateProc.size(); i++) {
                ids.add(Globals.templateProc.get(i).proc_id);
            }
            con.proc_id = Util.generateID(ids);
        }

        updateProcedure(con);
        for (int i = 0; i < Globals.templateProc.size(); i++) {
            if (Globals.templateProc.get(i).proc_id == con.proc_id) {
                Globals.templateProc.set(i, con);
                return;
            }
        }
        Globals.templateProc.add(con);
    }

    private static void initcond(GetProcedure.Procedure cond) {

        cond.date_activation = "";
        cond.date_deactivation = "";
        cond.objective = "";
        cond.name = "";
        cond.note_med = "";
        cond.note_patient = "";
        cond.feedbacks = new ArrayList<>();
        cond.smart_feedbacks = new ArrayList<>();
        cond.services = new ArrayList<>();
        cond.installed = false;
    }

    public static GetProcedure.Procedure makeCopy(GetProcedure.Procedure c) {

        Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.STATIC).create();

        String json = gson.toJson(c);

        GetProcedure.Procedure cond = gson.fromJson(json, GetProcedure.Procedure.class);
        GetProcedure.Procedure pr = cond;
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

        return cond;
    }

}
