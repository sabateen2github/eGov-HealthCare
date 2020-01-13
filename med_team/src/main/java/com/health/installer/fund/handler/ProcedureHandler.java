/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.installer.fund.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.health.installer.fund.utils.Util;
import com.health.objects.GetProcedure;
import com.health.project.medteam.Globals;
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
        }

        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < Globals.templatesProcedures.size(); i++) {
            ids.add(Globals.templatesProcedures.get(i).proc_id);
        }

        c.proc_id = Util.generateID(ids);

        return c;
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

    }

    public static GetProcedure.Procedure makeCopy(GetProcedure.Procedure c) {

        Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.STATIC).create();

        String json = gson.toJson(c);

        GetProcedure.Procedure cond = gson.fromJson(json, GetProcedure.Procedure.class);
        GetProcedure.Procedure pr = cond;

        return cond;
    }

}
