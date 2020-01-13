/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.installer.fund.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.health.installer.fund.utils.Util;
import com.health.objects.GetCondtion;
import com.health.project.medteam.Globals;
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
        } else {
            c = makeCopy(temp);
        }

        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < Globals.templatesCondtions.size(); i++) {
            ids.add(Globals.templatesCondtions.get(i).condtion_id);
        }
        c.condtion_id = Util.generateID(ids);
        return c;
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
        return cond;
    }

}
